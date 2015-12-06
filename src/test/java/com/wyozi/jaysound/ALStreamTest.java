package com.wyozi.jaysound;

import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.player.FFTVisualizer;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.sound.StreamingSound;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 30.11.2015.
 */
public class ALStreamTest {
    private static class ThrowawayVec3f implements JayVec3f {

        private final float x, y, z;

        private ThrowawayVec3f(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public float getJayX() {
            return x;
        }

        @Override
        public float getJayY() {
            return y;
        }

        @Override
        public float getJayZ() {
            return z;
        }
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        Configurator.defaultConfig()
                .formatPattern("{level}: {class}.{method}()\\t{message}")
                .level(org.pmw.tinylog.Level.DEBUG)
                .activate();

        Context ss = new Context();
        ss.updateListener(new ThrowawayVec3f(0, 0, 0), new ThrowawayVec3f(0, 0, -1), new ThrowawayVec3f(0, 0, 0));

        //StreamingSound handle = ss.createStreamingSound(new URL("http://streaming.radionomy.com/DRIVE"));
        StreamingSound handle = ss.createStreamingSound(Mp3DecoderTest.class.getResource("/higher.mp3"));
        handle.play();

        FFTVisualizer fftVisualizer = new FFTVisualizer();

        for (int i = 0;i < 50000; i++) {
            float x = 3f, z = 5f;
            //float x = (float) (Math.cos(i/30f)*1.5f);
            //float z = (float) (Math.sin(i/30f)*1.5f);
            handle.setPos(new ThrowawayVec3f(x, 0, z));
            //ss.updateListener(new Vec3f(x, 0, z), new Vec3f(0, 0, 1), null);

            ss.update();
            handle.fft();
            fftVisualizer.updateFFT(handle.getFft());
            Thread.sleep(20);
        }

        handle.dispose();
        ss.dispose();
    }
}
