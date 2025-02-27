package games.negative.lce.util;

import games.negative.lce.CombatPlugin;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.UUID;

@UtilityClass
public class CombatCheck {

    // Cache the world a user is in for 1 minute
    private final Cache<UUID, String> worlds = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(1))
            .build();

    public boolean checkCombat(@NotNull Player player) {
        return (fetchWorld(player) != null || WGUtil.testCombatFlag(player));
    }

    public boolean checkCombat(Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        boolean match = CombatPlugin.config().combatWorlds().stream().anyMatch(world1 -> world1.getName().equalsIgnoreCase(world.getName()));
        return match || WGUtil.testCombatFlag(location);
    }

    @Nullable
    private static World fetchWorld(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        String world = worlds.getIfPresent(uuid);
        if (world != null && player.getWorld().getName().equalsIgnoreCase(world)) return player.getWorld();

        World fetchedWorld = CombatPlugin.config().combatWorlds().stream()
                .filter(world1 -> world1.getName().equalsIgnoreCase(player.getWorld().getName()))
                .findFirst()
                .orElse(null);

        if (fetchedWorld == null) {
            worlds.invalidate(uuid);
            return null;
        }

        worlds.put(uuid, fetchedWorld.getName());
        return fetchedWorld;
    }
}
