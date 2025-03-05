package games.negative.lce;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import de.exlll.configlib.NameFormatters;
import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.config.Configuration;
import games.negative.alumina.util.PluginUtil;
import games.negative.lce.command.CommandGiveBlockHittingSword;
import games.negative.lce.command.CommandLCE;
import games.negative.lce.config.Config;
import games.negative.lce.config.serializer.KeySerializer;
import games.negative.lce.flag.FlagHandler;
import games.negative.lce.listener.LegacyPhysicsListener;
import games.negative.lce.listener.packet.BridgingPacketListener;
import games.negative.lce.listener.packet.EffectPacketListener;
import games.negative.lce.listener.packet.SoundPacketListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

public final class CombatPlugin extends AluminaPlugin {

    private static CombatPlugin instance;

    public static NamespacedKey LEGACY_COMBAT_SPEED;
    public static NamespacedKey LEGACY_WEAPON;

    private Configuration<Config> configuration;

    private FlagHandler handler = null;

    @Override
    public void load() {
        instance = this;

        LEGACY_WEAPON = new NamespacedKey(this, "legacy_weapon");
        LEGACY_COMBAT_SPEED = new NamespacedKey(this, "legacy_combat_speed");

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();

        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(new SoundPacketListener(), PacketListenerPriority.NORMAL);
        events.registerListener(new EffectPacketListener(), PacketListenerPriority.NORMAL);
        events.registerListener(new BridgingPacketListener(), PacketListenerPriority.NORMAL);

        if (PluginUtil.hasPlugin("WorldGuard")) handler = new FlagHandler();

    }

    @Override
    public void enable() {
        PacketEvents.getAPI().init();

        reload();

        registerCommand(new CommandGiveBlockHittingSword());
        registerCommand(new CommandLCE());
        registerListener(new LegacyPhysicsListener());
    }

    public void reload() {
        initConfig();
    }

    /**
     * Initializes the configuration for the plugin.
     * This method sets up the YamlConfigurationStore and updates the configuration from the "main.yml" file.
     * If the store is not null, it creates a new YamlConfigurationStore with the specified properties.
     * The configuration variable is then updated from the file using the store.
     */
    public void initConfig() {
        File file = new File(getDataFolder(), "main.yml");

        if (configuration != null) {
            configuration.reload();
            return;
        }

        configuration = Configuration.config(file, Config.class, builder -> {
            builder.setNameFormatter(NameFormatters.LOWER_KEBAB_CASE);
            builder.inputNulls(true);
            builder.outputNulls(true);

            builder.addSerializer(NamespacedKey.class, new KeySerializer());

            builder.header("""
                    --------------------------------------------------------
                    Legacy-Combat-Experience Configuration
                    \s
                    Useful Resources:
                    - Discord: https://discord.negative.games/
                    - GitHub: https://github.com/negative-games/legacy-combat-experience
                    - Issue Tracker: https://github.com/negative-games/legacy-combat-experience/issues
                    - Modrinth: https://modrinth.com/project/lce
                    --------------------------------------------------------
                    """);

            builder.footer("""
                    Authors: ericlmao
                    """);

            return builder;
        });
    }

    public void saveConfiguration() {
        configuration.save();
    }

    @Override
    public void disable() {
        PacketEvents.getAPI().terminate();
    }

    @NotNull
    public Config getConfiguration() {
        return configuration.get();
    }

    @CheckReturnValue
    public Optional<FlagHandler> getFlagHandler() {
        return Optional.ofNullable(handler);
    }

    @NotNull
    public static CombatPlugin instance() {
        return instance;
    }

    @NotNull
    public static Config config() {
        return instance().getConfiguration();
    }

    @CheckReturnValue
    public static Optional<FlagHandler> flagHandler() {
        return instance().getFlagHandler();
    }

}
