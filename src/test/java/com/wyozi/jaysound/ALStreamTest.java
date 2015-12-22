package com.wyozi.jaysound;

import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.efx.effects.*;
import com.wyozi.jaysound.efx.filters.LowPass;
import com.wyozi.jaysound.sound.Sound;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 30.11.2015.
 */
public class ALStreamTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        AudioContext ctx = new AudioContext();

        /*SoundEnvironment zone = new SoundEnvironment();
        zone.addEffect(Reverb.getBestAvailableReverb(Reverb.EFXPreset.HANGAR));
        ctx.setGlobalSoundEnvironment(zone);*/

        Sound sound = ctx.createStreamingSound(new URL("http://stream.plusfm.net/;"));
        sound.setDirectFilter(new LowPass(1f, 0.5f));
        sound.play();

        for (int i = 0;i < 1000; i++) {
            ctx.update();
            Thread.sleep(1000);
        }

        ctx.dispose();
    }
}
