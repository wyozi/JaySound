package com.wyozi.jaysound;

import com.wyozi.jaysound.sound.Sound;

import java.io.IOException;

/**
 * Created by wyozi on 27.12.2015.
 */
public class DopplerTest extends JaySoundTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        enableVerboseLogging();

        AudioContext ctx = new AudioContext();

        boolean listenerOrSound = true; // true = listener moves, false = source moves

        Sound sound = ctx.createStreamingSound(TEST_INTERNET_RADIO_URL);
        if (listenerOrSound) {
            ctx.setListenerVelocity(0f, 0f, 25f);
        } else {
            sound.setVelocity(0f, 0f, 25f);
        }
        sound.setRolloff(5f, 7f);
        sound.play();

        for (int i = 0;i < 200; i++) {
            ctx.update();

            float z = (i-100) * 0.5f;
            if (listenerOrSound) {
                ctx.setListenerPosition(0f, 0f, z);
            } else {
                sound.setPosition(0f, 0f, z);
            }

            Thread.sleep(20);
        }
    }
}
