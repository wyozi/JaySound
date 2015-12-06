package com.wyozi.jaysound.decoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by wyozi on 6.12.2015.
 */
public class DecoderUtils {
    public static int toShortArray(byte[] sourceBuffer, short[] targetBuffer) {
        ShortBuffer sbuf = ByteBuffer.wrap(sourceBuffer, 0, targetBuffer.length * 2).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        int shortCap = sbuf.capacity();
        sbuf.get(targetBuffer);
        return shortCap;
    }

    public static void toNormalizedFloatArray(short[] sourceBuffer, float[] floatBuffer) {
        for (int i = 0; i < sourceBuffer.length; i++) {
            floatBuffer[i] = ((float) sourceBuffer[i]) / 0x8000;
        }
    }

    public static void toNormalizedFloatArray(byte[] sourceBuffer, float[] floatBuffer) {
        for (int i = 0; i < sourceBuffer.length; i += 2) {
            // Convert 2-byte to a short
            short sample = (short)( ((sourceBuffer[i + 1]&0xFF)<<8) | (sourceBuffer[i]&0xFF) );

            floatBuffer[i/2] = ((float) sample) / 0x8000;
        }
    }
}
