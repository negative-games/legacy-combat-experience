package games.negative.lce.command;

import games.negative.alumina.command.Command;
import games.negative.alumina.command.CommandContext;
import games.negative.alumina.command.builder.CommandBuilder;
import games.negative.lce.CombatPlugin;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CommandGiveBlockHittingSword extends Command {

    public CommandGiveBlockHittingSword() {
        super(CommandBuilder.builder()
                .name("giveblockhittingsword")
                .playerOnly(true)
                .permission("combat.command.giveblockhittingsword")
        );
    }

    @Override
    public void execute(@NotNull CommandContext context) {
        ItemStack item = ItemStack.of(Material.DIAMOND_SWORD);
        item.setData(DataComponentTypes.CONSUMABLE, (DataComponentBuilder<Consumable>) () -> Consumable.consumable()
                .hasConsumeParticles(false)
                .consumeSeconds(Float.MAX_VALUE)
                .animation(ItemUseAnimation.BLOCK)
                .build());

        item.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(CombatPlugin.LEGACY_WEAPON, PersistentDataType.BOOLEAN, true);

            meta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(
                    CombatPlugin.LEGACY_COMBAT_SPEED, 20, AttributeModifier.Operation.ADD_NUMBER
            ));

            Material.DIAMOND_SWORD.getDefaultAttributeModifiers().forEach(meta::addAttributeModifier);
        });

        Player player = context.player().orElseThrow();
        player.getInventory().addItem(item);

        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
    }
}
