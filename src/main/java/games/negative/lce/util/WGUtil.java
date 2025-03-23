package games.negative.lce.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import games.negative.alumina.util.PluginUtil;
import games.negative.lce.CombatPlugin;
import games.negative.lce.flag.FlagHandler;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class WGUtil {

    public boolean testCombatFlag(@NotNull Player player) {
        if (!PluginUtil.hasPlugin("WorldGuard")) return false;

        Location location = BukkitAdapter.adapt(player.getLocation());

        FlagHandler handler = CombatPlugin.flagHandler().orElseThrow();

        StateFlag flag = handler.flag().orElse(null);
        if (flag == null) return false;

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return container.createQuery().testState(location, localPlayer, flag);
    }

    public boolean testCombatFlag(org.bukkit.Location location) {
        if (!PluginUtil.hasPlugin("WorldGuard")) return false;

        Location loc = BukkitAdapter.adapt(location);

        FlagHandler handler = CombatPlugin.flagHandler().orElseThrow();

        StateFlag flag = handler.flag().orElse(null);
        if (flag == null) return false;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return container.createQuery().testState(loc, null, flag);
    }

}
