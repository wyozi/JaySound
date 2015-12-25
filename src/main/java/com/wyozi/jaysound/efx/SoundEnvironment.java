
package com.wyozi.jaysound.efx;

import com.wyozi.jaysound.AudioContext;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a set of sounds that have common set of effects applied to them. Usually this could be an area in the world.
 *
 * Created by wyozi on 20.12.2015.
 */
public class SoundEnvironment {
    // Only queried when first sound environment is created
    private static int MAX_SOURCE_SENDS = -1;

    private final Set<Integer> connectedSources = new HashSet<>();
    private final List<Integer> effectSlots = new ArrayList<>();
    private Filter filter;

    public SoundEnvironment() {
        if (MAX_SOURCE_SENDS == -1) {
            MAX_SOURCE_SENDS = ALC10.alcGetInteger(ALC10.alcGetCurrentContext(), EXTEfx.ALC_MAX_AUXILIARY_SENDS);
            Logger.debug("EFX max auxiliary sends: {}", MAX_SOURCE_SENDS);
        }
    }

    public void connectALSource(int sourceId) {
        for (int i = 0; i < effectSlots.size(); i++) {
            AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, effectSlots.get(i), i, (filter != null) ? filter.getId() : EXTEfx.AL_FILTER_NULL);
        }
        connectedSources.add(sourceId);
    }

    public void disconnectAllSources() {
        for (int sourceId: connectedSources) {
            for (int i = 0; i < effectSlots.size(); i++) {
                AL11.alSource3i(sourceId, EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, i, EXTEfx.AL_FILTER_NULL);
            }
        }
        connectedSources.clear();
    }

    public void addEffect(Effect effect) {
        if (connectedSources.size() > 0) {
            throw new RuntimeException("Unable to add effect to zone with a connected sound source. Please create an issue on github (wyozi/JaySound) if this is needed.");
        }
        if (effectSlots.size() >= MAX_SOURCE_SENDS) {
            throw new RuntimeException("Attempting to add too many effects to a single sound environment (current=" + effectSlots.size() + "; max=" + MAX_SOURCE_SENDS + ") " +
                    "(note: it is possible to ask for more slots in alcCreateContext)");
        }

        int slot = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(slot, EXTEfx.AL_EFFECTSLOT_EFFECT, effect.id);
        effectSlots.add(slot);

        AudioContext.checkALError();

        effect.attached = true;
    }

    public void setFilter(Filter filter) {
        if (connectedSources.size() > 0) {
            throw new RuntimeException("Unable to set filter of zone with a connected sound source. Please create an issue on github (wyozi/JaySound) if this is needed.");
        }

        this.filter = filter;
    }
}
