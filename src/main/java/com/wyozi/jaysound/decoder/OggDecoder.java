package com.wyozi.jaysound.decoder;

import com.wyozi.jaysound.player.PCMPlayer;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class OggDecoder extends Decoder {
    private final InputStream in;

    public OggDecoder(InputStream in) {
        this.in = in;
    }

    @Override
    public void readFully(DecoderCallback callback) throws IOException {
        readStreamFully(in, callback);
    }

}
