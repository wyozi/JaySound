package com.wyozi.jaysound.decoder;

import com.wyozi.jaysound.StreamLoader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public abstract class Decoder {
    protected InputStream in;

    public Decoder(InputStream in) throws IOException {
        this.in = in;
        this.internalInit();
    }

    protected int channelCount = -1;
    protected int sampleRate = -1;

    public int getChannelCount() {
        return channelCount;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    /**
     * Returns title of this sound stream if it can be decoded from the input stream.
     * @return the title or null if it has not been decoded.
     */
    public String getDecodedTitle() {
        if (this.in instanceof StreamLoader.ICYInputStream) {
            return ((StreamLoader.ICYInputStream) this.in).getStreamTitle();
        }
        return null;
    }

    /**
     * Initialize decoder and possibly read some header data from the input.
     *
     * This should populate channel count and sample rate.
     */
    protected abstract void internalInit() throws IOException;

    public abstract int read(byte[] buffer, int offset, int length) throws IOException;

    public int read(byte[] buffer) throws IOException {
        return this.read(buffer, 0, buffer.length);
    }
}
