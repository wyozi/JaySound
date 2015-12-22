package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.buffer.StaticBuffer;
import org.lwjgl.openal.AL10;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class BufferedSound extends Sound {
    public BufferedSound(StaticBuffer buffer) {
        super(buffer);
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer.getOpenALId());
        AudioContext.checkALError();
    }
}
