package games.negative.lce.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import games.negative.lce.struct.Vector;
import lombok.Getter;

@Getter
@Configuration
public class PhysicsConfig {

    @Comment({"The velocity of the fishing rod when thrown."})
    private Vector fishingRodVelocity = new Vector(1, 1, 1);

    @Comment({
            "",
            "The divisible amount to reduce damage of an attack",
            "while the victim is blocking with a sword.",
            " ",
            "Example:",
            "\"damage-reduction-while-blocking-with-sword: 2\"",
            " ",
            "Functionality:",
            "(damage) / 2 = (new damage, but reduced by 50%)",
    })
    private double damageReductionWhileBlockingWithSword = 2;

    @Comment({
            "",
            "Whether or not to enable the custom bridging physics",
            "which increases speed when using Shift + Backwards"
    })
    private boolean enableBridgingPhysics = false;

    @Comment({
            "",
            "The speed of the player when bridging.",
            "Specifically Shift + Backward.",
    })
    private float bridgingSpeed = 0.225f;

    @Comment({
            "",
            "Whether or not to enable the legacy potion physics",
            "for throwing splash potions"
    })
    private boolean enableLegacyPotionPhysics = false;
}
