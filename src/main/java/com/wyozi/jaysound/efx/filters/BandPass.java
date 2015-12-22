package com.wyozi.jaysound.efx.filters;

import com.wyozi.jaysound.efx.Filter;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 22.12.2015.
 */
public class BandPass extends Filter {
    public BandPass() {
        super(EXTEfx.AL_FILTER_BANDPASS);
    }

    public void setGain(float f) {
        setFloat(EXTEfx.AL_BANDPASS_GAIN, f);
    }
    public void setGainHF(float f) {
        setFloat(EXTEfx.AL_BANDPASS_GAINHF, f);
    }
    public void setGainLF(float f) {
        setFloat(EXTEfx.AL_BANDPASS_GAINLF, f);
    }
}
