package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.buffer.StreamBuffer;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StreamingSound extends Sound {
    private final StreamBuffer streamBuffer;

    public StreamingSound(StreamBuffer buffer) {
        super(buffer);
        this.streamBuffer = buffer;
        this.streamBuffer.registerSound(this);
    }

    @Override
    public void update() {
        super.update();

        // Streaming sounds can experience problems with eg. buffer underflows
        // or there not being enough samples in buffer when sound is played.
        // For that reason StreamingSound must have some extra checks here to make
        // sure the sound is playing if it should be playing.
        if (commandedPlayState == PlayState.Playing && getState() != PlayState.Playing) {
            play();
        }
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

        Logger.debug("Stream buffer {} queued", bufferId);
    }
}
