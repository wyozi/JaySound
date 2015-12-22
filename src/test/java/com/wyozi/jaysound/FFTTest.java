package com.wyozi.jaysound;

import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.GenericJavaDecoder;
import com.wyozi.jaysound.player.FFTVisualizer;
import ddf.minim.analysis.FFT;
import ddf.minim.analysis.HammingWindow;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by wyozi on 5.12.2015.
 */
public class FFTTest {

    private final Decoder decoder;
    private final FFT fft;
    private final SourceDataLine line;
    private final FFTVisualizer visualizer;

    public FFTTest() throws IOException, LineUnavailableException {
        decoder = new GenericJavaDecoder(StreamLoader.openSoundStream(new URL("http://stream.plusfm.net/")));
        //decoder = new GenericJavaDecoder(Mp3DecoderTest.class.getResourceAsStream("/higher.mp3"));

        int channelCount = 1;
        final AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, decoder.getSampleRate(), 16, channelCount, channelCount * 2, decoder.getSampleRate(), false);
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

        fft = new FFT(512, decoder.getSampleRate());
        fft.window(new HammingWindow());

        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(outFormat);

        visualizer = new FFTVisualizer();
    }

    private void loop() throws IOException, InterruptedException {
        float[] finalFloatBuffer = new float[512];

        int finalFloatsRead;
        while ((finalFloatsRead = decoder.readNormalizedFloatsMono(finalFloatBuffer)) != -1) {
            byte[] bytez = decoder.getTmpByteBuffer();
            int bytezLength = 1024;

            while (line.available() < bytezLength) {
                Thread.sleep(1);
            }
            line.write(bytez, 0, bytezLength);
            if (!line.isActive())
                line.start();

            fft.forward(finalFloatBuffer);

            visualizer.updateFFT(fft);
        }
        line.drain();
        line.stop();
    }

    public static void main(String[] args) throws IOException, LineUnavailableException, InterruptedException {
        new FFTTest().loop();
    }
}
