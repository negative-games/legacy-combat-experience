package games.negative.lce.config;

import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.alumina.logger.Logs;
import games.negative.lce.config.defaults.ConfigDefaults;
import lombok.Getter;
import org.bukkit.NamespacedKey;

import java.time.Duration;
import java.util.List;

@Getter
@Configuration
public class ParticleConfig {

    public static final Cache<ParticleType<?>, NamespacedKey> BUKKIT_KEY_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();

    @Comment({
            "",
            "A list of sounds that should be blocked.",
    })
    private List<NamespacedKey> disabledParticles = ConfigDefaults.defaultDisabledParticles();

    public NamespacedKey getBukkitParticleKey(ParticleType<?> particle) {
        NamespacedKey existing = BUKKIT_KEY_CACHE.getIfPresent(particle);
        if (existing != null) return existing;

        String id = particle.getName().toString();
        Logs.info("Converting particle type " + id + " to NamespacedKey");
        String[] split = id.split(":");

        NamespacedKey key = new NamespacedKey(split[0], split[1]);

        BUKKIT_KEY_CACHE.put(particle, key);

        return key;
    }

    public boolean isDisabledParticle(ParticleType<?> particle) {
        NamespacedKey key = getBukkitParticleKey(particle);
        return disabledParticles.contains(key);
    }
}
