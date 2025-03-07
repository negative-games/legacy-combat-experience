package games.negative.lce.message;

import games.negative.alumina.message.Message;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Messages {

    public Message KNOCKBACK_DISABLED = new Message(
            "<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <red>Custom knockback handling is disabled.</red>"
    );

    public Message KNOCKBACK_PROMPT = new Message(
            "<dark_gray><st>----------------------------------------</st></dark_gray>",
            "<color:#37abad><b>Knockback Values</b></color>",
            " ",
            "<aqua>X:</aqua> <white>%x%</white>",
            "<aqua>Y:</aqua> <white>%y%</white>",
            "<aqua>Z:</aqua> <white>%z%</white><aqua></aqua>",
            "<dark_gray><st>----------------------------------------</st></dark_gray>"
    );

    public Message KNOCKBACK_NUMBER_FORMAT_EXCEPTION = new Message(
            "<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <red>Invalid number \"<yellow>%number%</yellow>\".</red>"
    );

    public Message KNOCKBACK_SUCCESS = new Message(
            "<dark_gray><st>----------------------------------------</st></dark_gray>",
            "<dark_gray>[<aqua>LCE</aqua>]</dark_gray> <green>Knockback values set to:</green>",
            " ",
            "<aqua>X:</aqua> <white>%x%</white>",
            "<aqua>Y:</aqua> <white>%y%</white>",
            "<aqua>Z:</aqua> <white>%z%</white><aqua></aqua>",
            "<dark_gray><st>----------------------------------------</st></dark_gray>"
    );
}
