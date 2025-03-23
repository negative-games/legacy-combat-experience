package games.negative.lce.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class LogConfig {

    @Comment({
            "Logs all incoming sound packets into console.",
            "Generally useful for debugging and knowing what sounds are being sent",
            "during testing and development to determine if they should be blocked.",
            " ",
            "Example: [LCE] Incoming sound: minecraft:entity.player.attack.crit"
    })
    private boolean logIncomingSoundPackets = false;

    @Comment({
            "",
            "Logs all incoming particle packets into console.",
            "Generally useful for debugging and knowing what particles are being sent",
            "during testing and development to determine if they should be blocked.",
            " ",
            "Example: [LCE] Incoming particle: minecraft:sweep_attack"
    })
    private boolean logIncomingParticlePackets = false;

}
