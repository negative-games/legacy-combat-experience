package com.ericlmao.combat.listener.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import org.bukkit.entity.Player;

public class BridgingPacketListener implements PacketListener {

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Client.PLAYER_INPUT) return;

        WrapperPlayClientPlayerInput packet = new WrapperPlayClientPlayerInput(event);

        Player player = event.getPlayer();
        if (packet.isShift() && packet.isBackward()) {
            player.setWalkSpeed(0.25f);
        } else {
            player.setWalkSpeed(0.2f);
        }


    }
}
