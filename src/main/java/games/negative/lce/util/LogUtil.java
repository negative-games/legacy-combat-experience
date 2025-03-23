package games.negative.lce.util;

import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import games.negative.alumina.logger.Logs;
import games.negative.lce.CombatPlugin;
import games.negative.lce.config.LogConfig;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogUtil {

    public void logIncomingSound(Sound sound) {
        if (!config().isLogIncomingSoundPackets()) return;

        Logs.info("Incoming sound: " + sound.getSoundId().toString());
    }

    public void logIncomingParticle(ParticleType<?> particle) {
        if (!config().isLogIncomingParticlePackets()) return;

        Logs.info("Incoming particle: " + particle.getName().toString());
    }

    private LogConfig config() {
        return CombatPlugin.configs().logs();
    }
}
