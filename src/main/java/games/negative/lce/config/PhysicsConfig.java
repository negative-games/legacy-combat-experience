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
            "Whether or not to make axe damage what it was in 1.8."
    })
    private boolean oldschoolAxeDamage = true;

    @Comment({
            "",
            "The damage modifier for critical hits."
    })
    private double criticalModifier = 1.5;

    @Comment({
            "",
            "Whether or not players can deal crits while sprinting.",
            "In newer versions, sprinting players cannot deal crits."
    })
    private boolean cancelCritIfSprinting = false;

    @Comment({
            "",
            "Whether or not to make sharpness act the way it did in 1.8."
    })
    private boolean oldSharpnessDamageBuff = true;

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

    @Comment({
            "",
            "The amount to divide the velocity of a",
            "thrown potion by to make it more like 1.8",
            " ",
            "Functionality:",
            "velocity = direction * speed / 2 "
    })
    private double legacyPotionVelocityReduction = 2.0;

    @Comment({
            "",
            "Whether or not to disable the offhand slot"
    })
    private boolean disableOffhand = true;
}
