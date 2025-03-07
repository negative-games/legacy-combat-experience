package games.negative.lce.command;

import com.google.common.base.Preconditions;
import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.TabContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.alumina.util.NumberUtil;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.Config;
import games.negative.lce.message.Messages;
import games.negative.lce.struct.Vector;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CmdKnockback extends Command {

    public CmdKnockback() {
        super(CommandBuilder.builder()
                .name("knockback")
                .aliases("kb")
                .playerOnly(true)
                .smartTabComplete(true)
        );
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        Player player = context.player().orElseThrow();

        if (!adjustments().isUseCustomKnockback()) {
            Messages.KNOCKBACK_DISABLED.create().send(player);
            return;
        }

        Vector vector = knockbackValues();

        if (context.length() != 3) {
            Messages.KNOCKBACK_PROMPT.create()
                    .replace("%x%", NumberUtil.decimalFormat(vector.x()))
                    .replace("%y%", NumberUtil.decimalFormat(vector.y()))
                    .replace("%z%", NumberUtil.decimalFormat(vector.z()))
                    .send(player);
            return;
        }

        Double x = context.argument(0).map(NumberUtil::getDouble).orElse(null);
        Double y = context.argument(1).map(NumberUtil::getDouble).orElse(null);
        Double z = context.argument(2).map(NumberUtil::getDouble).orElse(null);

        if (x == null || y == null || z == null) {
            int index = (x == null ? 0 : (y == null ? 1 : 2));
            String input = context.argument(index).orElseThrow();

            Messages.KNOCKBACK_NUMBER_FORMAT_EXCEPTION.create()
                    .replace("%number%", input)
                    .send(player);
            return;
        }

        Vector updated = new Vector(x, y, z);
        adjustments().setKnockbackProfile(EntityKnockbackEvent.Cause.ENTITY_ATTACK, updated);
        save();

        Messages.KNOCKBACK_SUCCESS.create()
                .replace("%x%", NumberUtil.decimalFormat(updated.x()))
                .replace("%y%", NumberUtil.decimalFormat(updated.y()))
                .replace("%z%", NumberUtil.decimalFormat(updated.z()))
                .send(player);
    }

    @Override
    public List<String> onTabComplete(@NotNull TabContext context) {
        int index = context.index();

        Vector vector = knockbackValues();

        return switch (index) {
            case 1 -> List.of(NumberUtil.decimalFormat(vector.x()));
            case 2 -> List.of(NumberUtil.decimalFormat(vector.y()));
            case 3 -> List.of(NumberUtil.decimalFormat(vector.z()));
            default -> List.of();
        };

    }

    private Vector knockbackValues() {
        Vector vector = adjustments().getKnockback().getOrDefault(EntityKnockbackEvent.Cause.ENTITY_ATTACK, null);
        if (vector == null) {
            adjustments().setKnockbackProfile(EntityKnockbackEvent.Cause.ENTITY_ATTACK, new Vector(1, 1, 1));
            save();

            vector = adjustments().getKnockback().get(EntityKnockbackEvent.Cause.ENTITY_ATTACK);
            Preconditions.checkNotNull(vector, "Knockback vector for 'ENTITY_ATTACK' should not be null");
        }

        return vector;
    }

    private Config.Adjustments adjustments() {
        return CombatPlugin.config().adjustments();
    }

    private void save() {
        CombatPlugin.instance().saveConfiguration();
    }
}
