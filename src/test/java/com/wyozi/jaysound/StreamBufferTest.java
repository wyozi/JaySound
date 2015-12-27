package com.wyozi.jaysound;

import com.wyozi.jaysound.sound.Sound;

import java.io.IOException;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StreamBufferTest extends JaySoundTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        enableVerboseLogging();

        AudioContext ctx = new AudioContext();

        Sound sound = ctx.createStreamingSound(TEST_INTERNET_RADIO_URL);
        sound.play();

        while (true) {
            ctx.update();

            double time = (System.currentTimeMillis() / 300.0);
            sound.setPosition((float) Math.cos(time), 0f, (float) Math.sin(time));

            Thread.sleep(30);
        }
    }
}
