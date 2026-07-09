package games.negative.lce.struct;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VectorTest {

    @Test
    void convertsToBukkitVectorWithoutChangingComponents() {
        Vector vector = new Vector(1.25, -0.5, 3.75);

        org.bukkit.util.Vector bukkit = vector.toBukkitVector();

        assertEquals(1.25, bukkit.getX());
        assertEquals(-0.5, bukkit.getY());
        assertEquals(3.75, bukkit.getZ());
    }
}
