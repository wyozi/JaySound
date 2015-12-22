package com.wyozi.jaysound.buffer;

import org.lwjgl.openal.AL10;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StaticBuffer extends Buffer {
    protected final int buffer;

    public StaticBuffer() {
        this.buffer = AL10.alGenBuffers();
    }
}
