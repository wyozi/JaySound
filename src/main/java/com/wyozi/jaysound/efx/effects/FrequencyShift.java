package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public class FrequencyShift extends Effect {
    public FrequencyShift() {
        super(EXTEfx.AL_EFFECT_FREQUENCY_SHIFTER);
    }

    /**
     * Helper method.
     * @param amount the amount to shift in Hz. Must be between -24000 and 24000.
     */
    public void shift(float amount) {
        int direction = amount < 0 ? 0 : 1;
        setLeftDirection(direction);
        setRightDirection(direction);
        setFrequency(Math.abs(amount));
    }

    /**
     * @param f value between 0 and 24000
     */
    public void setFrequency(float f) {
        setFloat(EXTEfx.AL_FREQUENCY_SHIFTER_FREQUENCY, f);
    }

    /**
     * @param i (0 = down, 1 = up, 2 = off)
     */
    public void setLeftDirection(int i) {
        setInt(EXTEfx.AL_FREQUENCY_SHIFTER_DEFAULT_LEFT_DIRECTION, i);
    }

    /**
     * @param i (0 = down, 1 = up, 2 = off)
     */
    public void setRightDirection(int i) {
        setInt(EXTEfx.AL_FREQUENCY_SHIFTER_DEFAULT_RIGHT_DIRECTION, i);
    }


}
