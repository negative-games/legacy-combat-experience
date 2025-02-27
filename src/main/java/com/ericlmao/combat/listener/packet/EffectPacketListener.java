package com.ericlmao.combat.listener.packet;

import com.ericlmao.combat.util.CombatCheck;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import org.bukkit.entity.Player;

public class EffectPacketListener implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Server.PARTICLE) return;

        WrapperPlayServerParticle packet = new WrapperPlayServerParticle(event);
        Particle<?> particle = packet.getParticle();

        if (!particle.getType().equals(ParticleTypes.SWEEP_ATTACK)) return;

        Player player = event.getPlayer();
        if (!CombatCheck.checkCombat(player)) return;

        event.setCancelled(true);
    }
}
