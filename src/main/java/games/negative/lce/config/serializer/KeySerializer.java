package games.negative.lce.config.serializer;

import de.exlll.configlib.Serializer;
import org.bukkit.NamespacedKey;

public class KeySerializer implements Serializer<NamespacedKey, String> {

    @Override
    public String serialize(NamespacedKey namespacedKey) {
        return namespacedKey.toString();
    }

    @Override
    public NamespacedKey deserialize(String s) {
        String[] split = s.split(":");
        return new NamespacedKey(split[0], split[1]);
    }
}
