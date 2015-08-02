package com.wyozi.jaysound;

import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.decoder.OggDecoder;
import com.wyozi.jaysound.sound.BufferedSound;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.sound.StreamingSound;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class Context {
    private final ALContext ctx;

    public static void checkALError() {
        int err = AL10.alGetError();
        if (err != AL10.AL_NO_ERROR) throw new RuntimeException("OpenAL Error: " + err);
    }
    public Context() {
        ctx = ALContext.create();

        Logger.info("Using device '{}'", ALC10.alcGetString(ctx.getPointer(), ALC10.ALC_DEFAULT_DEVICE_SPECIFIER));
        Logger.info("Available devices: '{}'", ALC10.alcGetString(ctx.getPointer(), ALC10.ALC_DEVICE_SPECIFIER));
        Logger.info("Available extensions: '{}'", ALC10.alcGetString(ctx.getPointer(), ALC10.ALC_EXTENSIONS));

        /*ALCapabilities caps = ctx.getCapabilities();
        System.out.println("OpenAL Capabilities:");
        for (Field f : caps.getClass().getDeclaredFields()) {
            if (!Modifier.isPublic(f.getModifiers())) continue;

            f.setAccessible(true);
            try {
                System.out.println(f.getName() + " = " + f.get(caps));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/

        checkALError();

        AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE);
    }

    private List<Sound> sounds = new ArrayList<>();

    /**
     * Streaming sounds require some plumbing to be done in the main OpenAL thread. Call this method to do the plumbing.
     *
     * Not needed for buffered sounds.
     */
    public void update() {
        for (Sound sound : sounds) {
            sound.update();
        }
    }

    private FloatBuffer listenerOri = (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
    public void updateListener(JayVec3f pos, JayVec3f dir, JayVec3f velocity) {
        AL10.alListener3f(AL10.AL_POSITION, pos.getX(), pos.getY(), pos.getZ());
        if (dir != null) {
            listenerOri.put(new float[] {dir.getX(), dir.getY(), dir.getZ(), 0.0f, 1.0f, 0.0f});
            listenerOri.flip();
            AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
        }
        if (velocity != null) {
            AL10.alListener3f(AL10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
        }
    }

    public void dispose() {
        ctx.destroy();
    }

    private Decoder getDecoder(URL url) throws IOException {
        if (url.getFile().endsWith(".ogg"))
            return new OggDecoder(url.openStream());
        return new MP3Decoder(url.openStream());
    }

    public Sound createBufferedSound(URL url) throws IOException {
        BufferedSound sound = new BufferedSound();
        sound.load(getDecoder(url));

        sounds.add(sound);
        return sound;
    }
    public Sound createStreamingSound(URL url) throws IOException {
        StreamingSound sound = new StreamingSound();
        sound.load(getDecoder(url));

        sounds.add(sound);
        return sound;
    }

    private static class ThrowawayVec3f implements JayVec3f {

        private final float x, y, z;

        private ThrowawayVec3f(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }

        @Override
        public float getZ() {
            return z;
        }
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        Context ss = new Context();
        ss.updateListener(new ThrowawayVec3f(0, 0, 0), new ThrowawayVec3f(0, 0, -1), new ThrowawayVec3f(0, 0, 0));

        Sound handle = ss.createStreamingSound(new URL("http://198.100.147.142:80/"));
        handle.play();

        for (int i = 0;i < 50000; i++) {
            float x = (float) (Math.cos(i / 60f)*1.5f);
            float z = (float) (Math.sin(i/60f)*1.5f);
            handle.setPos(new ThrowawayVec3f(x, 0, z));
            //ss.updateListener(new Vec3f(x, 0, z), new Vec3f(0, 0, 1), null);
            //System.out.println(x + " x " + z);

            ss.update();
            Thread.sleep(20);
        }

        handle.dispose();
        ss.dispose();
    }
}
