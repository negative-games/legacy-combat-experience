package games.negative.lce.listener.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.ParticleConfig;
import games.negative.lce.util.CombatCheck;
import games.negative.lce.util.LogUtil;
import org.bukkit.entity.Player;

public class ParticlePacketListener implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Server.PARTICLE) return;

        WrapperPlayServerParticle packet = new WrapperPlayServerParticle(event);
        Particle<?> particle = packet.getParticle();

        LogUtil.logIncomingParticle(particle.getType());

        ParticleType<?> particleType = particle.getType();
        if (!particles().isDisabledParticle(particleType)) return;

        Player player = event.getPlayer();
        if (!CombatCheck.checkCombat(player)) return;

        event.setCancelled(true);
    }

    public ParticleConfig particles() {
        return CombatPlugin.configs().particles();
    }
}
