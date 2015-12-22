package com.wyozi.jaysound;

import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.buffer.StaticBuffer;
import com.wyozi.jaysound.buffer.StreamBuffer;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.decoder.OggDecoder;
import com.wyozi.jaysound.efx.EffectZone;
import com.wyozi.jaysound.sound.BufferedSound;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.sound.StreamingSound;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class AudioContext {
    private final ALContext ctx;

    public static void checkALError() {
        int err = AL10.alGetError();
        if (err != AL10.AL_NO_ERROR) throw new RuntimeException("OpenAL Error: " + err);
    }
    public AudioContext() {
        ctx = ALContext.create();

        Logger.debug("OpenAL Version: {}", AL10.alGetString(AL10.AL_VERSION));

        Logger.debug("Using device '{}'", ALC10.alcGetString(ctx.getPointer(), ALC10.ALC_DEFAULT_DEVICE_SPECIFIER));
        Logger.debug("Available devices: '{}'", ALC10.alcGetString(ctx.getPointer(), ALC10.ALC_DEVICE_SPECIFIER));
        Logger.debug("Available extensions: '{}'", ALC10.alcGetString(ctx.getPointer(), ALC10.ALC_EXTENSIONS));

        Logger.debug("EFX Support: {}", EFXUtil.isEfxSupported());

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

        AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);
    }

    private List<Sound> sounds = new ArrayList<>();

    private EffectZone globalEffectZone;

    public void setGlobalEffectZone(EffectZone zone) {
        this.globalEffectZone = zone;

        for (Sound sound : sounds) {
            sound.connectToEffectZone(this.globalEffectZone);
        }
    }

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
        AL10.alListener3f(AL10.AL_POSITION, pos.getJayX(), pos.getJayY(), pos.getJayZ());
        if (dir != null) {
            listenerOri.put(new float[] {dir.getJayX(), dir.getJayY(), dir.getJayZ(), 0.0f, 1.0f, 0.0f});
            listenerOri.flip();
            AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
        }
        if (velocity != null) {
            AL10.alListener3f(AL10.AL_VELOCITY, velocity.getJayX(), velocity.getJayY(), velocity.getJayZ());
        }
    }

    public void dispose() {
        ctx.destroy();
    }

    private Decoder getDecoder(URL url, InputStream stream) throws IOException {
        if (url.getFile().endsWith(".ogg"))
            return new OggDecoder(stream);
        return new MP3Decoder(stream);
    }

    protected void onNewSoundCreated(Sound sound) {
        if (this.globalEffectZone != null)
            sound.connectToEffectZone(this.globalEffectZone);

        sounds.add(sound);
    }

    public Sound createBufferedSound(URL url) throws IOException {
        BufferedSound sound = new BufferedSound(new StaticBuffer(getDecoder(url, StreamLoader.openSoundStream(url)), true));

        onNewSoundCreated(sound);

        return sound;
    }

    public StreamingSound createStreamingSound(URL url) throws IOException {
        StreamingSound sound = new StreamingSound(new StreamBuffer(getDecoder(url, StreamLoader.openSoundStream(url)), true));

        onNewSoundCreated(sound);

        return sound;
    }
}
