package com.wyozi.jaysound.efx.filters;

import com.wyozi.jaysound.efx.Filter;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 22.12.2015.
 */
public class LowPass extends Filter {
    public LowPass() {
        super(EXTEfx.AL_FILTER_LOWPASS);
    }
    public LowPass(float gain) {
        this();
        this.setGain(gain);
    }
    public LowPass(float gain, float gainHF) {
        this(gain);
        this.setGainHF(gainHF);
    }

    public void setGain(float f) {
        setFloat(EXTEfx.AL_LOWPASS_GAIN, f);
    }
    public void setGainHF(float f) {
        setFloat(EXTEfx.AL_LOWPASS_GAINHF, f);
    }
}
