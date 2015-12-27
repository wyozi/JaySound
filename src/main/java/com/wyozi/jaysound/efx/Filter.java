package com.wyozi.jaysound.efx;

import com.wyozi.jaysound.AudioContext;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public abstract class Filter {
    private final int id;

    public Filter(int efxFilterId) {
        this.id = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(id, EXTEfx.AL_FILTER_TYPE, efxFilterId);
        AudioContext.checkALError();
    }

    protected void setFloat(int param, float value) {
        EXTEfx.alFilterf(this.id, param, value);
        AudioContext.checkALError();
    }

    protected void setInt(int param, int value) {
        EXTEfx.alFilteri(this.id, param, value);
        AudioContext.checkALError();
    }

    public void dispose() {
        EXTEfx.alDeleteFilters(this.id);
    }

    public int getId() {
        return id;
    }
}
