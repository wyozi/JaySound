package com.wyozi.jaysound;

import com.wyozi.jaysound.buffer.StreamBuffer;
import com.wyozi.jaysound.decoder.GenericJavaDecoder;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.sound.Sound;

import java.io.*;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * This file tests how sound handles stream buffer being underflown (playback advancing faster than data)
 *
 * Created by wyozi on 27.12.2015.
 */
public class StreamUnderflowTest extends JaySoundTest {
    private static void feed(InputStream in, OutputStream out, int count) throws IOException {
        byte[] b = new byte[count];
        int read = in.read(b);
        System.out.println("(req " + count + "; read " + read + ") writing to out");
        out.write(b);
        System.out.println("written!");
    }
    private static void feedAsync(final InputStream in, final OutputStream out, int count) {
        new Thread(() -> { try { feed(in, out, count); } catch (IOException e) { e.printStackTrace(); } }).start();
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        enableVerboseLogging();

        AudioContext ctx = new AudioContext();

        InputStream urlStream = StreamLoader.openSoundStream(new File("F:/Downloads/paintings.mp3").toURI().toURL());

        PipedOutputStream outPipe = new PipedOutputStream();
        PipedInputStream inPipe = new PipedInputStream();
        outPipe.connect(inPipe);

        // Feed the initial mp3 data (otherwise MP3Decoder#internalInit will block forever)
        feedAsync(urlStream, outPipe, 1024 * 40);
        StreamBuffer buffer = new StreamBuffer(new GenericJavaDecoder(inPipe), false);

        System.out.println("stream buffer initialized!");

        Sound sound = ctx.createStreamingSound(buffer);
        sound.play();

        // First feed ~4 sec of stereo data to the stream
        feedAsync(urlStream, outPipe, 44000 * 2 * 4);

        // Sleep for 8 seconds
        for (int i = 0;i < 80; i++) {
            ctx.update();
            Thread.sleep(100);
        }

        // queue for 5 more seconds
        feedAsync(urlStream, outPipe, 44000 * 2 * 5);

        // sleep for 8 seconds
        for (int i = 0;i < 80; i++) {
            ctx.update();
            Thread.sleep(100);
        }
    }
}
