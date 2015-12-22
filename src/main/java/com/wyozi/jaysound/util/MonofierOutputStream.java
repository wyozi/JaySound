package com.wyozi.jaysound.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * OutputStream that converts short (16- bit) stereo samples to mono samples.
 *
 * Created by wyozi on 22.12.2015.
 */
public class MonofierOutputStream extends OutputStream {
    private final OutputStream out;
    public MonofierOutputStream(OutputStream out) {
        this.out = out;
    }

    private int mark = 0;
    private final int[] byteCache = new int[4];

    @Override
    public void write(int b) throws IOException {
        byteCache[mark] = b;
        if (mark == byteCache.length-1) {
            writeMono();
            mark = 0;
        } else {
            mark++;
        }
    }

    private static final float gain = (float) Math.pow(10.0f, (-6.0f / 20.0f));

    private void writeMono() throws IOException {
        int channel1 = DataConverterUtils.toShort(byteCache[0], byteCache[1]);
        int channel2 = DataConverterUtils.toShort(byteCache[2], byteCache[3]);

        int combined = (int) ((channel1 + channel2) * gain);

        out.write((combined) & 0xFF);
        out.write((combined >> 8) & 0xFF);
    }
}
