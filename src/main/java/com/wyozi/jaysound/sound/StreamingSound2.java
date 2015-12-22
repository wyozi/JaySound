package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.Context;
import com.wyozi.jaysound.buffer.StreamBuffer;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StreamingSound2 extends Sound {
    public StreamingSound2(StreamBuffer buffer) {
        super(buffer);
        buffer.registerSound(this);
    }

    /**
     * @return OpenAL index of the oldest processed buffer. -1 if no buffers have been processed.
     */
    public int getProcessedBuffer() {
        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);
        if (processed > 0) {
            return AL10.alSourceUnqueueBuffers(source);
        }
        return -1;
    }

    private boolean initialSoundQueued = false;

    public boolean isInitialSoundQueued() {
        return initialSoundQueued;
    }

    private Set<Integer> buffersInRotation = new HashSet<>();

    public Set<Integer> getBuffersInRotation() {
        return buffersInRotation;
    }

    public int getBuffersInRotationCount() {
        return buffersInRotation.size();
    }

    public void queueBuffer(int bufferId) {
        AL10.alSourceQueueBuffers(source, bufferId);
        if (!buffersInRotation.contains(bufferId)) buffersInRotation.add(bufferId);
        initialSoundQueued = true;
    }
}
