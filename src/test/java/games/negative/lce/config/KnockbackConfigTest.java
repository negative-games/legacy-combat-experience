package games.negative.lce.config;

import games.negative.lce.struct.SpeedVector;
import games.negative.lce.struct.Vector;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KnockbackConfigTest {

    @Test
    void defaultsIncludeLegacyCombatProjectiles() {
        KnockbackConfig config = new KnockbackConfig();

        assertTrue(config.isKnockbackProjectile(EntityType.SNOWBALL));
        assertTrue(config.isKnockbackProjectile(EntityType.EGG));
        assertTrue(config.isKnockbackProjectile(EntityType.ENDER_PEARL));
        assertTrue(config.isKnockbackProjectile(EntityType.FISHING_BOBBER));
        assertFalse(config.isKnockbackProjectile(EntityType.ARROW));
    }

    @Test
    void defaultBowBoostSpeedUsesUnitVector() {
        KnockbackConfig config = new KnockbackConfig();

        assertEquals(new SpeedVector(1, 1), config.getBowBoostSpeed());
    }

    @Test
    void knockbackProfileCanBeUpdated() {
        KnockbackConfig config = new KnockbackConfig();
        Vector updated = new Vector(0.8, 0.4, 0.8);

        config.setKnockbackProfile(EntityKnockbackEvent.Cause.ENTITY_ATTACK, updated);

        assertEquals(updated, config.getKnockback().get(EntityKnockbackEvent.Cause.ENTITY_ATTACK));
    }
}
