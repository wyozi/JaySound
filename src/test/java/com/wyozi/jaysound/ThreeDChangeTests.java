package com.wyozi.jaysound;

import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.efx.effects.Reverb;
import com.wyozi.jaysound.efx.filters.LowPass;
import com.wyozi.jaysound.sound.StreamingSound;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 25.12.2015.
 */
public class ThreeDChangeTests {
    public static void main(String[] args) throws IOException, InterruptedException {
        Configurator.defaultConfig()
                .formatPattern("{level}: {class}.{method}()\\t{message}")
                .level(org.pmw.tinylog.Level.DEBUG)
                .activate();

        AudioContext ctx = new AudioContext();

        SoundEnvironment outsideCarEnv = new SoundEnvironment();
        outsideCarEnv.addEffect(Reverb.getBestAvailableReverb(Reverb.EFXPreset.PARKINGLOT));
        ctx.setGlobalSoundEnvironment(outsideCarEnv);

        StreamingSound sound = ctx.createStreamingSound(new URL("http://stream.plusfm.net/;"));
        sound.play();

        for (int i = 0;i < 1000; i++) {
            if (i % 5 == 0) {
                boolean b = i % 10 == 0;

                if (b) {
                    System.out.println("Entering car");

                    sound.setDirectFilter(null);
                    ctx.setGlobalSoundEnvironment(null);

                    sound.disable3D();
                } else {
                    System.out.println("Exiting car");

                    sound.setDirectFilter(new LowPass(LowPass.Preset.CarStereo));
                    ctx.setGlobalSoundEnvironment(outsideCarEnv);

                    sound.setPosition(1f, 0f, 0f);
                }
            }

            ctx.update();
            Thread.sleep(1000);
        }

        ctx.dispose();
    }
}
