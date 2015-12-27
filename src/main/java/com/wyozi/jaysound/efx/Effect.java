package com.wyozi.jaysound.efx;

import com.wyozi.jaysound.AudioContext;
import org.lwjgl.openal.EXTEfx;
import org.pmw.tinylog.Logger;

/**
 * Created by wyozi on 20.12.2015.
 */
public abstract class Effect {
    final int id;

    /**
     * Whether effect is attached to a {@link SoundEnvironment}.
     */
    boolean attached = false;

    public Effect(int efxEffectId) {
        this.id = org.lwjgl.openal.EXTEfx.alGenEffects();
        EXTEfx.alEffecti(id, EXTEfx.AL_EFFECT_TYPE, efxEffectId);
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

    private boolean disposed = false;

    public void dispose() {
        EXTEfx.alDeleteEffects(this.id);
        disposed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (!disposed) {
            Logger.warn("Effect id #{} getting disposed by GC", this.id);
            dispose();
        }
    }
}
