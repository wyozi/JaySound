package com.wyozi.jaysound;

import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

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
    private static InputStream openICYStream(URL url) throws IOException {
        int port = url.getPort();
        if (port == -1) port = 80;

        Socket sock = new Socket(url.getHost(), port);

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

        String crlf = "\r\n";
        pw.print("GET " + url.getPath() + " HTTP/1.0 " + crlf);
        pw.print("Icy-MetaData: 0 " + crlf);
        pw.print(crlf);
        pw.flush();

        InputStream in = sock.getInputStream();
        BufferedReader bis = new BufferedReader(new InputStreamReader(in));

        // Read all stupid headers
        String line;
        while (!(line = bis.readLine()).equals("")) {
            Logger.debug("Received ICY header: " + line);
        }

        // Now we should get some juicy sound data; pass stream to whoever wants it
        return in;
    }
}
