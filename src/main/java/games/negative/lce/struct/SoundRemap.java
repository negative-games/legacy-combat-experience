package games.negative.lce.struct;

import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;

public record SoundRemap(String sound, float volume, float pitch) {

    public Sound getSound() {
        return Sounds.getByName(sound);
    }
}
