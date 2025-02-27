package com.ericlmao.combat.flag;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import games.negative.alumina.logger.Logs;
import org.jetbrains.annotations.CheckReturnValue;

import java.util.Optional;

public class FlagHandler {

    private static final String FLAG_ID = "legacy-combat";

    private StateFlag flag;

    public FlagHandler() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            StateFlag flag = new StateFlag(FLAG_ID, false);
            registry.register(flag);
            this.flag = flag;

            Logs.custom("<green>WorldGuard integration success! You can use the <yellow>legacy-combat</yellow> flag in your regions.");
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get(FLAG_ID);
            if (existing instanceof StateFlag) {
                flag = (StateFlag) existing;
                Logs.custom("<green>WorldGuard integration success! You can use the <yellow>legacy-combat</yellow> flag in your regions.");
            } else {
                Logs.error("Another plugin registered a WorldGuard flag with the name 'legacy-combat' that is not a StateFlag. The WorldGuard integration will not work for this plugin.");
            }
        }
    }

    @CheckReturnValue
    public Optional<StateFlag> flag() {
        return Optional.ofNullable(flag);
    }
}
