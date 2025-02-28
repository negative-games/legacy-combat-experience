package games.negative.lce.config;

import com.github.retrooper.packetevents.protocol.sound.Sound;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.lce.struct.SoundRemap;
import games.negative.lce.struct.Vector;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

        @Comment({"", "The velocity of the fishing rod when thrown."})
        private Vector fishingRodVelocity = new Vector(0, 0, 0);

        @Comment({
                "",
                "A list of sounds that should be remapped to another sound.",
        })
        private Map<String, SoundRemap> soundRemap = Map.of(
            "minecraft:entity.fishing_bobber.throw", new SoundRemap(
                "minecraft:entity.egg.throw", 1f, 0.5f
                )
        );

        @Comment({
                "",
                "A list of sounds that should be blocked.",
        })
        private List<String> disabledSounds = List.of(
                "minecraft:entity.player.attack.nodamage",
                "minecraft:entity.player.attack.sweep",
                "minecraft:entity.player.attack.weak",
                "minecraft:entity.player.attack.crit",
                "minecraft:entity.player.attack.strong",
                "minecraft:entity.player.attack.knockback",
                "minecraft:entity.fishing_bobber.retrieve"
        );

        @Comment({
                "",
                "The speed of the player when bridging.",
                "Specifically Shift + Backward.",
        })
        private float bridgingSpeed = 0.225f;

        public boolean isRemappedSound(Sound sound) {
            return soundRemap.containsKey(sound.getSoundId().toString());
        }

        public SoundRemap getRemappedSound(Sound sound) {
            if (!soundRemap.containsKey(sound.getSoundId().toString())) return null;

            return soundRemap.get(sound.getSoundId().toString());
        }
    }
}
