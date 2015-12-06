package com.wyozi.jaysound.decoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class OggDecoder extends GenericJavaDecoder {
    public OggDecoder(InputStream in) throws IOException {
        super(in);
    }
}
