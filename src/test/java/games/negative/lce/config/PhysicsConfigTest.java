package games.negative.lce.config;

import games.negative.lce.struct.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhysicsConfigTest {

    @Test
    void defaultsMatchLegacyCombatBehavior() {
        PhysicsConfig config = new PhysicsConfig();

        assertEquals(new Vector(1, 1, 1), config.getFishingRodVelocity());
        assertEquals(2.0, config.getDamageReductionWhileBlockingWithSword());
        assertTrue(config.isOldschoolAxeDamage());
        assertEquals(1.5, config.getCriticalModifier());
        assertFalse(config.isCancelCritIfSprinting());
        assertTrue(config.isOldSharpnessDamageBuff());
        assertFalse(config.isEnableBridgingPhysics());
        assertEquals(0.225f, config.getBridgingSpeed());
        assertFalse(config.isEnableLegacyPotionPhysics());
        assertEquals(2.0, config.getLegacyPotionVelocityReduction());
        assertTrue(config.isDisableOffhand());
    }
}
