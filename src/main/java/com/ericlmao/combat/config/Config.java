package com.ericlmao.combat.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

@Getter
@Configuration
public class Config {

    @Comment({
            "A list of worlds the plugin should consider",
            "as 'legacy combat worlds' where legacy combat would",
            "be enabled, so 1.9 particles and sounds won't be played."
    })
    private List<String> legacyCombatWorlds = List.of("legacy_pvp_arenas");

    @Unmodifiable
    public List<World> combatWorlds() {
        return legacyCombatWorlds.stream()
                .filter(s -> Bukkit.getWorld(s) != null)
                .map(Bukkit::getWorld)
                .toList();
    }
}
