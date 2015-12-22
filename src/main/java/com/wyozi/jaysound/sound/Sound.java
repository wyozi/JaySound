package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.buffer.Buffer;
import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.util.DataConverterUtils;
import ddf.minim.analysis.FFT;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

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

    /**
     * Whether sound should be playing or not.
     */
    protected boolean targetPlayState = false;

    public void play() {
        AL10.alSourcePlay(source);
        targetPlayState = true;
    }

    public void pause() {
        AL10.alSourcePause(source);
        targetPlayState = false;
    }
    public void update() {
        // Sets source state if it's not what we want
        // Especially big cases where this happens are play() before loading any sound data or streaming sound
        //      not loading enough data.
        // TODO find a better way to do this (that does not require query and allows more states)
        if ((AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) != targetPlayState) {
            if (targetPlayState)
                play();
            else
                pause();
        }
    }

    /**
     * Makes sure that our data comes from mono source or throws an exception.
     */
    private void checkStereo() {
        if (this.buffer.getChannelCount() == 2)
            throw new RuntimeException("cannot set 3d parameter on stereo sound");
    }

    public void setPos(JayVec3f pos) {
        checkStereo();

        AL10.alSource3f(source, AL10.AL_POSITION, pos.getJayX(), pos.getJayY(), pos.getJayZ());
        AudioContext.checkALError();
    }

    public void setDirection(JayVec3f dir) {
        checkStereo();

        AL10.alSource3f(source, AL10.AL_DIRECTION, dir.getJayX(), dir.getJayY(), dir.getJayZ());
        AudioContext.checkALError();
    }

    public void setVelocity(JayVec3f vel) {
        checkStereo();

        AL10.alSource3f(source, AL10.AL_VELOCITY, vel.getJayX(), vel.getJayY(), vel.getJayZ());
        AudioContext.checkALError();
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

    public void connectToEnvironment(SoundEnvironment zone) {
        zone.connectALSource(this.source);
    }

    public FFT getFft() {
        return fft;
    }

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
            fftBuffer[fftIndex] = DataConverterUtils.toNormalizedFloat((short) DataConverterUtils.toShort(byte0, byte1));
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
        }
    }
}
