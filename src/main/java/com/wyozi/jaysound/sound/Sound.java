package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.Context;
import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.buffer.Buffer;
import com.wyozi.jaysound.efx.EffectZone;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public abstract class Sound {
    protected final int source;
    private final Buffer buffer;

    public Sound(Buffer buffer) {
        this.source = AL10.alGenSources();
        this.buffer = buffer;
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
        Context.checkALError();
    }

    public void setDirection(JayVec3f dir) {
        checkStereo();

        AL10.alSource3f(source, AL10.AL_DIRECTION, dir.getJayX(), dir.getJayY(), dir.getJayZ());
        Context.checkALError();
    }

    public void setVelocity(JayVec3f vel) {
        checkStereo();

        AL10.alSource3f(source, AL10.AL_VELOCITY, vel.getJayX(), vel.getJayY(), vel.getJayZ());
        Context.checkALError();
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
        Context.checkALError();
    }
    public void setOuterConeGain(float outerGain) {
        checkStereo();

        AL10.alSourcef(source, AL10.AL_CONE_OUTER_GAIN, outerGain);
        Context.checkALError();
    }

    public void setVolume(float v) {
        AL10.alSourcef(source, AL10.AL_GAIN, v);
        Context.checkALError();
    }

    public void setPitch(float v) {
        AL10.alSourcef(source, AL10.AL_PITCH, v);
        Context.checkALError();
    }

    public void dispose() {
        AL10.alDeleteSources(source);
        Context.checkALError();
    }

    public void connectToEffectZone(EffectZone zone) {
        zone.connectALSource(this.source);
    }
}
