package com.wyozi.jaysound.efx.filters;

import com.wyozi.jaysound.efx.Filter;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 22.12.2015.
 */
public class HighPass extends Filter {
    public HighPass() {
        super(EXTEfx.AL_FILTER_HIGHPASS);
    }

    public void setGain(float f) {
        setFloat(EXTEfx.AL_HIGHPASS_GAIN, f);
    }
    public void setGainLF(float f) {
        setFloat(EXTEfx.AL_HIGHPASS_GAINLF, f);
    }
}
