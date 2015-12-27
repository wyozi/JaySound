package com.wyozi.jaysound;

import com.wyozi.jaysound.adapter.JayVec3f;
import com.wyozi.jaysound.buffer.Buffer;
import com.wyozi.jaysound.buffer.StaticBuffer;
import com.wyozi.jaysound.buffer.StreamBuffer;
import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.decoder.MP3Decoder;
import com.wyozi.jaysound.decoder.OggDecoder;
import com.wyozi.jaysound.efx.SoundEnvironment;
import com.wyozi.jaysound.sound.BufferedSound;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.sound.StreamingSound;
import com.wyozi.jaysound.util.EFXUtil;
import com.wyozi.jaysound.util.MiscUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.Set;

/**
 * @author Wyozi
 * @since 2.8.2015
 */
public class AudioContext {
    private final ALContext ctx;

    public static void checkALError() {
        int err = AL10.alGetError();
        if (err != AL10.AL_NO_ERROR)
            throw new RuntimeException("OpenAL Error: " + err + " (" + AL10.alGetString(err) + ")");
    }

    public AudioContext() {
        ctx = ALContext.create();

        Logger.debug("OpenAL Version: {}", AL10.alGetString(AL10.AL_VERSION));

        Logger.debug("Using device '{}'", ALC10.alcGetString(ctx.address(), ALC10.ALC_DEFAULT_DEVICE_SPECIFIER));
        Logger.debug("Available devices: '{}'", ALC10.alcGetString(ctx.address(), ALC10.ALC_DEVICE_SPECIFIER));
        Logger.debug("Available extensions: '{}'", ALC10.alcGetString(ctx.address(), ALC10.ALC_EXTENSIONS));

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

    private Set<Buffer> buffers = MiscUtils.weakSet();
    private Set<Sound> sounds = MiscUtils.weakSet();

    private SoundEnvironment globalSoundEnvironment;

    /**
     * Sets the global sound environment. These effects are immediately applied to <b>all</b> sounds currently in the world
     * and added afterwards.
     * <p/>
     * Please use caution with this method; it is often advisable to add individual sounds to the sound
     * environment instead.
     *
     * @param zone
     */
    public void setGlobalSoundEnvironment(SoundEnvironment zone) {
        if (this.globalSoundEnvironment != null) {
            this.globalSoundEnvironment.disconnectAllSources();
        }

        this.globalSoundEnvironment = zone;

        if (this.globalSoundEnvironment != null) {
            for (Sound sound : sounds) {
                sound.connectToEnvironment(this.globalSoundEnvironment);
            }
        }
    }

    /**
     * Some sounds require some plumbing to be done in the main OpenAL thread. This method does the plumbing.
     * <p/>
     * This method should be called a few times a second, but there are no specific timing needs.
     */
    public void update() {
        buffers.forEach(Buffer::update);
        sounds.forEach(Sound::update);
    }

    /**
     * Sets listener position to given coordinates.
     */
    public void setListenerPosition(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_POSITION, x, y, z);
    }

    private FloatBuffer listenerOri = (FloatBuffer) BufferUtils.createFloatBuffer(6);

    /**
     * Sets listener orientation (aka. view direction) to given coordinates
     */
    public void setListenerOrientation(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        listenerOri.put(new float[]{dirX, dirY, dirZ, upX, upY, upZ});
        listenerOri.flip();
        AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
    }

    /**
     * Sets listener orientation (aka. view direction) to given coordinates
     */
    public void setListenerOrientation(float dirX, float dirY, float dirZ) {
        setListenerOrientation(dirX, dirY, dirZ, 0f, 1f, 0f);
    }

    /**
     * Sets listener velocity.
     */
    public void setListenerVelocity(float x, float y, float z) {
        AL10.alListener3f(AL10.AL_VELOCITY, x, y, z);
    }

    /**
     * Updates listener location in the world. Direction and velocity can be null.
     * @param pos position in the world
     * @param direction view direction
     * @param velocity velocity in the world
     */
    public void updateListener(JayVec3f pos, JayVec3f direction, JayVec3f velocity) {
        setListenerPosition(pos.getJayX(), pos.getJayY(), pos.getJayZ());
        if (direction != null) setListenerOrientation(direction.getJayX(), direction.getJayY(), direction.getJayZ());
        if (velocity != null) setListenerVelocity(velocity.getJayX(), velocity.getJayY(), velocity.getJayZ());
    }

    /**
     * Disposes all sounds and buffers created through this context and finally destroys the context itself.
     */
    public void dispose() {
        buffers.forEach(Buffer::dispose);
        sounds.forEach(Sound::dispose);

        ctx.destroy();
    }

    private Decoder inferDecoder(URL url) throws IOException {
        InputStream stream = StreamLoader.openSoundStream(url);

        if (url.getFile().endsWith(".ogg"))
            return new OggDecoder(stream);
        return new MP3Decoder(stream);
    }

    protected void onNewSoundCreated(Sound sound) {
        if (this.globalSoundEnvironment != null)
            sound.connectToEnvironment(this.globalSoundEnvironment);

        Buffer buffer = sound.getBuffer();
        if (!buffers.contains(buffer)) {
            Logger.debug("Rogue buffer found: {} has been added to audiocontext buffers via new sound", buffer);
            buffers.add(buffer);
        }

        sounds.add(sound);
    }

    public StaticBuffer createStaticBuffer(URL url) throws IOException {
        StaticBuffer buffer = new StaticBuffer(inferDecoder(url), true);
        buffers.add(buffer);
        return buffer;
    }

    public StreamBuffer createStreamBuffer(URL url) throws IOException {
        StreamBuffer buffer = new StreamBuffer(inferDecoder(url), true);
        buffers.add(buffer);
        return buffer;
    }

    public BufferedSound createBufferedSound(StaticBuffer staticBuffer) {
        BufferedSound sound = new BufferedSound(staticBuffer);
        onNewSoundCreated(sound);
        return sound;
    }

    public BufferedSound createBufferedSound(URL url) throws IOException {
        return createBufferedSound(createStaticBuffer(url));
    }

    public StreamingSound createStreamingSound(StreamBuffer buffer) throws IOException {
        StreamingSound sound = new StreamingSound(buffer);
        onNewSoundCreated(sound);
        return sound;
    }

    public StreamingSound createStreamingSound(URL url) throws IOException {
        return createStreamingSound(createStreamBuffer(url));
    }
}