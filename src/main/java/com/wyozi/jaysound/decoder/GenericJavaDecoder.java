package com.wyozi.jaysound.decoder;

import com.wyozi.jaysound.StreamLoader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wyozi on 6.12.2015.
 */
public class GenericJavaDecoder extends Decoder {
    protected AudioFormat baseFormat;
    protected AudioFormat outFormat;
    protected AudioInputStream audioIn;

    public GenericJavaDecoder(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected void internalInit() throws IOException {
        AudioInputStream audioIn;
        try {
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(in));
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e);
        }

        this.baseFormat = audioIn.getFormat();

        this.channelCount = this.baseFormat.getChannels();
        this.sampleRate = (int) this.baseFormat.getSampleRate();

        this.createOutFormat();

        this.audioIn = AudioSystem.getAudioInputStream(this.outFormat, audioIn);
    }

    private void createOutFormat() {
        int sampleRate = this.sampleRate;
        int channelCount = this.channelCount;

        this.outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sampleRate,
                16,
                channelCount,
                channelCount * 2,
                sampleRate,
                false);
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        return this.audioIn.read(buffer, offset, length);
    }
}
