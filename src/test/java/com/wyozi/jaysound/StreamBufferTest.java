package com.wyozi.jaysound;

import com.wyozi.jaysound.buffer.StreamBuffer;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.sound.StreamingSound;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
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

        AudioContext context = new AudioContext();

        context.updateListener(new ThrowawayVec3f(0, 0, 0), new ThrowawayVec3f(0, 0, -1), new ThrowawayVec3f(0, 0, 0));

        StreamBuffer buf = new StreamBuffer(new MP3Decoder(StreamLoader.openSoundStream(new URL("http://stream.plusfm.net/;"))), true);
        StreamingSound sound2 = new StreamingSound(buf);
        //sound2.setRolloff(0.7f, 1.3f);
        sound2.play();

        for (int i = 0;i < 50000; i++) {
            float x = (float) (Math.cos(i/30f)*1f);
            float z = (float) (Math.sin(i/30f)*1.5f);
            //sound2.setPos(new ThrowawayVec3f(x, 0, z));

            sound2.update();
            Thread.sleep(20);
        }

    }
}
