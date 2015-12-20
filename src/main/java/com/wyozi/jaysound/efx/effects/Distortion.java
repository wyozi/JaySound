package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public class Distortion extends Effect {
    public Distortion() {
        super(EXTEfx.AL_EFFECT_DISTORTION);
    }

    /**
     * @param f value between 0 and 1
     */
    public void setEdge(float f) {
        setFloat(EXTEfx.AL_DISTORTION_EDGE, f);
    }

    /**
     * @param f value between 0 and 1
     */
    public void setGain(float f) {
        setFloat(EXTEfx.AL_DISTORTION_GAIN, f);
    }
}
