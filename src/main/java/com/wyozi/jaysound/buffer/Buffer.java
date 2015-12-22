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
    private final Decoder decoder;
    private final boolean convertToMono;
    protected final int sampleRate;
    protected final int channels;

    public Buffer(Decoder decoder, boolean convertToMono) {
        this.decoder = decoder;
        this.convertToMono = convertToMono;

        this.sampleRate = decoder.getSampleRate();
        this.channels = convertToMono ? 1 : decoder.getChannelCount();

        Logger.debug("Buffer {} initialized with SampleRate {}, ChannelCount {}, ConvertToMono", this, sampleRate, channels, convertToMono);
    }

    protected void readFully(OutputStream iout, int bufferSize) throws IOException {
        OutputStream out = (convertToMono) ? new MonofierOutputStream(iout) : iout;

        byte[] data = new byte[bufferSize];
        int read;
        while ((read = decoder.read(data, 0, data.length)) != -1) {
            //Logger.debug("Read {} bytes of data from decoder; writing it to piped output", read);
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
