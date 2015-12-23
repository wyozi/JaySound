package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 23.12.2015.
 */
public class Equalizer extends Effect {
    public Equalizer() {
        super(EXTEfx.AL_EFFECT_EQUALIZER);
    }

    public void setLowGain(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_LOW_GAIN, f);
    }
    public void setLowCutoff(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_LOW_CUTOFF, f);
    }
    public void setMid1Center(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_MID1_CENTER, f);
    }
    public void setMid1Gain(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_MID1_GAIN, f);
    }
    public void setMid1Width(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_MID1_WIDTH, f);
    }
    public void setMid2Gain(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_MID2_GAIN, f);
    }
    public void setMid2Center(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_MID2_CENTER, f);
    }
    public void setMid2Width(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_MID2_WIDTH, f);
    }
    public void setHighGain(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_HIGH_GAIN, f);
    }
    public void setHighCutoff(float f) {
        setFloat(EXTEfx.AL_EQUALIZER_HIGH_CUTOFF, f);
    }
}
