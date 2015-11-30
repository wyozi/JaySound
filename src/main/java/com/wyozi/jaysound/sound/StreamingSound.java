package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.Context;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.DecoderCallback;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.*;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class StreamingSound extends Sound {
    private final int STREAMING_BUFFER_COUNT = 2;
    private final int STREAMING_BUFFER_SIZE = 48000;

    private int bufferIndex = 0;
    private final int[] buffers = new int[STREAMING_BUFFER_COUNT];

    public StreamingSound() {
        super();

        for (int i = 0;i < buffers.length; i++) {
            buffers[i] = AL10.alGenBuffers();
        }
        Context.checkALError();
    }

    final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private volatile boolean hasSetupBuffers = false;
    private void setupBuffers() {
        // Queue all buffers
        for (int i = 0;i < buffers.length; i++) {
            // TODO this is terrible, lol
            while (!hasSomeData()) {
                Thread.yield();
            }

            readInto(buffers[i]);
            AL10.alSourceQueueBuffers(source, buffers[i]);
            Context.checkALError();
        }

        hasSetupBuffers = true;
    }

    @Override
    public void update() {
        super.update();

        if (!hasSetupBuffers) {
            setupBuffers();
        }

        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
        while (processed > 0 && hasSomeData()) {
            int buffer = AL10.alSourceUnqueueBuffers(source);
            readInto(buffer);
            AL10.alSourceQueueBuffers(source, buffer);

            processed--;
        }

        if (playAsap) {
            this.play();
            playAsap = false;
        }
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

    public boolean hasSomeData() {
        return baos.size() > 0;
    }

    private void readInto(int buffer) {
        byte[] data;
        synchronized (baos) {
            data = baos.toByteArray();
            baos.reset();
        }

        ByteBuffer bdata;
        if (channels > 1) {
            // Two channels compressed into one means two times less data
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
                //channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                bdata,
                rate);
        Context.checkALError();
    }

    private int rate, channels;
    private volatile boolean hasSetup = false;
    private void setup(int rate, int channels) {
        this.rate = rate;
        this.channels = channels;

        hasSetup = true;
    }

    public boolean hasSetup() {
        return this.hasSetup;
    }

    private void loadInternal(Decoder decoder) throws IOException {
        decoder.readFully(new DecoderCallback() {
            @Override
            public void writePCM(byte[] data, int offset, int length) {
                synchronized (baos) {
                    baos.write(data, offset, length);
                }
            }

            @Override
            public void inform(int channels, int rate) {
                setup(rate, channels);
            }
        });
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
