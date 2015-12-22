package com.wyozi.jaysound.buffer;

import com.wyozi.jaysound.Context;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.util.MonofierOutputStream;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by wyozi on 22.12.2015.
 */
public class StaticBuffer extends Buffer {
    protected final int buffer;
    private final byte[] data;

    public StaticBuffer(Decoder decoder) throws IOException {
        this.buffer = AL10.alGenBuffers();

        this.sampleRate = decoder.getSampleRate();
        this.channels = decoder.getChannelCount();

        this.data = decode(decoder);
        Logger.debug("Static buffer data has been decoded. Samplerate: {}, ChannelCount: {}, Size: {}", this.sampleRate, this.channels, this.data.length);

        ByteBuffer bdata = BufferUtils.createByteBuffer(this.data.length);
        bdata.put(this.data);
        bdata.flip();

        AL10.alBufferData(
                buffer,
                channels > 1 ? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                bdata,
                this.sampleRate);

        Context.checkALError();
    }

    private byte[] decode(Decoder decoder) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] data = new byte[1024];
        int read;
        while ((read = decoder.read(data, 0, data.length)) != -1) {
            //Logger.debug("Read {} bytes of data from decoder; writing it to piped output", read);
            baos.write(data, 0, read);
        }

        return baos.toByteArray();
    }

    public int getOpenALId() {
        return this.buffer;
    }
}
