package games.negative.lce.listener;

import games.negative.alumina.logger.Logs;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class LegacyPhysicsListener implements Listener {

    public KnockbackConfig knockback() {
        return CombatPlugin.configs().knockback();
    }

    public PhysicsConfig physics() {
        return CombatPlugin.configs().physics();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageWhileBlocking(EntityDamageByEntityEvent event) {
        if (event.isCancelled()
                || !(event.getEntity() instanceof Player player)
                || !CombatCheck.checkCombat(player)
                || !CombatCheck.isBlockingWithSword(player)) return;

        double reduction = physics().getDamageReductionWhileBlockingWithSword() / 100D;

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
        if (event.isCancelled() || !(event.getHitEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation())) return;

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

        if (!(event.getCaught() instanceof LivingEntity victim) || !CombatCheck.checkCombat(victim.getLocation())) return;

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

        if (!(event.getEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation())) return;

        games.negative.lce.struct.Vector vector = knockback().getKnockback().get(event.getCause());
        if (vector == null) return;

        Vector knockback = event.getKnockback().multiply(vector.toBukkitVector());
        event.setKnockback(knockback);
    }
}
