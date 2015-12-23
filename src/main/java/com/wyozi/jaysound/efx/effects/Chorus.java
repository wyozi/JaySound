package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 23.12.2015.
 */
public class Chorus extends Effect {
    public Chorus() {
        super(EXTEfx.AL_EFFECT_CHORUS);
    }

    public enum Waveform {
        Sin, Triangle;
    }
    public void setWaveform(Waveform waveform) {
        int value = waveform == Waveform.Sin ? 0 : 1;
        setInt(EXTEfx.AL_CHORUS_WAVEFORM, value);
    }

    public void setPhase(int i) {
        setInt(EXTEfx.AL_CHORUS_PHASE, i);
    }
    public void setRate(float rate) {
        setFloat(EXTEfx.AL_CHORUS_RATE, rate);
    }
    public void setDepth(float depth) {
        setFloat(EXTEfx.AL_CHORUS_DEPTH, depth);
    }
    public void setFeedback(float feedback) {
        setFloat(EXTEfx.AL_CHORUS_FEEDBACK, feedback);
    }
    public void setDelay(float delay) {
        setFloat(EXTEfx.AL_CHORUS_DELAY, delay);
    }
}
