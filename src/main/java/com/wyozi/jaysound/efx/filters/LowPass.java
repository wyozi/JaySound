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
    public LowPass(Preset preset) {
        this();
        preset.apply(this);
    }

    public void setGain(float f) {
        setFloat(EXTEfx.AL_LOWPASS_GAIN, f);
    }
    public void setGainHF(float f) {
        setFloat(EXTEfx.AL_LOWPASS_GAINHF, f);
    }

    public enum Preset {
        /**
         * Emulates sound coming from inside a car while listener is standing outside.
         *
         * Note: low pass filter by OpenAL is fairly limited. It might be worth it to use
         * {@link com.wyozi.jaysound.efx.effects.Equalizer} instead if more elaborate effect is needed.
         */
        CarStereo {
            @Override
            protected void apply(LowPass lowPass) {
                lowPass.setGain(0.9f);
                lowPass.setGainHF(0.05f);
            }
        };

        protected abstract void apply(LowPass lowPass);
    }
}
