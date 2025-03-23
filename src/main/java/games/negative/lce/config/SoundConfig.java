package games.negative.lce.config;

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.lce.config.defaults.ConfigDefaults;
import games.negative.lce.struct.SoundRemap;
import lombok.Getter;
import org.bukkit.NamespacedKey;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Getter
@Configuration
public class SoundConfig {

    public static final Cache<Sound, NamespacedKey> BUKKIT_KEY_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

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

    public NamespacedKey getBukkitSoundKey(Sound sound) {
        NamespacedKey existing = BUKKIT_KEY_CACHE.getIfPresent(sound);
        if (existing != null) return existing;

        String id = sound.getSoundId().toString();
        String[] split = id.split(":");

        NamespacedKey key = new NamespacedKey(split[0], split[1]);
        BUKKIT_KEY_CACHE.put(sound, key);

        return key;
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

    public boolean isDisabledSound(Sound sound) {
        NamespacedKey key = getBukkitSoundKey(sound);
        return disabledSounds.contains(key);
    }

}
