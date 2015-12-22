package com.wyozi.jaysound.buffer;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.sound.StreamingSound;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * StreamBuffer is in charge of reading data from an input stream (in a worker thread) and constantly uploading it to OpenAl.'
 * Internally StreamBuffer uses multiple OpenAL buffers and switches between them. To support multiple streaming sounds
 * using the same databuffer StreamBuffer must do some behind the scenes magic to make sure that a in-use databuffer is not updated.
 *
 *
 * NOTE: Continuously calling update() in the main (OpenAL) thread is required for OpenAL databuffer updates to happen.
 *
 * Created by wyozi on 22.12.2015.
 */
public class StreamBuffer extends Buffer {
    /**
     * The amount of buffers that will be used for sound data storage.
     * If a sound lags behind this amount, it will be abruptly forced to sync.
     */
    private final int BUFFER_COUNT = 2;

    /**
     * How many seconds of sound data does a single databuffer contain.
     */
    private final int BUFFER_DURATION = 3;

    /**
     * The byte size of a single sample.
     */
    private final int SAMPLE_SIZE = 2;

    /**
     * The buffers
     */
    protected final SingleBuffer[] buffers = new SingleBuffer[BUFFER_COUNT];


    /**
     * Size of a single databuffer in bytes. This is resolved in runtime based on
     */
    protected final int dataBufferSize;

    /**
     * The bytebuffer that contains data to be uploaded to openal databuffer.
     */
    private final ByteBuffer alBuffer;

    /**
     * The input for stream data.
     */
    private final PipedInputStream streamDataInput;

    public StreamBuffer(Decoder decoder, boolean convertToMono) {
        super(decoder, convertToMono);

        int dataPerSecond = SAMPLE_SIZE * sampleRate * channels;
        dataBufferSize = dataPerSecond * BUFFER_DURATION;

        // Initially use two buffers. This works fine for a single
        for (int i = 0;i < BUFFER_COUNT; i++) {
            buffers[i] = new SingleBuffer(dataBufferSize);
        }

        streamDataInput = new PipedInputStream(dataBufferSize * BUFFER_COUNT);
        alBuffer = BufferUtils.createByteBuffer(dataBufferSize);

        Thread t = new Thread(new StreamDecodeWorker(decoder));
        t.setDaemon(true);
        t.start();
    }

    private Set<StreamingSound> sounds = Collections.newSetFromMap(new WeakHashMap<>());

    public void registerSound(StreamingSound streamingSound) {
        sounds.add(streamingSound);
    }

    public void update() {
        int soundCount = sounds.size();
        if (soundCount == 0) {
            return;
        }
        if (soundCount > 1) {
            throw new RuntimeException("More than one sound per databuffer is currently not supported.");
        }

        try {
            StreamingSound sound = sounds.iterator().next();
            if (!sound.isInitialSoundQueued()) {

                // Only queue data from first buffer at this point. This allows sound to begin playing faster
                SingleBuffer buffer = buffers[0];
                if (updateOpenALBuffer(buffer)) {
                    sound.queueBuffer(buffer.openalId);
                }

            } else if (streamDataAvailable()) {
                if (sound.getBuffersInRotationCount() < buffers.length) {
                    // All buffers offered by streambuffer are not yet in buffer rotation. Add one missing one per update tick

                    Set<Integer> buffersInRotation = sound.getBuffersInRotation();
                    for (SingleBuffer buffer : buffers) {
                        boolean inRotation = buffersInRotation.contains(buffer.openalId);

                        if (!inRotation) {
                            updateOpenALBuffer(buffer.openalId);
                            sound.queueBuffer(buffer.openalId);

                            Logger.debug("Found buffer {} not in rotation; sending it to sound to be queud", buffer.openalId);

                            break;
                        }
                    }

                } else {
                    // If all buffers are in rotation only update if there's any unprocessed ones.

                    int processedBuffer = sound.getProcessedBuffer();
                    if (processedBuffer != -1 && updateOpenALBuffer(processedBuffer)) {
                        sound.queueBuffer(processedBuffer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean streamDataAvailable() throws IOException {
        return streamDataInput.available() >= dataBufferSize;
    }

    private SingleBuffer getBufferByOpenALId(int openalId) {
        for (SingleBuffer buf : buffers) {
            if (buf.openalId == openalId) {
                return buf;
            }
        }
        return null;
    }

    @Override
    public byte[] getBufferData(int openalId) {
        SingleBuffer buf = getBufferByOpenALId(openalId);
        if (buf != null)
            return buf.databuffer;
        return null;
    }

    private boolean updateOpenALBuffer(SingleBuffer buf) throws IOException {
        if (streamDataAvailable()) {
            byte[] databuffer = buf.databuffer;

            int bytesRead = streamDataInput.read(databuffer);
            alBuffer.put(databuffer);
            alBuffer.flip();

            Logger.debug("Read {} bytes of data to buffer of size {} from piped output; writing it to openal buffer", bytesRead, databuffer.length);

            AudioContext.checkALError();

            AL10.alBufferData(
                    buf.openalId,
                    this.channels == 2 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                    alBuffer,
                    sampleRate);

            AudioContext.checkALError();

            return true;
        }

        return false;
    }

    private boolean updateOpenALBuffer(int openalBufferId) throws IOException {
        return updateOpenALBuffer(getBufferByOpenALId(openalBufferId));
    }

    @Override
    public void dispose() {
        for (SingleBuffer buffer : buffers) {
            AL10.alDeleteBuffers(buffer.openalId);
        }
    }

    private static class SingleBuffer {
        final int openalId = AL10.alGenBuffers();
        final byte[] databuffer;

        public SingleBuffer(int bufferSize) {
            this.databuffer = new byte[bufferSize];
        }
    }

    private class StreamDecodeWorker implements Runnable {
        private final Decoder decoder;

        public StreamDecodeWorker(Decoder decoder) {
            this.decoder = decoder;
        }

        @Override
        public void run() {
            try {
                PipedOutputStream streamDataOutput = new PipedOutputStream(streamDataInput);
                StreamBuffer.this.readFully(streamDataOutput, dataBufferSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
