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

        BufferedSound sound2 = context.createBufferedSound(TEST_INTERNET_AUDIO_URL);
        sound2.play();

        context.update();
        Thread.sleep(25000);
    }
}
