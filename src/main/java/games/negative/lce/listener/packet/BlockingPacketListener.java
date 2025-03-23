package games.negative.lce.listener.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.google.common.collect.Sets;
import games.negative.lce.util.CombatCheck;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

public class BlockingPacketListener implements PacketListener {

    public static final Set<UUID> BLOCKING_WITH_SWORD = Sets.newConcurrentHashSet();

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon type = event.getPacketType();

        Player player = event.getPlayer();
        if (player != null && !CombatCheck.checkCombat(player)) return;

        handleStartBlocking(type, event);
        handleStopBlocking(type, event);
    }

    private void handleStartBlocking(PacketTypeCommon type, PacketReceiveEvent event) {
        if (type != PacketType.Play.Client.USE_ITEM) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().name().contains("SWORD") || !item.hasData(DataComponentTypes.CONSUMABLE)) return;

        BLOCKING_WITH_SWORD.add(player.getUniqueId());
    }

    private void handleStopBlocking(PacketTypeCommon type, PacketReceiveEvent event) {
        if (type != PacketType.Play.Client.PLAYER_DIGGING) return;

        WrapperPlayClientPlayerDigging packet = new WrapperPlayClientPlayerDigging(event);

        if (packet.getAction() != DiggingAction.RELEASE_USE_ITEM) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().name().contains("SWORD") || !item.hasData(DataComponentTypes.CONSUMABLE)) return;

        BLOCKING_WITH_SWORD.remove(player.getUniqueId());
    }

}
