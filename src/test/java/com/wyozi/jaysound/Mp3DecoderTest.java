package com.wyozi.jaysound;

import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.decoder.OggDecoder;
import com.wyozi.jaysound.player.PCMPlayer;

import javax.sound.sampled.LineUnavailableException;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by wyozi on 30.11.2015.
 */
public class Mp3DecoderTest {
    public static void main(String[] args) throws IOException {
        MP3Decoder oggDecoder = new MP3Decoder(Mp3DecoderTest.class.getResourceAsStream("/forestmaze.mp3"));

        final PCMPlayer player = new PCMPlayer();
        oggDecoder.readFully(player);

        try {
            player.play();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
