package com.wyozi.jaysound.buffer;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.decoder.Decoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StaticBuffer extends Buffer {
    protected final int buffer;
    private final byte[] data;

    public StaticBuffer(Decoder decoder, boolean convertToMono) throws IOException {
        super(decoder, convertToMono);

        this.buffer = AL10.alGenBuffers();

        this.data = decode(decoder);
        Logger.debug("Static buffer data has been decoded. Size: {}", this.data.length);

        ByteBuffer bdata = BufferUtils.createByteBuffer(this.data.length);
        bdata.put(this.data);
        bdata.flip();

        AL10.alBufferData(
                buffer,
                channels == 2 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                bdata,
                this.sampleRate);

        AudioContext.checkALError();
    }

    @Override
    public byte[] getBufferData(int openalId) {
        return this.data;
    }

    private byte[] decode(Decoder decoder) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.readFully(baos, 2048);
        return baos.toByteArray();
    }

    public int getOpenALId() {
        return this.buffer;
    }

    @Override
    public void dispose() {
        AL10.alDeleteBuffers(this.buffer);
    }
}
