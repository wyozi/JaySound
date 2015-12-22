package com.wyozi.jaysound;

import com.wyozi.jaysound.efx.EffectZone;
import com.wyozi.jaysound.efx.effects.*;
import com.wyozi.jaysound.player.FFTVisualizer;
import com.wyozi.jaysound.sound.StreamingSound;
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

        AudioContext ss = new AudioContext();
        ss.updateListener(new ThrowawayVec3f(0, 0, 0), new ThrowawayVec3f(0, 0, -1), new ThrowawayVec3f(0, 0, 0));

        EffectZone zone = new EffectZone();
        zone.addEffect(new Flanger());
        ss.setGlobalEffectZone(zone);

        StreamingSound handle = ss.createStreamingSound(new URL("http://wyozi.party:8080/f/Bank%20Head%20-%20Kingdom%20ft.%20Kelela.mp3"));
        //StreamingSound handle = ss.createStreamingSound(Mp3DecoderTest.class.getResource("/higher.mp3"));
        handle.play();

        FFTVisualizer fftVisualizer = new FFTVisualizer();

        for (int i = 0;i < 50000; i++) {
            float x = (float) (Math.cos(i/30f)*1.5f);
            float z = (float) (Math.sin(i/30f)*1.5f);
            handle.setPos(new ThrowawayVec3f(x, 0, z));
            //ss.updateListener(new Vec3f(x, 0, z), new Vec3f(0, 0, 1), null);

            ss.update();
            handle.updateFft();
            fftVisualizer.updateFFT(handle.getFft());
            Thread.sleep(20);
        }

        handle.dispose();
        ss.dispose();
    }
}
