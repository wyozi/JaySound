package com.wyozi.jaysound.efx;

import com.wyozi.jaysound.AudioContext;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an area in the world. An EffectZone has a set of effects that will be applied to all
 * audio sources that are part of the zone.
 *
 * Created by wyozi on 20.12.2015.
 */
public class SoundEnvironment {
    private final Set<Integer> effectSlots = new HashSet<Integer>();
    private boolean sourceConnected = false;

    public void connectALSource(int sourceId) {
        for (int slot : effectSlots) {
            AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, slot, 0, 0);
        }
        sourceConnected = true;
    }

    public void addEffect(Effect effect) {
        if (sourceConnected) {
            throw new RuntimeException("Unable to add effect to zone with a connected sound source. Please create an issue on github (wyozi/JaySound) if this is needed.");
        }

        int slot = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_EFFECT, effect.id);
        effectSlots.add(slot);

        AudioContext.checkALError();

        effect.attached = true;
    }
}
