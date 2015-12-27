package com.wyozi.jaysound.util;

import org.lwjgl.openal.ALC;

/**
 * Created by wyozi on 27.12.2015.
 */
public class EFXUtil {
    public static boolean isEfxSupported() {
        return ALC.getCapabilities().ALC_EXT_EFX;
    }
}
