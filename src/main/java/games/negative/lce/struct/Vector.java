package games.negative.lce.struct;

public record Vector(double x, double y, double z) {

    public org.bukkit.util.Vector toBukkitVector() {
        return new org.bukkit.util.Vector(x, y, z);
    }

}
