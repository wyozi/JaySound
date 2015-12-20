package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public class Echo extends Effect {
    public Echo() {
        super(EXTEfx.AL_EFFECT_ECHO);
    }

    /**
     * @param f value between 0 and 0.207
     */
    public void setDelay(float f) {
        setFloat(EXTEfx.AL_ECHO_DELAY, f);
    }

    /**
     * @param f value between 0 and 0.99
     */
    public void setDamping(float f) {
        setFloat(EXTEfx.AL_ECHO_DAMPING, f);
    }

    /**
     * @param f value between 0 and 1
     */
    public void setFeedback(float f) {
        setFloat(EXTEfx.AL_ECHO_FEEDBACK, f);
    }

    /**
     * @param f value between -1 and 1
     */
    public void setSpread(float f) {
        setFloat(EXTEfx.AL_ECHO_SPREAD, f);
    }
}
