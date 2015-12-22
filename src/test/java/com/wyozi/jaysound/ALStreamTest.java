package com.wyozi.jaysound;

import com.sun.org.apache.regexp.internal.RE;
import com.wyozi.jaysound.efx.EffectZone;
import com.wyozi.jaysound.efx.effects.*;
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
    public static void main(String[] args) throws InterruptedException, IOException {
        AudioContext ctx = new AudioContext();

        Sound sound = ctx.createStreamingSound(new URL("https://upload.wikimedia.org/wikipedia/en/3/3d/Sample_of_Daft_Punk's_Da_Funk.ogg"));
        sound.play();

        for (int i = 0;i < 100; i++) {
            ctx.update();
            Thread.sleep(30);
        }

        ctx.dispose();
    }
}
