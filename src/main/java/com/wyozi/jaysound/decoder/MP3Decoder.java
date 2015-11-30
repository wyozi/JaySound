package com.wyozi.jaysound.decoder;

import com.wyozi.jaysound.player.PCMPlayer;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class MP3Decoder extends Decoder {
    private final InputStream in;

    public MP3Decoder(InputStream in) {
        this.in = in;
    }

    @Override
    public void readFully(DecoderCallback callback) throws IOException {
        readStreamFully(in, callback);
    }
}
