package games.negative.lce.listener.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerInput;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.Config;
import org.bukkit.entity.Player;

public class BridgingPacketListener implements PacketListener {

    private static final float DEFAULT_WALK_SPEED = 0.2f;

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon type = event.getPacketType();
        if (type != PacketType.Play.Client.PLAYER_INPUT) return;

        WrapperPlayClientPlayerInput packet = new WrapperPlayClientPlayerInput(event);

        Player player = event.getPlayer();

        if (packet.isShift() && packet.isBackward()) {
            player.setWalkSpeed(adjustments().getBridgingSpeed());
            return;
        }

        if (player.getWalkSpeed() == DEFAULT_WALK_SPEED) return;

        player.setWalkSpeed(DEFAULT_WALK_SPEED);
    }

    private Config.Adjustments adjustments() {
        return CombatPlugin.config().adjustments();
    }
}
