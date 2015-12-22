package com.wyozi.jaysound.util;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by wyozi on 22.12.2015.
 */
public class MiscUtils {
    public static <T> Set<T> weakSet() {
        return Collections.newSetFromMap(new WeakHashMap<>());
    }
}
