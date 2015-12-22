package com.wyozi.jaysound;

import com.sun.org.apache.regexp.internal.RE;
import com.wyozi.jaysound.efx.EffectZone;
import com.wyozi.jaysound.efx.effects.*;
import com.wyozi.jaysound.player.FFTVisualizer;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.sound.StreamingSound;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Configurator;

import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 30.11.2015.
 */
public class ALStreamTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        AudioContext ctx = new AudioContext();

        EffectZone zone = new EffectZone();
        zone.addEffect(Reverb.getBestAvailableReverb(Reverb.EFXPreset.ARENA));
        ctx.setGlobalEffectZone(zone);

        Sound sound = ctx.createStreamingSound(new URL("http://stream.plusfm.net/;"));
        sound.play();

        for (int i = 0;i < 1000; i++) {
            ctx.update();
            Thread.sleep(1000);
        }

        ctx.dispose();
    }
}
