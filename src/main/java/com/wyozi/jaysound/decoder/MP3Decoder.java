package com.wyozi.jaysound.decoder;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class MP3Decoder extends GenericJavaDecoder {
    public MP3Decoder(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected AudioInputStream createAudioInputStream() throws IOException, UnsupportedAudioFileException {
        return new MpegAudioFileReader().getAudioInputStream(in);
    }
}
