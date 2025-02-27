package com.ericlmao.combat.listener.packet;

import com.ericlmao.combat.util.CombatCheck;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.bukkit.entity.Player;

import java.util.Set;

public class SoundPacketListener implements PacketListener {

    private static final Set<String> BLOCKED_SOUNDS = Set.of(
            "minecraft:entity.player.attack.nodamage",
            "minecraft:entity.player.attack.sweep",
            "minecraft:entity.player.attack.weak",
            "minecraft:entity.player.attack.crit",
            "minecraft:entity.player.attack.strong",
            "minecraft:entity.player.attack.knockback"
    );

    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Server.SOUND_EFFECT) return;

        WrapperPlayServerSoundEffect packet = new WrapperPlayServerSoundEffect(event);

        Sound sound = packet.getSound();
        if (!BLOCKED_SOUNDS.contains(sound.getSoundId().toString())) return;

        Player player = event.getPlayer();
        if (!CombatCheck.checkCombat(player)) return;

        event.setCancelled(true);
    }

}
