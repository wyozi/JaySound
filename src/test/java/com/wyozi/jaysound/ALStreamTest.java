package com.wyozi.jaysound;

import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.efx.effects.*;
import com.wyozi.jaysound.efx.filters.LowPass;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.sound.StreamingSound;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 30.11.2015.
 */
public class ALStreamTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        Configurator.defaultConfig()
                .formatPattern("{level}: {class}.{method}()\\t{message}")
                .level(org.pmw.tinylog.Level.DEBUG)
                .activate();

        AudioContext ctx = new AudioContext();

        SoundEnvironment zone = new SoundEnvironment();
        zone.addEffect(Reverb.getBestAvailableReverb(Reverb.EFXPreset.ICEPALACE_HALL));
        ctx.setGlobalSoundEnvironment(zone);

        StreamingSound sound = ctx.createStreamingSound(new URL("http://stream.plusfm.net/;"));
        sound.play();

        for (int i = 0;i < 1000; i++) {
            ctx.update();
            System.out.println(sound.getDecodedTitle());
            Thread.sleep(1000);
        }

        ctx.dispose();
    }
}
