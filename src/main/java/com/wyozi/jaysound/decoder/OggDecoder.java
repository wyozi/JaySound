package com.wyozi.jaysound.decoder;

import javazoom.spi.vorbis.sampled.convert.DecodedVorbisAudioInputStream;
import javazoom.spi.vorbis.sampled.file.VorbisAudioFileReader;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
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

    @Override
    protected AudioInputStream createAudioInputStream() throws IOException, UnsupportedAudioFileException {
        return new VorbisAudioFileReader().getAudioInputStream(in);
    }
}
