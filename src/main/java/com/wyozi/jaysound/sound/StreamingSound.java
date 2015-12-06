package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.Context;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.DecoderCallback;
import com.wyozi.jaysound.decoder.DecoderUtils;
import ddf.minim.analysis.FFT;
import ddf.minim.analysis.FourierTransform;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.*;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class StreamingSound extends Sound {
    private final int FFT_SIZE = 512;

    private final int STREAMING_BUFFER_COUNT = 2;

    private int bufferIndex = 0;
    private final int[] buffers = new int[STREAMING_BUFFER_COUNT];

    // TODO variable sizify
    private final float[][] dataBuffers = new float[STREAMING_BUFFER_COUNT][200_000];

    public StreamingSound() {
        super();

        for (int i = 0;i < buffers.length; i++) {
            buffers[i] = AL10.alGenBuffers();
        }
        Context.checkALError();
    }

    /**
     * OpenAL Buffer id to local zero-indexed buffer id.
     * @param openal
     * @return
     */
    private int openALBufferToZeroIndex(int openal) {
        for (int i = 0;i < buffers.length; i++) {
            if (buffers[i] == openal) {
                return i;
            }
        }
        return -1;
    }

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private byte[] readAndStoreData(int buffer) {
        byte[] data = readRemainingData();

        float[] floatData = new float[data.length / 2];
        DecoderUtils.toNormalizedFloatArray(data, floatData);
        dataBuffers[buffer - 2] = floatData;

        return data;
    }

    private volatile boolean hasSetupBuffers = false;
    private void setupBuffers() {
        // Queue all buffers
        for (int i = 0;i < buffers.length; i++) {
            while (!hasSomeData()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {}
            }

            readInto(buffers[i], readAndStoreData(buffers[i]));
            AL10.alSourceQueueBuffers(source, buffers[i]);
            Context.checkALError();
        }

        hasSetupBuffers = true;
    }

    @Override
    public void update() {
        super.update();

        if (!hasSetupBuffers) {
            // TODO add a timeout
            setupBuffers();
        }

        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
        while (processed > 0 && hasSomeData()) {
            int buffer = AL10.alSourceUnqueueBuffers(source);

            readInto(buffer, readAndStoreData(buffer));
            AL10.alSourceQueueBuffers(source, buffer);

            processed--;
        }

        if (playAsap) {
            this.play();
            playAsap = false;
        }
    }

    private final float[] tmpFft = new float[FFT_SIZE];
    private FFT fft;

    public FFT getFft() {
        return fft;
    }

    public float[] fft() {
        int curBuffer = AL10.alGetSourcei(source, AL10.AL_BUFFER);
        int curSample = AL10.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET);
        int curBufferLocal = openALBufferToZeroIndex(curBuffer);

        // If not playing return early
        if (curBufferLocal == -1) {
            return tmpFft; // TODO return something nicer
        }

        float[] curBufferData = this.dataBuffers[curBufferLocal];

        int remaining = curBufferData.length-curSample-1;
        if (remaining > 0) {
            int length = Math.min(FFT_SIZE, remaining);
            System.arraycopy(curBufferData, curSample, tmpFft, 0, length);

            // If we're nearing current buffer's end fetch some samples from the other buffer
            if (remaining < FFT_SIZE) {
                int samplesNeeded = FFT_SIZE - remaining;
                float[] otherBufferData = this.dataBuffers[1 - curBufferLocal];
                System.arraycopy(otherBufferData, 0, tmpFft, length, samplesNeeded);
            }
        }

        this.fft.forward(tmpFft);
        return this.tmpFft;
    }

    private volatile boolean playAsap = false;
    public void play() {
        if (!hasSetupBuffers) {
            playAsap = true;
            return;
        }

        AL10.alSourcePlay(source);
    }
    public void pause() {
        if (!hasSetupBuffers) {
            playAsap = false;
            return;
        }

        AL10.alSourcePause(source);
    }

    public void load(Decoder decoder) throws IOException {
        AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 2f);
        AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, 200f);
        AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, 0.7f); // exponential

        AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
        AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
        Context.checkALError();

        new Thread(() -> {
            try {
                loadInternal(decoder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean hasSomeData() {
        return baos.size() > 0;
    }

    private byte[] readRemainingData() {
        byte[] data;
        synchronized (baos) {
            data = baos.toByteArray();
            baos.reset();
        }
        return data;
    }

    private void readInto(int buffer, byte[] data) {
        ByteBuffer bdata;
        if (channels > 1) {
            // Two targetChannelCount compressed into one means two times less data
            bdata = ByteBuffer.allocateDirect(data.length / 2).order(ByteOrder.LITTLE_ENDIAN);

            ByteBuffer wrapped = ByteBuffer.wrap(data);
            wrapped.order(ByteOrder.LITTLE_ENDIAN);

            float gain = (float) Math.pow(10.0f, (-6.0f / 20.0f));
            for (int i = 0;i < bdata.capacity()/2; i++) {
                short channel1 = wrapped.getShort();
                short channel2 = wrapped.getShort();

                int combined = (int) ((channel1 + channel2) * gain);

                bdata.putShort((short) combined);
            }
            bdata.rewind();
        } else {
            bdata = ByteBuffer.allocateDirect(data.length);
            bdata.put(data);
            bdata.rewind();
        }

        Logger.debug("Writing {} bytes of delicious PCM data into buffer #{}", bdata.capacity(), buffer);

        AL10.alBufferData(
                buffer,
                AL10.AL_FORMAT_MONO16,
                //targetChannelCount > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                bdata,
                rate);
        Context.checkALError();
    }

    private int rate, channels;
    private volatile boolean hasSetup = false;
    private void setup(int rate, int channels) {
        this.rate = rate;
        this.channels = channels;

        this.fft = new FFT(512, rate);
        this.fft.window(FourierTransform.HAMMING);

        hasSetup = true;
    }

    public boolean hasSetup() {
        return this.hasSetup;
    }

    private void loadInternal(Decoder decoder) throws IOException {
        // Note: things seem to break if you pass stereo data to baos, so we read
        // everything as mono in here.

        setup(decoder.getSampleRate(), 1);

        byte[] data = new byte[1024];
        int read;
        while ((read = decoder.readMono(data, 0, data.length)) != -1) {
            synchronized (baos) {
                baos.write(data, 0, read);
            }
        }
    }


    @Override
    public void dispose() {
        super.dispose();

        for (int i = 0;i < buffers.length; i++) {
            AL10.alDeleteBuffers(buffers[i]);
        }
        Context.checkALError();
    }
}
