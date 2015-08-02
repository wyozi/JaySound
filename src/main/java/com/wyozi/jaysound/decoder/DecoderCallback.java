package com.wyozi.jaysound.decoder;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public interface DecoderCallback {
    void writePCM(byte[] data, int offset, int length);
    void inform(int channels, int rate);
}
