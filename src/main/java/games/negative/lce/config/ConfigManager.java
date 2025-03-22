package games.negative.lce.config;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import games.negative.alumina.config.Configuration;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.serializer.KeySerializer;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Function;

public class ConfigManager {

    private final Configuration<LogConfig> logConfig;
    private final Configuration<WorldConfig> worldConfig;
    private final Configuration<SoundConfig> soundConfig;
    private final Configuration<ParticleConfig> particleConfig;
    private final Configuration<PhysicsConfig> physicsConfig;
    private final Configuration<KnockbackConfig> knockbackConfig;

    public ConfigManager(@NotNull CombatPlugin plugin) {
        var dir = plugin.getDataFolder();

        this.logConfig = Configuration.config(new File(dir, "debug.yml"), LogConfig.class, builder("""
                --------------------------------------------------------
                    Legacy-Combat-Experience Debug/Logging Config
                    \s
                    !WARNING!: You never want to simply leave these
                    settings on, as they will cause a lot of
                    spam in your console. This is only for debugging
                    purposes, and should be turned off
                    when you are done testing.
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                """));

        this.worldConfig = Configuration.config(new File(dir, "worlds.yml"), WorldConfig.class, builder("""
                --------------------------------------------------------
                    Legacy-Combat-Experience Worlds Config
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                """));

        this.soundConfig = Configuration.config(new File(dir, "sounds.yml"), SoundConfig.class, builder("""
                --------------------------------------------------------
                    Legacy-Combat-Experience Sounds Config
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                """));

        this.particleConfig = Configuration.config(new File(dir, "particles.yml"), ParticleConfig.class, builder("""
                --------------------------------------------------------
                    Legacy-Combat-Experience Particle Config
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                """));

        this.physicsConfig = Configuration.config(new File(dir, "physics.yml"), PhysicsConfig.class, builder("""
                --------------------------------------------------------
                    Legacy-Combat-Experience Physics Config
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                """));

        this.knockbackConfig = Configuration.config(new File(dir, "knockback.yml"), KnockbackConfig.class, builder("""
                --------------------------------------------------------
                    Legacy-Combat-Experience Knockback Config
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                """));
    }

    private Function<YamlConfigurationProperties.Builder<?>, YamlConfigurationProperties.Builder<?>> builder(String header) {
        return builder -> {
            builder.setNameFormatter(NameFormatters.LOWER_KEBAB_CASE);
            builder.inputNulls(true);
            builder.outputNulls(true);

            builder.addSerializer(NamespacedKey.class, new KeySerializer());

            builder.header(header);

            builder.footer("""
                    Authors: ericlmao
                    """);

            return builder;
        };
    }

    public void reload() {
        worldConfig.reload();
        soundConfig.reload();
        particleConfig.reload();
        physicsConfig.reload();
        knockbackConfig.reload();
        logConfig.reload();
    }

    public void save() {
        worldConfig.save();
        soundConfig.save();
        particleConfig.save();
        physicsConfig.save();
        knockbackConfig.save();
    }

    public @NotNull WorldConfig world() {
        return worldConfig.get();
    }

    public @NotNull SoundConfig sound() {
        return soundConfig.get();
    }

    public @NotNull PhysicsConfig physics() {
        return physicsConfig.get();
    }

    public @NotNull KnockbackConfig knockback() {
        return knockbackConfig.get();
    }

    public @NotNull ParticleConfig particles() {
        return particleConfig.get();
    }

    public @NotNull LogConfig logs() {
        return logConfig.get();
    }

}
