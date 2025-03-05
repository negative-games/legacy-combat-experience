package games.negative.lce.config.defaults;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import games.negative.lce.struct.SoundRemap;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class ConfigDefaults {

    public List<NamespacedKey> defaultDisabledSounds() {
        List<NamespacedKey> list = Lists.newArrayList();

        Registry<Sound> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT);

        list.add(registry.getKey(Sound.ENTITY_PLAYER_ATTACK_NODAMAGE));
        list.add(registry.getKey(Sound.ENTITY_PLAYER_ATTACK_SWEEP));
        list.add(registry.getKey(Sound.ENTITY_PLAYER_ATTACK_WEAK));
        list.add(registry.getKey(Sound.ENTITY_PLAYER_ATTACK_CRIT));
        list.add(registry.getKey(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK));
        list.add(registry.getKey(Sound.ENTITY_FISHING_BOBBER_RETRIEVE));

        return list;
    }

    public Map<String, SoundRemap> defaultSoundRemap() {
        Map<String, SoundRemap> map = Maps.newHashMap();

        Registry<Sound> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.SOUND_EVENT);

        map.put(Objects.requireNonNull(registry.getKey(Sound.ENTITY_FISHING_BOBBER_THROW)).toString(), new SoundRemap(
                registry.getKey(Sound.ENTITY_EGG_THROW),
                1f,
                0.5f
        ));

        return map;
    }
}
