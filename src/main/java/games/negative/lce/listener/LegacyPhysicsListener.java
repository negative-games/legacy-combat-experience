package games.negative.lce.listener;

import games.negative.lce.CombatPlugin;
import games.negative.lce.config.Config;
import games.negative.lce.struct.SpeedVector;
import games.negative.lce.util.CombatCheck;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

import java.util.Objects;

public class LegacyPhysicsListener implements Listener {

    public Config.Adjustments adjustments() {
        return CombatPlugin.config().adjustments();
    }

    /*
     * Adjust the velocity of arrows to be more like 1.8
     * Credit to https://github.com/Heklo1/StraightArrows/
     * for solving this issue, I did not even know EntityShootBowEvent existed
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onArrowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player) || event.isCancelled() || !CombatCheck.checkCombat(event.getEntity().getLocation())) return;

        Entity projectile = event.getProjectile();

        double speed = projectile.getVelocity().length();
        Vector direction = player.getLocation().getDirection();

        Vector velocity = direction.multiply(speed);

        projectile.setVelocity(velocity);
    }

    /*
        * Adjust the velocity of "knockback" projectiles to be more like 1.8
        * "knockback" projectiles are snowballs, eggs, etc.
     */

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.isCancelled() || !(event.getHitEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation())) return;

        Projectile projectile = event.getEntity();
        if (!adjustments().isKnockbackProjectile(projectile.getType())) return;

        entity.damage(adjustments().getKnockbackProjectileDamage(), projectile);

        Vector velocity = projectile.getVelocity().multiply(-1);

        entity.knockback(adjustments().getKnockbackProjectileKnockbackStrength(), velocity.getX(), velocity.getZ());
    }

    /**
     * Credit to https://github.com/Heklo1/StraightArrows/
     * for the original "bow boosting" simulation code
     * but modified to use EntityKnockbackEvent instead of PlayerVelocityEvent
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSelfProjectileHit(EntityKnockbackEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player player) || !CombatCheck.checkCombat(player))
            return;

        Vector initial = event.getKnockback();

        EntityDamageEvent damageEvent = player.getLastDamageCause();
        if (!(damageEvent instanceof EntityDamageByEntityEvent damage)
                || !(damage.getDamager() instanceof Arrow arrow)
                || !(arrow.getShooter() instanceof Player shooter)
                || !Objects.equals(player.getUniqueId(), shooter.getUniqueId()))
            return;

        SpeedVector speed = adjustments().getBowBoostSpeed();
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
        Vector adjusted = velocity.multiply(adjustments().getFishingRodVelocity().toBukkitVector());

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
        if (event.isCancelled() || !adjustments().isUseCustomKnockback()) return;

        if (!(event.getEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation())) return;

        games.negative.lce.struct.Vector vector = adjustments().getKnockback().get(event.getCause());
        if (vector == null) return;

        Vector knockback = event.getKnockback().multiply(vector.toBukkitVector());
        event.setKnockback(knockback);
    }
}
