package com.wyozi.jaysound.buffer;

import com.wyozi.jaysound.decoder.Decoder;
import com.wyozi.jaysound.util.MonofierOutputStream;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wyozi on 22.12.2015.
 */
public abstract class Buffer {
    /**
     * The decoder to be used for decoding data to be uploaded to the buffers.
     */
    private final Decoder decoder;

    /**
     * Whether audio should be automatically converted to mono.
     */
    private final boolean convertToMono;

    /**
     * The sample rate of the sound.
     *
     * Note: not necessarily the actual sample rate of the stream but the sample rate at which decoder MUST feed data to openal.
     */
    protected final int sampleRate;

    /**
     * The amount of channels.
     *
     * Note: not necessarily the actual channel count of the stream but the channel count which buffer operates on.
     */
    protected final int channels;

    /**
     * @see Buffer#getDecodedBytes()
     */
    private long decodedBytes = 0;

    public Buffer(Decoder decoder, boolean convertToMono) {
        this.decoder = decoder;
        this.convertToMono = convertToMono;

        this.sampleRate = decoder.getSampleRate();
        this.channels = convertToMono ? 1 : decoder.getChannelCount();

        Logger.debug("Buffer {} initialized with SampleRate {}, ChannelCount {}, ConvertToMono", this, sampleRate, channels, convertToMono);
    }

    /**
     * @return the amount of bytes that have been decoded by the streambuffer worker thread.
     */
    public long getDecodedBytes() {
        return decodedBytes;
    }

    protected void readFully(OutputStream iout, int bufferSize) throws IOException {
        OutputStream out = (convertToMono) ? new MonofierOutputStream(iout) : iout;

        byte[] data = new byte[bufferSize];
        int read;
        while ((read = decoder.read(data, 0, data.length)) != -1) {
            Logger.trace("Read {} bytes of data from decoder; writing it to piped output", read);
            decodedBytes += read;
            out.write(data, 0, read);
        }
    }

    /**
     * @see Decoder#getDecodedTitle()
     */
    public String getDecodedTitle() {
        return decoder.getDecodedTitle();
    }

    public void update() { }

    public abstract byte[] getBufferData(int openalId);

    public int getChannelCount() {
        return channels;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    private boolean disposed = false;
    protected abstract void disposeBuffers();

    public final void dispose() {
        disposeBuffers();
        this.disposed = true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (!disposed) {
            dispose();
        }
    }
}
