package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.Context;
import com.wyozi.jaysound.buffer.StaticBuffer;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.DecoderCallback;
import org.lwjgl.openal.AL10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class BufferedSound extends Sound {
    public BufferedSound(StaticBuffer buffer) {
        super(buffer);
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer.getOpenALId());
        Context.checkALError();
    }
}
