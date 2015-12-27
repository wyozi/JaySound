package com.wyozi.jaysound;

import com.wyozi.jaysound.sound.StreamingSound;
import org.pmw.tinylog.Configurator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wyozi on 27.12.2015.
 */
public abstract class JaySoundTest {
    public static void enableVerboseLogging() {
        Configurator.defaultConfig()
                .formatPattern("{level}: {class}.{method}()\\t{message}")
                .level(org.pmw.tinylog.Level.DEBUG)
                .activate();
    }

    private static URL createURL(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final URL TEST_INTERNET_RADIO_URL = createURL("http://stream.plusfm.net/;");
    public static final URL TEST_INTERNET_AUDIO_URL = createURL("https://upload.wikimedia.org/wikipedia/en/3/3d/Sample_of_Daft_Punk's_Da_Funk.ogg");
}
