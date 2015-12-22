package com.wyozi.jaysound;

import com.wyozi.jaysound.buffer.StreamBuffer;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.efx.EffectZone;
import com.wyozi.jaysound.efx.effects.Echo;
import com.wyozi.jaysound.efx.effects.Flanger;
import com.wyozi.jaysound.efx.effects.Reverb;
import com.wyozi.jaysound.player.FFTVisualizer;
import com.wyozi.jaysound.sound.StreamingSound;
import com.wyozi.jaysound.sound.StreamingSound2;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StreamBufferTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Configurator.defaultConfig()
                .formatPattern("{level}: {class}.{method}()\\t{message}")
                .level(org.pmw.tinylog.Level.DEBUG)
                .activate();

        Context context = new Context();

        context.updateListener(new ThrowawayVec3f(0, 0, 0), new ThrowawayVec3f(0, 0, -1), new ThrowawayVec3f(0, 0, 0));

        StreamBuffer buf = new StreamBuffer(new MP3Decoder(StreamLoader.openStreamingSoundStream(new URL("http://stream.plusfm.net/;"))));
        StreamingSound2 sound2 = new StreamingSound2(buf);
        sound2.setRolloff(0.7f, 1f);
        sound2.play();

        EffectZone zo = new EffectZone();
        zo.addEffect(new Echo());
        sound2.connectToEffectZone(zo);

        for (int i = 0;i < 50000; i++) {
            float x = 0f;
            float z = (float) (i / 60f) % 3;
            //sound2.setPos(new ThrowawayVec3f(x, 0, z));

            buf.update();
            sound2.update();
            Thread.sleep(20);
        }

    }
}
