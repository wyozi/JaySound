package com.wyozi.jaysound;

import com.wyozi.jaysound.decoder.OggDecoder;
import com.wyozi.jaysound.player.PCMPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

/**
 * Created by wyozi on 30.11.2015.
 */
public class OggDecoderTest {
    public static void main(String[] args) throws IOException {
        OggDecoder oggDecoder = new OggDecoder(OggDecoderTest.class.getResourceAsStream("/Rayman_2_music_sample.ogg"));

        final PCMPlayer player = new PCMPlayer();
        oggDecoder.readFully(player);

        try {
            player.play();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
