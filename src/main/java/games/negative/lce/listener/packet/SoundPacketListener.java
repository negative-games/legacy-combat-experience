package games.negative.lce.listener.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.SoundConfig;
import games.negative.lce.struct.SoundRemap;
import games.negative.lce.util.CombatCheck;
import games.negative.lce.util.LogUtil;
import org.bukkit.entity.Player;

public class SoundPacketListener implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Server.SOUND_EFFECT) return;

        WrapperPlayServerSoundEffect packet = new WrapperPlayServerSoundEffect(event);

        Sound sound = packet.getSound();
        LogUtil.logIncomingSound(sound);

        Player player = event.getPlayer();
        if (!CombatCheck.checkCombat(player)) return;

        if (sounds().isRemappedSound(sound)) {
            SoundRemap mapped = sounds().getRemappedSound(sound);
            if (mapped == null) return;

            packet.setSound(mapped.getSound());
            packet.setVolume(mapped.volume());
            packet.setPitch(mapped.pitch());
            return;
        }

        if (!sounds().isDisabledSound(sound)) return;

        event.setCancelled(true);
    }

    public SoundConfig sounds() {
        return CombatPlugin.configs().sound();
    }
}
