package com.wyozi.jaysound;

import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.efx.effects.*;
import com.wyozi.jaysound.sound.StreamingSound;

import java.io.IOException;

/**
 * Created by wyozi on 30.11.2015.
 */
public class ALStreamTest extends JaySoundTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        enableVerboseLogging();

        AudioContext ctx = new AudioContext();

        SoundEnvironment zone1 = new SoundEnvironment();
        zone1.addEffect(Reverb.getBestAvailableReverb(Reverb.EFXPreset.ARENA));
        //zone1.addEffect(new Flanger());
        ctx.setGlobalSoundEnvironment(zone1);

        StreamingSound sound = ctx.createStreamingSound(TEST_INTERNET_RADIO_URL);
        sound.play();

        for (int i = 0;i < 1000; i++) {
            if (i % 5 == 0) {
                System.out.println(sound.getDecodedTitle());
            }

            ctx.update();
            Thread.sleep(1000);
        }

        ctx.dispose();
    }
}
