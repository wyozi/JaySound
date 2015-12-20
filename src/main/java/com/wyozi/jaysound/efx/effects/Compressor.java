package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public class Compressor extends Effect {
    public Compressor() {
        super(EXTEfx.AL_EFFECT_COMPRESSOR);
    }
}
