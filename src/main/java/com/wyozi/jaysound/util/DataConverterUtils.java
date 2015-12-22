package com.wyozi.jaysound.util;

/**
 * Created by wyozi on 22.12.2015.
 */
public class DataConverterUtils {
    /**
     * Converts given bytes to a little-endian short.
     * @param byte0
     * @param byte1
     * @return
     */
    public static short toShort(int byte0, int byte1) {
        return (short)( ((byte1&0xFF)<<8) | (byte0&0xFF) );
    }

    public static float toNormalizedFloat(short s) {
        return ((float) s) / 0x8000;
    }
}
