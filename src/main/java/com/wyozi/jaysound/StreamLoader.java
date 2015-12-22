package com.wyozi.jaysound;

import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for getting input stream of audio data from an URL.
 *
 * Created by wyozi on 6.12.2015.
 */
public class StreamLoader {
    static InputStream openSoundStream(URL url) throws IOException {
        // Some streams return ICY header which doesn't work with openStream()
        // in those cases we open separate stream which accepts ICY headers
        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            return connection.getInputStream();
        } catch (IOException ex) {
            return openICYStream(url);
        }
    }

    private static String readLine(InputStream in) throws IOException {
        int ch = in.read();

        StringBuilder builder = new StringBuilder();
        while (ch != '\n' && ch != '\r' && ch != -1) {
            builder.append((char) ch);
            ch = in.read();
        }

        if (ch == '\n' || ch == '\r')
            //noinspection ResultOfMethodCallIgnored
            in.read();

        return builder.toString();
    }

    private static final Pattern HEADER_PATTERN = Pattern.compile("(.*):(.*)");
    private static InputStream openICYStream(URL url) throws IOException {
        int port = url.getPort();
        if (port == -1) port = 80;

        Socket sock = new Socket(url.getHost(), port);

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

        String crlf = "\r\n";
        pw.print("GET " + url.getPath() + " HTTP/1.0 " + crlf);
        pw.print("Icy-MetaData: 1" + crlf);
        pw.print(crlf);
        pw.flush();

        InputStream in = sock.getInputStream();

        Map<String, String> headers = new HashMap<>();

        String line;
        while (!(line = readLine(in)).isEmpty()) {
            Logger.debug("Received ICY response header: " + line);

            Matcher matcher = HEADER_PATTERN.matcher(line);
            if (matcher.find()) {
                String headerKey = matcher.group(1).trim();
                String headerValue = matcher.group(2).trim();

                headers.put(headerKey, headerValue);
            }
        }

        int metaDataOccurrence = Integer.parseInt(headers.getOrDefault("icy-metaint", "-1"));

        // Now we should get some juicy sound data; pass stream to whoever wants it
        if (metaDataOccurrence != -1) {
            Logger.debug("Creating ICY input stream (metadata comes every {} bytes)", metaDataOccurrence);
            ICYInputStream icyInputStream = new ICYInputStream(in, metaDataOccurrence);

            icyInputStream.streamName = headers.get("icy-name");
            icyInputStream.streamGenre = headers.get("icy-genre");
            icyInputStream.streamUrl = headers.get("icy-url");

            return icyInputStream;
        }

        return in;
    }

    public static class ICYInputStream extends InputStream {
        private final InputStream in;
        private final int metaDataOccurrence;

        private ICYInputStream(InputStream in, int metaDataOccurrence) {
            this.in = in;
            this.metaDataOccurrence = metaDataOccurrence;
        }

        private int audioDataRead = 0;
        private final byte[] metadata = new byte[16 * 255];

        @Override
        public int read() throws IOException {
            if (audioDataRead == metaDataOccurrence) {
                readMetadata();
                audioDataRead = 0;
            }
            try {
                return in.read();
            } finally {
                audioDataRead += 1;
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int bytesTillMetadata = metaDataOccurrence - audioDataRead;
            if (len < bytesTillMetadata) {
                int actualRead = in.read(b, off, len);
                audioDataRead += actualRead;
                return actualRead;
            } else {
                // read from offset to metadata start
                int actualRead = 0;
                if (bytesTillMetadata > 0) {
                    actualRead = in.read(b, off, bytesTillMetadata);
                    audioDataRead += actualRead;
                }

                // we failed to read until even metadata. Disappointing; disown byte array by returning early.
                if (actualRead != bytesTillMetadata) return actualRead;

                this.readMetadata();
                audioDataRead = 0;

                // read the rest recursively because we can't know if the length is STILL enough to skip metadatas
                int toReadAfter = len - bytesTillMetadata;
                return actualRead + (toReadAfter > 0 ? read(b, off + bytesTillMetadata, toReadAfter) : 0);
            }
        }

        private void readMetadata() throws IOException {
            int metadataLength = in.read() * 16;
            if (metadataLength > 0) {
                Logger.debug("Reading {} bytes of ICY stream metadata", metadataLength);

                in.read(metadata, 0, metadataLength);
                processMetadata(new String(metadata, "ASCII"));
            }
        }

        private String streamName;
        private String streamGenre;
        private String streamUrl;
        private String streamTitle;

        private static final Pattern TITLE_PATTERN = Pattern.compile("StreamTitle='(.*?)'");
        public void processMetadata(String metadata) {
            Logger.debug("Received ICY metadata: {}", metadata);

            Matcher matcher = TITLE_PATTERN.matcher(metadata);
            if (matcher.find()) {
                streamTitle = matcher.group(1);
                Logger.debug("Parsed ICY stream title: {}", streamTitle);
            }
        }

        public String getStreamName() {
            return streamName;
        }

        public String getStreamGenre() {
            return streamGenre;
        }

        public String getStreamUrl() {
            return streamUrl;
        }

        public String getStreamTitle() {
            return streamTitle;
        }
    }
}
