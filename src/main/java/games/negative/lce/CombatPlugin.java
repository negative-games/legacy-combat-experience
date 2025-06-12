package games.negative.lce;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import games.negative.alumina.AluminaPlugin;
import games.negative.alumina.util.PluginUtil;
import games.negative.lce.command.CommandGiveBlockHittingSword;
import games.negative.lce.command.CommandLCE;
import games.negative.lce.config.ConfigManager;
import games.negative.lce.flag.FlagHandler;
import games.negative.lce.listener.LegacyPhysicsListener;
import games.negative.lce.listener.packet.BlockingPacketListener;
import games.negative.lce.listener.packet.BridgingPacketListener;
import games.negative.lce.listener.packet.ParticlePacketListener;
import games.negative.lce.listener.packet.SoundPacketListener;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class CombatPlugin extends AluminaPlugin {

    private static CombatPlugin instance;

    public static NamespacedKey LEGACY_COMBAT_SPEED;
    public static NamespacedKey LEGACY_WEAPON;

    private ConfigManager configurations;

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
        events.registerListener(new ParticlePacketListener(), PacketListenerPriority.NORMAL);
        events.registerListener(new BridgingPacketListener(), PacketListenerPriority.NORMAL);
        events.registerListener(new BlockingPacketListener(), PacketListenerPriority.NORMAL);

        if (PluginUtil.hasPlugin("WorldGuard")) handler = new FlagHandler();

    }

    @Override
    public void enable() {
        PacketEvents.getAPI().init();

        configurations = new ConfigManager(this);

        registerCommand(new CommandGiveBlockHittingSword());
        registerCommand(new CommandLCE());
        registerListener(new LegacyPhysicsListener());
        registerListener(new PlayerJoinListener());
    }

    public void reload() {
        configurations.reload();
    }

    @Override
    public void disable() {
        PacketEvents.getAPI().terminate();
    }

    @NotNull
    public ConfigManager getConfigurations() {
        return configurations;
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
    public static ConfigManager configs() {
        return instance().getConfigurations();
    }

    @CheckReturnValue
    public static Optional<FlagHandler> flagHandler() {
        return instance().getFlagHandler();
    }

}
