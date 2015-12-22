package com.wyozi.jaysound.efx;

import com.wyozi.jaysound.AudioContext;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public abstract class Effect {
    final int id;
    private final int effectId;

    /**
     * Is effect attached to a zone.
     */
    boolean attached = false;

    public Effect(int efxEffectId) {
        this.id = org.lwjgl.openal.EXTEfx.alGenEffects();
        this.effectId = efxEffectId;
        EXTEfx.alEffecti(id, EXTEfx.AL_EFFECT_TYPE, this.effectId);
        AudioContext.checkALError();
    }

    protected void setFloat(int param, float value) {
        if (attached)
            throw new RuntimeException("unable to set effect parameter on an effect already attached to a zone");

        EXTEfx.alEffectf(this.id, param, value);
        AudioContext.checkALError();
    }

    protected void setInt(int param, int value) {
        if (attached)
            throw new RuntimeException("unable to set effect parameter on an effect already attached to a zone");

        EXTEfx.alEffecti(this.id, param, value);
        AudioContext.checkALError();
    }

}
