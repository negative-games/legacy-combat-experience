package com.ericlmao.combat.command;

import com.ericlmao.combat.CombatPlugin;
import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandRealLegacyCombat extends Command {

    public CommandRealLegacyCombat() {
        super(CommandBuilder.builder().name("reallegacycombat")
                .aliases("rlc")
                .permission("reallegacycombat.command")
                .smartTabComplete(true)
        );

        injectSubCommand(CommandBuilder.builder().name("reload"), context -> {
            CommandSender sender = context.sender();

            CombatPlugin.instance().reload();

            sender.sendRichMessage("<dark_gray>[<aqua>RLC</aqua>]</dark_gray> <gray>The configurations has been reloaded</gray>");
        });
    }

    @Override
    public void execute(@NotNull CommandContext context) {

    }
}
