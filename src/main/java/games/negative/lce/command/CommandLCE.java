package games.negative.lce.command;

import games.negative.lce.CombatPlugin;
import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandLCE extends Command {

    public CommandLCE() {
        super(CommandBuilder.builder().name("legacycombatexperience")
                .aliases("lce")
                .permission("lce.command")
                .smartTabComplete(true)
        );

        injectSubCommand(CommandBuilder.builder().name("reload"), context -> {
            CommandSender sender = context.sender();

            CombatPlugin.instance().reload();

            sender.sendRichMessage("<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <gray>The configurations has been reloaded</gray>");
        });

        injectSubCommand(CommandBuilder.builder().name("sound").playerOnly(true).parameter("sound", context -> {
            Registry<@NotNull Sound> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT);
            return registry.stream().map(registry::getKey).filter(Objects::nonNull).map(NamespacedKey::toString).toList();
        }), context -> {
            Player player = context.player().orElseThrow();

            String raw = context.argument(0).orElseThrow();
            if (!raw.contains(":")) {
                player.sendRichMessage("<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <red>Invalid sound</red>");
                return;
            }

            String[] split = raw.split(":");
            if (split.length != 2) {
                player.sendRichMessage("<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <red>Invalid sound</red>");
                return;
            }

            String namespace = split[0];
            String path = split[1];

            NamespacedKey key = new NamespacedKey(namespace, path);

            Sound sound = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(key);
            if (sound == null) {
                player.sendRichMessage("<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <red>Invalid sound</red>");
                return;
            }

            float volume = context.argument(1).map(Float::parseFloat).orElse(1f);
            float pitch = context.argument(2).map(Float::parseFloat).orElse(1f);

            player.playSound(player, sound, volume, pitch);

            player.sendRichMessage("<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <gray>Played sound <white>" + raw + "</white></gray>");
        });
    }

    @Override
    public void execute(@NotNull CommandContext context) {

    }
}
