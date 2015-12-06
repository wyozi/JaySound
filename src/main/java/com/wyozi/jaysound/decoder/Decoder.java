package com.wyozi.jaysound.decoder;

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
    public static final Settings DEFAULT_SETTINGS = new Settings();

    protected final Settings settings;

    public Decoder(Settings settings) {
        this.settings = settings;
    }

    public Decoder() {
        this(DEFAULT_SETTINGS);
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
     * Initialize decoder and possibly read some header data from the input.
     *
     * This should populate channel count and sample rate.
     */
    protected abstract void internalInit() throws IOException;

    public abstract int read(byte[] buffer, int offset, int length) throws IOException;

    public int read(byte[] buffer) throws IOException {
        return this.read(buffer, 0, buffer.length);
    }

    private final byte[] tmpByte = new byte[1024 * 10];
    public byte[] getTmpByteBuffer() {
        return tmpByte;
    }

    /**
     * Read from audio source to buffer. If source is stereo, converts it to mono.
     *
     * @param buffer
     * @return
     */
    public int readMono(byte[] buffer, int offset, int length) throws IOException {
        if (channelCount == 1) {
            return this.read(buffer);
        } else {
            // assume stereo (2 channels) sound with 16 bit samples
            int read = this.read(tmpByte, 0, length * 2);

            for (int i = 0;i < length; i+=2) {
                int rawI = i * 2;

                buffer[offset + i] = tmpByte[rawI];
                buffer[offset + i+1] = tmpByte[rawI+1];
            }

            return read / 2;
        }
    }

    private int internalReadShorts(byte[] sourceBuffer, short[] targetBuffer) {
        return DecoderUtils.toShortArray(sourceBuffer, targetBuffer);
    }

    public int readShorts(short[] shortBuffer) throws IOException {
        int read = this.read(tmpByte, 0, shortBuffer.length * 2);
        internalReadShorts(tmpByte, shortBuffer);
        return read / 2;
    }

    public int readShortsMono(short[] shortBuffer) throws IOException {
        int read = this.readMono(tmpByte, 0, shortBuffer.length * 2);
        internalReadShorts(tmpByte, shortBuffer);
        return read / 2;
    }

    private void internalReadNormFloats(short[] sourceBuffer, float[] floatBuffer) {
        DecoderUtils.toNormalizedFloatArray(sourceBuffer, floatBuffer);
    }

    /**
     * Reads data as floats normalized to 0-1 range.
     * @param floatBuffer
     * @return
     */
    public int readNormalizedFloats(float[] floatBuffer) throws IOException {
        short[] tmp = new short[floatBuffer.length];
        int read = this.readShorts(tmp);

        internalReadNormFloats(tmp, floatBuffer);
        return read;
    }

    public int readNormalizedFloatsMono(float[] floatBuffer) throws IOException {
        short[] tmp = new short[floatBuffer.length];
        int read = this.readShortsMono(tmp);

        internalReadNormFloats(tmp, floatBuffer);
        return read;
    }

    /**
     * @param callback
     * @throws IOException
     * @deprecated use read() methods instead
     */
    @Deprecated
    public void readFully(DecoderCallback callback) throws IOException {
        callback.inform(this.channelCount, this.sampleRate);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = this.read(buffer, 0, 1024)) != -1) {
            callback.writePCM(buffer, 0, read);
        }
    }

    public static class Settings {
    }
}
