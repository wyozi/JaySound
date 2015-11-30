package com.wyozi.jaysound.decoder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public abstract class Decoder {
    public abstract void readFully(DecoderCallback callback) throws IOException;

    protected void readStreamFully(InputStream in, DecoderCallback callback) throws IOException {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e);
        }
        AudioFormat baseFormat = audioIn.getFormat();

        callback.inform(baseFormat.getChannels(), (int) baseFormat.getSampleRate());

        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false);

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, audioIn);
        byte[] buffer = new byte[4096];
        int read;
        while ((read = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
            callback.writePCM(buffer, 0, read);
        }

        audioInputStream.close();
    }
}
