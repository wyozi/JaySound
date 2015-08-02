package com.wyozi.jaysound.decoder;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public abstract class Decoder {
    public abstract void readFully(DecoderCallback callback) throws IOException;
}
