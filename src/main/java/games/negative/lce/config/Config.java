package games.negative.lce.config;

import com.github.retrooper.packetevents.protocol.sound.Sound;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.lce.config.defaults.ConfigDefaults;
import games.negative.lce.struct.SoundRemap;
import games.negative.lce.struct.SpeedVector;
import games.negative.lce.struct.Vector;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

@Getter
@Configuration
public class Config {

    @Comment({
            "A list of worlds the plugin should consider",
            "as 'legacy combat worlds' where legacy combat would",
            "be enabled, so 1.9 particles and sounds won't be played."
    })
    private List<String> legacyCombatWorlds = List.of("legacy_pvp_arenas");

    @Comment({
            "",
            "Do not edit these settings unless you know what you're doing.",
    })
    private Adjustments adjustments = new Adjustments();

    public Adjustments adjustments() {
        return adjustments;
    }

    @Unmodifiable
    public List<World> combatWorlds() {
        return legacyCombatWorlds.stream()
                .filter(s -> Bukkit.getWorld(s) != null)
                .map(Bukkit::getWorld)
                .toList();
    }

    @Getter
    @Configuration
    public static class Adjustments {

        @Comment({
                "",
                "Whether or not to use LCE for handling custom knockback",
                "Can be disabled by setting to 'false' in case you have",
                "another plugin that handles custom knockback or you just want",
                "to use vanilla knockback."
        })
        private boolean useCustomKnockback = true;

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

        @Comment({"", "The velocity of the fishing rod when thrown."})
        private Vector fishingRodVelocity = new Vector(1, 1, 1);

        @Comment({
                "",
                "A list of sounds that should be remapped to another sound.",
        })
        private Map<String, SoundRemap> soundRemap = ConfigDefaults.defaultSoundRemap();

        @Comment({
                "",
                "A list of sounds that should be blocked.",
        })
        private List<NamespacedKey> disabledSounds = ConfigDefaults.defaultDisabledSounds();

        @Comment({
                "",
                "The speed of the player when bridging.",
                "Specifically Shift + Backward.",
        })
        private float bridgingSpeed = 0.225f;

        public NamespacedKey getBukkitSoundKey(Sound sound) {
            String id = sound.getSoundId().toString();
            String[] split = id.split(":");

            return new NamespacedKey(split[0], split[1]);
        }

        public boolean isRemappedSound(Sound sound) {
            NamespacedKey key = getBukkitSoundKey(sound);
            return soundRemap.containsKey(key.toString());
        }

        public SoundRemap getRemappedSound(Sound sound) {
            NamespacedKey key = getBukkitSoundKey(sound);
            if (!soundRemap.containsKey(key.toString())) return null;

            return soundRemap.get(key.toString());
        }

        public boolean isKnockbackProjectile(EntityType type) {
            return knockbackProjectiles.contains(type.getKey());
        }

        public void setKnockbackProfile(EntityKnockbackEvent.Cause cause, Vector vector) {
            knockback.put(cause, vector);
        }
    }
}
