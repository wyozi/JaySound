package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.buffer.Buffer;
import com.wyozi.jaysound.efx.Filter;
import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.util.DataConverterUtils;
import ddf.minim.analysis.FFT;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;
import org.pmw.tinylog.Logger;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public abstract class Sound {
    protected final int source;
    private final Buffer buffer;

    private final FFT fft;

    private final float[] fftBuffer;
    public Sound(Buffer buffer) {
        this.source = AL10.alGenSources();
        this.buffer = buffer;

        final int fftTimeSize = 512;
        this.fft = new FFT(fftTimeSize, this.buffer.getSampleRate());
        this.fftBuffer = new float[fftTimeSize];
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public enum PlayState {
        Initial(AL10.AL_INITIAL),
        Playing(AL10.AL_PLAYING),
        Paused(AL10.AL_PAUSED),
        Stopped(AL10.AL_STOPPED);

        private final int alid;

        private PlayState(int alid) {
            this.alid = alid;
        }
    }

    /**
     * Queries and returns the play state of this sound.
     *
     * Consider
     *
     * @return the current play state of the sound
     */
    public PlayState getState() {
        int alState = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);

        for (PlayState possibleState : PlayState.values()) {
            if (possibleState.alid == alState) {
                return possibleState;
            }
        }

        return null;
    }

    /**
     * @return if this sound is active (ie. playing or paused).
     */
    public boolean isActive() {
        PlayState state = getState();
        return state == PlayState.Playing || state == PlayState.Paused;
    }

    /**
     * @return if this sound is currently playing (note: does not mean that the sound can be currently heard)
     */
    public boolean isPlaying() {
        return getState() == PlayState.Playing;
    }

    /**
     * The play state user has told this sound to be in.
     */
    protected PlayState commandedPlayState;

    /**
     * Plays the sound.
     */
    public void play() {
        AL10.alSourcePlay(source);
        this.commandedPlayState = PlayState.Playing;
    }

    /**
     * Pauses the sound.
     */
    public void pause() {
        AL10.alSourcePause(source);
        this.commandedPlayState = PlayState.Paused;
    }

    /**
     * Stops the sound.
     *
     * The sound will still be valid and can be restarted from beginning with {@link Sound#play()}
     */
    public void stop() {
        AL10.alSourceStop(source);
        this.commandedPlayState = PlayState.Stopped;
    }

    /**
     * Updates sound (ie. makes sure the sound's internal state matches expected)
     *
     * This is called automatically on all sounds in {@link AudioContext#update()}, but can be called
     * manually if AudioContext is not used for some reason.
     */
    public void update() {}

    /**
     * Makes sure that our data comes from mono source or throws an exception.
     */
    private void checkStereo() {
        if (this.buffer.getChannelCount() == 2)
            throw new RuntimeException("cannot set 3d parameter on stereo sound");
    }

    /**
     * Sets the sound position in the world.
     */
    public void setPosition(float x, float y, float z) {
        checkStereo();
        AL10.alSourcei(source, AL10.AL_SOURCE_RELATIVE, AL10.AL_FALSE);
        AL10.alSource3f(source, AL10.AL_POSITION, x, y, z);
        AudioContext.checkALError();
    }
    public void setPosition(JayVec3f pos) {
        setPosition(pos.getJayX(), pos.getJayY(), pos.getJayZ());
    }

    /**
     * Disables 3D-ness of the sound (ie. the gain will not be affected by listener position anymore)
     *
     * You can call {@link Sound#setPosition(float, float, float)} to re-enable 3D sound.
     */
    public void disable3D() {
        AL10.alSourcei(source, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);
        AL10.alSource3f(source, AL10.AL_POSITION, 0.0f, 0.0f, 0.0f);
        AL10.alSource3f(source, AL10.AL_VELOCITY, 0.0f, 0.0f, 0.0f);
    }

    /**
     * Sets the sound direction in the world (ie. to what direction the sound is pointing at)
     */
    public void setDirection(float x, float y, float z) {
        checkStereo();
        AL10.alSource3f(source, AL10.AL_DIRECTION, x, y, z);
        AudioContext.checkALError();
    }
    public void setDirection(JayVec3f dir) {
        setDirection(dir.getJayX(), dir.getJayY(), dir.getJayZ());
    }

    /**
     * Sets the sound velocity in the world.
     */
    public void setVelocity(float x, float y, float z) {
        checkStereo();
        AL10.alSource3f(source, AL10.AL_VELOCITY, x, y, z);
        AudioContext.checkALError();
    }
    public void setVelocity(JayVec3f vel) {
        setVelocity(vel.getJayX(), vel.getJayY(), vel.getJayZ());
    }

    /**
     * Sets the attenuation rolloff parameters for this sound.
     *
     * @param minDistance distance where gain is maximum
     * @param halfGainDistance distance at which gain is 50% of minDistance's gain.
     */
    public void setRolloff(float minDistance, float halfGainDistance) {
        checkStereo();

        // TODO dynamically figure out which model we're using
        setInverseDistanceRolloff(minDistance, halfGainDistance);
    }

    /**
     * <code>
     *     gain = AL_REFERENCE_DISTANCE / (AL_REFERENCE_DISTANCE + AL_ROLLOFF_FACTOR * (distance – AL_REFERENCE_DISTANCE));
     * </code>
     * @param minDistance
     * @param halfGainDistance
     */
    private void setInverseDistanceRolloff(float minDistance, float halfGainDistance) {
        AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, minDistance);

        /*
        Rolloff can be calculated by solving the inverse distance rolloff model equation for rolloff when gain is 0 and distance is halfGainDistance
         */
        // Note: to prevent infinities rolloff is calculated for when gain is 0.01 rather than zero
        float rolloff = (minDistance) / (halfGainDistance - minDistance);
        AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, rolloff);
    }

    public void setCone(float inner, float outer) {
        checkStereo();

        AL10.alSourcef(source, AL10.AL_CONE_INNER_ANGLE, inner);
        AL10.alSourcef(source, AL10.AL_CONE_OUTER_ANGLE, outer);
        AudioContext.checkALError();
    }

    public void setOuterConeGain(float outerGain) {
        checkStereo();

        AL10.alSourcef(source, AL10.AL_CONE_OUTER_GAIN, outerGain);
        AudioContext.checkALError();
    }
    public void setVolume(float v) {
        AL10.alSourcef(source, AL10.AL_GAIN, v);
        AudioContext.checkALError();
    }

    public void setPitch(float v) {
        AL10.alSourcef(source, AL10.AL_PITCH, v);
        AudioContext.checkALError();
    }

    /**
     * Connects this sound to a {@link SoundEnvironment}
     * @param zone
     */
    public void connectToEnvironment(SoundEnvironment zone) {
        zone.connectSound(this);
    }

    private Filter directFilter;
    public Filter getDirectFilter() {
        return directFilter;
    }
    public void setDirectFilter(Filter directFilter) {
        this.directFilter = directFilter;
        AL10.alSourcei(source, EXTEfx.AL_DIRECT_FILTER, directFilter != null ? directFilter.getId() : EXTEfx.AL_FILTER_NULL);
    }

    public FFT getFft() {
        return fft;
    }

    /**
     * Updates FFT object to match currently playing samples.
     */
    public void updateFft() {
        int curBuffer = AL10.alGetSourcei(source, AL10.AL_BUFFER);
        int curSample = AL10.alGetSourcei(source, AL11.AL_SAMPLE_OFFSET);

        byte[] data = this.buffer.getBufferData(curBuffer);

        // If not playing return early
        if (data == null) {
            return;
        }

        int sampleLength = Math.min(fftBuffer.length, (data.length - curSample * 2) / 2);

        for (int fftIndex = 0; fftIndex < sampleLength; fftIndex++) {
            int sampleIndex = fftIndex + curSample;
            int byte0 = data[sampleIndex * 2 + 0] & 0xFF;
            int byte1 = data[sampleIndex * 2 + 1] & 0xFF;
            fftBuffer[fftIndex] = DataConverterUtils.toNormalizedFloat(DataConverterUtils.toShort(byte0, byte1));
        }

        // TODO account for data buffer wraparounds
        /*
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
        }*/

        this.fft.forward(fftBuffer);
    }

    public int getOpenALSourceId() {
        return this.source;
    }

    /**
     * Returns the decoded title of the sound. Decoded means the title is parsed from the data input stream itself and
     * thus can contain real-time data of eg. a web music stream track title.
     *
     * This also means that the title returned by this method can be inaccurate.
     */
    public String getDecodedTitle() {
        return buffer.getDecodedTitle();
    }

    /**
     * Has the internal OpenAL sound object been disposed of
     */
    private boolean disposed = false;

    public void dispose() {
        AL10.alDeleteSources(source);
        AudioContext.checkALError();

        this.disposed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (!disposed) {
            dispose();
            Logger.warn("Sound id #{} getting disposed by GC", this.source);
        }
    }
}
