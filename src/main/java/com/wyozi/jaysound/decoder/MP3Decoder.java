package com.wyozi.jaysound.decoder;

import com.wyozi.jaysound.player.PCMPlayer;

import javax.sound.sampled.*;
import java.io.*;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class MP3Decoder extends Decoder {
    private final InputStream in;

    public MP3Decoder(InputStream in) {
        this.in = in;
    }

    @Override
    public void readFully(DecoderCallback callback) throws IOException {
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(this.in));
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
