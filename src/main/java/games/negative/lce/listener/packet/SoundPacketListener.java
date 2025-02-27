package games.negative.lce.listener.packet;

import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import games.negative.alumina.logger.Logs;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.Config;
import games.negative.lce.struct.SoundRemap;
import games.negative.lce.util.CombatCheck;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SoundPacketListener implements PacketListener {

    private static final Set<String> BLOCKED_SOUNDS = Set.of(
            "minecraft:entity.player.attack.nodamage",
            "minecraft:entity.player.attack.sweep",
            "minecraft:entity.player.attack.weak",
            "minecraft:entity.player.attack.crit",
            "minecraft:entity.player.attack.strong",
            "minecraft:entity.player.attack.knockback",
            "minecraft:entity.fishing_bobber.retrieve"
    );

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Server.SOUND_EFFECT) return;

        WrapperPlayServerSoundEffect packet = new WrapperPlayServerSoundEffect(event);

        Player player = event.getPlayer();
        if (!CombatCheck.checkCombat(player)) return;

        Sound sound = packet.getSound();

        if (adjustments().isRemappedSound(sound)) {
            SoundRemap mapped = adjustments().getRemappedSound(sound);
            if (mapped == null) return;

            packet.setSound(mapped.getSound());
            packet.setVolume(mapped.volume());
            packet.setPitch(mapped.pitch());
            return;
        }

        if (!BLOCKED_SOUNDS.contains(sound.getSoundId().toString())) return;

        event.setCancelled(true);
    }

    public Config.Adjustments adjustments() {
        return CombatPlugin.config().adjustments();
    }
}
