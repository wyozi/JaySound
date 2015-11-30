package com.wyozi.jaysound.player;

import com.wyozi.jaysound.decoder.DecoderCallback;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Basic PCM sound player using Java runtime (no LWJGL required)
 *
 * @author Wyozi
 * @since 2.8.2015
 */
public class PCMPlayer implements DecoderCallback {
    private int rate, channels;

    private AudioFormat getOutFormat() {
        final int ch = this.channels;
        final float rate = this.rate;
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private final Lock dataLineLock = new ReentrantLock();
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private SourceDataLine line;
    private volatile boolean playing = false;

    public void play() throws LineUnavailableException {
        final AudioFormat outFormat = getOutFormat();
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

        dataLineLock.lock();

        line = (SourceDataLine) AudioSystem.getLine(info);
        if (line != null) {
            line.open(outFormat);
            line.start();

            byte[] b = baos.toByteArray();
            line.write(b, 0, b.length);

            line.drain();
            line.stop();
        }

        this.playing = true;
        dataLineLock.unlock();
    }

    @Override
    public void writePCM(byte[] data, int offset, int length) {
        dataLineLock.lock();

        if (line != null)
            line.write(data, offset, length);
        else
            baos.write(data, offset, length);

        dataLineLock.unlock();
    }

    @Override
    public void inform(int channels, int rate) {
        this.channels = channels;
        this.rate = rate;
    }
}
