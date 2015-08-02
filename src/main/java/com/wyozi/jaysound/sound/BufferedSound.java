package com.wyozi.jaysound.sound;

import com.wyozi.jaysound.Context;
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
    private final int buffer;

    public BufferedSound() {
        super();

        this.buffer = AL10.alGenBuffers();
        Context.checkALError();
    }

    private int rate, channels;

    public void load(Decoder decoder) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        decoder.readFully(new DecoderCallback() {
            @Override
            public void writePCM(byte[] data, int offset, int length) {
                baos.write(data, offset, length);
            }

            @Override
            public void inform(int channels, int rate) {
                BufferedSound.this.rate = rate;
                BufferedSound.this.channels = channels;
            }
        });

        byte[] data = baos.toByteArray();
        ByteBuffer bdata = ByteBuffer.allocateDirect(data.length);
        bdata.put(data);
        bdata.rewind();

        AL10.alBufferData(
                buffer,
                channels>1?
                        AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16,
                bdata,
                rate);
        Context.checkALError();

        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);

        AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 1f);
        AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, 1000f);
        AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, 1f); // exponential

        AL10.alSourcef(source, AL10.AL_PITCH, 1.0f);
        AL10.alSourcef(source, AL10.AL_GAIN, 1.0f);
        Context.checkALError();
    }

    @Override
    public void dispose() {
        super.dispose();

        AL10.alDeleteBuffers(buffer);
        Context.checkALError();
    }
}
