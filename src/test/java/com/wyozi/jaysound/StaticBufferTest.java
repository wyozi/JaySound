package com.wyozi.jaysound;

import com.wyozi.jaysound.buffer.StaticBuffer;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.sound.BufferedSound;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StaticBufferTest extends JaySoundTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        enableVerboseLogging();

        AudioContext context = new AudioContext();

        context.updateListener(new ThrowawayVec3f(0, 0, 0), new ThrowawayVec3f(0, 0, -1), new ThrowawayVec3f(0, 0, 0));

        StaticBuffer buf = new StaticBuffer(new MP3Decoder(StreamLoader.openSoundStream(TEST_INTERNET_AUDIO_URL)), true);
        BufferedSound sound2 = new BufferedSound(buf);
        sound2.setRolloff(0.7f, 1f);
        sound2.play();

        for (int i = 0;i < 50000; i++) {
            float x = 0f;
            float z = i / 60f % 3;
            //sound2.setPos(new ThrowawayVec3f(x, 0, z));

            sound2.update();
            Thread.sleep(20);
        }
    }
}
