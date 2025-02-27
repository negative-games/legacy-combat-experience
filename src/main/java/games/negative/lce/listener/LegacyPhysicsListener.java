package games.negative.lce.listener;

import games.negative.lce.CombatPlugin;
import games.negative.lce.config.Config;
import games.negative.lce.util.CombatCheck;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class LegacyPhysicsListener implements Listener {

    public Config.Adjustments adjustments() {
        return CombatPlugin.config().adjustments();
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
    public void onHit(ProjectileHitEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof FishHook hook) || !(event.getHitEntity() instanceof LivingEntity entity) || !CombatCheck.checkCombat(entity.getLocation())) return;

        entity.damage(0.01, hook);

        Vector velocity = hook.getVelocity().multiply(-1);

        entity.knockback(1, velocity.getX(), velocity.getZ());
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
