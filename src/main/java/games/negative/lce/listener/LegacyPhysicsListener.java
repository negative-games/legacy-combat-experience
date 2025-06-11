package games.negative.lce.listener;

import games.negative.alumina.util.Tasks;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.KnockbackConfig;
import games.negative.lce.config.PhysicsConfig;
import games.negative.lce.struct.SpeedVector;
import games.negative.lce.util.CombatCheck;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class LegacyPhysicsListener implements Listener {

    public KnockbackConfig knockback() {
        return CombatPlugin.configs().knockback();
    }

    public PhysicsConfig physics() {
        return CombatPlugin.configs().physics();
    }

    /**
     * Adjust the velocity of thrown potions to be more like 1.8
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPotionThrow(ProjectileLaunchEvent event) {
        if (event.isCancelled()
                || !CombatCheck.checkCombat(event.getLocation())
                || !physics().isEnableLegacyPotionPhysics()
                || !(event.getEntity() instanceof ThrownPotion potion)) return;

        ProjectileSource shooter = potion.getShooter();
        if (!(shooter instanceof Player player)) return;

        double speed = potion.getVelocity().length();
        Vector direction = player.getLocation().getDirection();

        Vector velocity = direction.multiply(speed / physics().getLegacyPotionVelocityReduction());

        potion.setVelocity(velocity);
    }

    /**
     * Nerf health regeneration while in combat to be more like 1.8
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onHealthRegeneration(EntityRegainHealthEvent event) {
        if (event.isCancelled()
                || !(event.getEntity() instanceof Player player)
                || !CombatCheck.checkCombat(player)
                || event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) return;

        event.setAmount(event.getAmount() / 2);
    }

    /**
     * Adjust the damage of an attack while the victim is blocking with a sword
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageWhileBlocking(EntityDamageByEntityEvent event) {
        if (event.isCancelled()
                || !(event.getEntity() instanceof Player player)
                || !CombatCheck.checkCombat(player)
                || !CombatCheck.isBlockingWithSword(player)) return;

        double reduction = physics().getDamageReductionWhileBlockingWithSword();

        event.setDamage(event.getDamage() / reduction);
    }

    /*
     * Adjust the velocity of arrows to be more like 1.8
     * Credit to https://github.com/Heklo1/StraightArrows/
     * for solving this issue; I did not even know EntityShootBowEvent existed
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onArrowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)
                || event.isCancelled()
                || !CombatCheck.checkCombat(event.getEntity().getLocation())
                || !(event.getProjectile() instanceof Arrow arrow)
                || !(event.getBow() != null && event.getBow().getType().equals(Material.BOW))) return;

        boolean isBoosting = knockback().isEnableBowBoost() && event.getForce() <= knockback().getBowBoostThreshold();
        double speed = arrow.getVelocity().length();

        Vector direction = player.getLocation().getDirection();

        Vector velocity = direction.multiply(isBoosting ? speed / 2 : speed);

        arrow.setVelocity(velocity);

        // Only run the task if the player is bow boosting
        if (!isBoosting) return;

        Tasks.run(new ArrowTask(arrow));
    }

    @RequiredArgsConstructor
    private static class ArrowTask extends BukkitRunnable {
        private final Arrow arrow;

        @Override
        public void run() {
            arrow.setHasLeftShooter(true);
        }
    }

    /*
     * Adjust the velocity of "knockback" projectiles to be more like 1.8
     * "knockback" projectiles are snowballs, eggs, etc.
     */

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.isCancelled() || !(event.getHitEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation()))
            return;

        Projectile projectile = event.getEntity();
        if (!knockback().isKnockbackProjectile(projectile.getType())) return;

        entity.damage(knockback().getKnockbackProjectileDamage(), projectile);

        Vector velocity = projectile.getVelocity().multiply(-1);

        entity.knockback(knockback().getKnockbackProjectileKnockbackStrength(), velocity.getX(), velocity.getZ());
    }


    /*
     * Adjust the velocity of self-shot arrows to be more like 1.8
     * This is also known as "bow boosting"
     */


    /**
     * Credit to https://github.com/Heklo1/StraightArrows/
     * for the original "bow boosting" simulation code
     * but modified to use EntityKnockbackEvent instead of PlayerVelocityEvent
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSelfProjectileKnockback(EntityKnockbackEvent event) {
        if (event.isCancelled()
                || !(event.getEntity() instanceof Player player)
                || !CombatCheck.checkCombat(player)
                || !knockback().isEnableBowBoost())
            return;

        Vector initial = event.getKnockback();

        EntityDamageEvent damageEvent = player.getLastDamageCause();
        if (!(damageEvent instanceof EntityDamageByEntityEvent damage)
                || !(damage.getDamager() instanceof Arrow arrow)
                || !(arrow.getShooter() instanceof Player shooter)
                || !Objects.equals(player.getUniqueId(), shooter.getUniqueId()))
            return;

        SpeedVector speed = knockback().getBowBoostSpeed();
        double horizontal = speed.horizontal();
        double vertical = speed.vertical();

        double horizontalSpeed = Math.sqrt(initial.getX() * initial.getX() + initial.getZ() * initial.getZ()) * horizontal;
        double verticalSpeed = initial.getY() * vertical;

        Vector dir = arrow.getLocation().getDirection().normalize();
        Vector newVelocity = new Vector(-dir.getX() * horizontalSpeed, initial.getY() * verticalSpeed, dir.getZ() * horizontalSpeed);

        event.setKnockback(newVelocity);
    }

    /*
     * Adjust the velocity of fishing rods to be more like 1.8
     */

    @EventHandler(priority = EventPriority.HIGH)
    public void onCast(ProjectileLaunchEvent event) {
        if (event.isCancelled() || !CombatCheck.checkCombat(event.getLocation())) return;

        Projectile entity = event.getEntity();
        if (!(entity instanceof FishHook hook)) return;

        Vector velocity = hook.getVelocity();
        Vector adjusted = velocity.multiply(physics().getFishingRodVelocity().toBukkitVector());

        hook.setVelocity(adjusted);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onReel(PlayerFishEvent event) {
        if (event.isCancelled() || event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;

        if (!(event.getCaught() instanceof LivingEntity victim) || !CombatCheck.checkCombat(victim.getLocation()))
            return;

        event.setCancelled(true);

        FishHook hook = event.getHook();
        hook.remove();
    }

    /*
     * Adjust general player knockback to be more like 1.8/customized
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onKnockback(EntityKnockbackEvent event) {
        if (event.isCancelled() || !knockback().isEnabled()) return;

        if (!(event.getEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation()))
            return;

        games.negative.lce.struct.Vector vector = knockback().getKnockback().get(event.getCause());
        if (vector == null) return;

        Vector knockback = event.getKnockback().multiply(vector.toBukkitVector());
        event.setKnockback(knockback);
    }

    /*
     * Disable the offhand slot
     */

    @EventHandler(priority = EventPriority.HIGH)
    public void onOffhandSwap(PlayerSwapHandItemsEvent event) {
        if (event.isCancelled()
                || !physics().isDisableOffhand()
                || !CombatCheck.checkCombat(event.getPlayer())
                || event.getOffHandItem().getType().isAir()) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onOffhandInventoryClick(InventoryClickEvent event) {
        if (event.isCancelled()
                || !physics().isDisableOffhand()
                || event.getInventory().getType() != InventoryType.CRAFTING
                || !(event.getWhoClicked() instanceof Player player)
                || !CombatCheck.checkCombat(player)) return;

        if (event.getClick() != ClickType.SWAP_OFFHAND && event.getSlot() != 40) return;

        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleportToRemoveOffhand(PlayerTeleportEvent event) {
        if (event.isCancelled()
                || !physics().isDisableOffhand()
                || !CombatCheck.checkCombat(event.getTo())) return;

        Player player = event.getPlayer();

        PlayerInventory inventory = player.getInventory();

        ItemStack item = inventory.getItemInOffHand();
        if (item.getType().isAir()) return;

        inventory.addItem(item).forEach((integer, itemStack) -> {
            player.dropItem(EquipmentSlot.OFF_HAND);
        });

        inventory.setItemInOffHand(null);
        player.updateInventory();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMoveToRemoveOffhand(PlayerMoveEvent event) {
        if (event.isCancelled()
                || !physics().isDisableOffhand()
                || event.getPlayer().getInventory().getItemInOffHand().getType().isAir()
                || !CombatCheck.checkCombat(event.getTo())) return;

        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        ItemStack item = inventory.getItemInOffHand();

        inventory.addItem(item).forEach((integer, itemStack) -> {
            player.dropItem(EquipmentSlot.OFF_HAND);
        });

        inventory.setItemInOffHand(null);
        player.updateInventory();
    }

    /**
     * Set axe damage to legacy 1.8 values
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onAxeDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()
                || !(event.getDamager() instanceof Player player)
                || !CombatCheck.checkCombat(player)
                || !physics().isOldschoolAxeDamage()) return;
            
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().name().contains("_AXE")) return;
        
        // Set damage to 1.8 values
        double damage = switch(item.getType()) {
            case WOODEN_AXE -> 4.0; // 2 hearts
            case STONE_AXE -> 5.0;  // 2.5 hearts
            case IRON_AXE -> 6.0;   // 3 hearts
            case GOLDEN_AXE -> 4.0; // 2 hearts
            case DIAMOND_AXE -> 7.0; // 3.5 hearts
            case NETHERITE_AXE -> 8.0; // 4 hearts
            default -> event.getDamage();
        };
        
        event.setDamage(damage);
    }

    /**
     * Adjust critical hit damage
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onCriticalHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()
                || !(event.getDamager() instanceof Player player)
                || !CombatCheck.checkCombat(player)) return;
            
        boolean isCritical = player.getFallDistance() > 0.0F 
                && !player.isOnGround() 
                && !player.isInWater() 
                && !player.hasPotionEffect(PotionEffectType.BLINDNESS);
            
        // Check if we should cancel crit for sprinting players
        if (isCritical && physics().isCancelCritIfSprinting() && player.isSprinting()) {
            isCritical = false;
        }
        
        if (isCritical) {
            event.setDamage(event.getDamage() * physics().getCriticalModifier());
        }
    }

    /**
     * Apply 1.8 sharpness damage calculations
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onSharpnessDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()
                || !(event.getDamager() instanceof Player player)
                || !CombatCheck.checkCombat(player)
                || !physics().isOldSharpnessDamageBuff()) return;
            
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getEnchantmentLevel(Enchantment.SHARPNESS) <= 0) return;
        
        // Get the sharpness level
        int level = item.getEnchantmentLevel(Enchantment.SHARPNESS);
        
        // In 1.8, each level of sharpness adds 1.25 damage (modern is 0.5 + 0.5 * level)
        double oldDamage = event.getDamage();
        double newDamage = oldDamage - (0.5 + (0.5 * level)) + (1.25 * level);
        
        event.setDamage(newDamage);
    }
}
