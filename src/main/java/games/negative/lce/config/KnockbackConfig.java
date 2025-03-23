package games.negative.lce.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.lce.struct.SpeedVector;
import games.negative.lce.struct.Vector;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;

import java.util.List;
import java.util.Map;

@Getter
@Configuration
public class KnockbackConfig {

    @Comment({
            "Whether or not to use LCE for handling custom knockback",
            "Can be disabled by setting to 'false' in case you have",
            "another plugin that handles custom knockback or you just want",
            "to use vanilla knockback."
    })
    private boolean enabled = true;

    @Comment({
            "",
            "A list of knockback modification values.",
            "All types:",
            "* DAMAGE | Knockback caused by non-entity damage.",
            "* ENTITY_ATTACK | Knockback caused by an attacking entity.",
            "* EXPLOSION | Knockback caused by an explosion.",
            "* SHIELD_BLOCK | Knockback caused by the target blocking with a shield.",
            "* SWEEP_ATTACK | Knockback caused by a sweeping attack.",
            "* PUSH | A generic push.",
            "* UNKNOWN | Knockback with an unknown cause."
    })
    private Map<EntityKnockbackEvent.Cause, Vector> knockback = Map.of(
            EntityKnockbackEvent.Cause.ENTITY_ATTACK, new Vector(1, 1, 1),
            EntityKnockbackEvent.Cause.EXPLOSION, new Vector(1, 1, 1)
    );

    @Comment({
            "",
            "A list of projectiles that should apply knockback.",
            "This is useful for projectiles that normally don't apply knockback"
    })
    private List<NamespacedKey> knockbackProjectiles = List.of(
            EntityType.SNOWBALL.getKey(),
            EntityType.EGG.getKey(),
            EntityType.ENDER_PEARL.getKey(),
            EntityType.FISHING_BOBBER.getKey()
    );

    @Comment({
            "",
            "The damage dealt by knockback projectiles.",
    })
    private double knockbackProjectileDamage = 0.01;

    @Comment({
            "",
            "The strength of the knockback applied by knockback projectiles.",
    })
    private double knockbackProjectileKnockbackStrength = 1;

    @Comment({
            "",
            "Whether or not to enable bow boosting.",
            "This is when you shoot an arrow and jump at the same time to get a boost."
    })
    private boolean enableBowBoost = false;

    @Comment({
            "",
            "The threshold for bow boosting. If the \"force\" of the arrow is less than this,",
            "the player will \"bow boost\" and different physics will be applied."
    })
    private float bowBoostThreshold = 0.4f;

    @Comment({
            "",
            "The speed of the bow boost.",
            "This is the speed at which the player is boosted when bow boosting."
    })
    private SpeedVector bowBoostSpeed = new SpeedVector(1, 1);

    public boolean isKnockbackProjectile(EntityType type) {
        return knockbackProjectiles.contains(type.getKey());
    }

    public void setKnockbackProfile(EntityKnockbackEvent.Cause cause, Vector vector) {
        knockback.put(cause, vector);
    }
}
