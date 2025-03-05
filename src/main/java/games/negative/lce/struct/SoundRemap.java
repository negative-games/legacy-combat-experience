package games.negative.lce.struct;

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import org.bukkit.NamespacedKey;

public record SoundRemap(NamespacedKey sound, float volume, float pitch) {

    public Sound getSound() {
        return Sounds.getByName(sound.toString());
    }
}
