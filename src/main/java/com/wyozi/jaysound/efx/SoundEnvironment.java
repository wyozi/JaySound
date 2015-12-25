
package com.wyozi.jaysound.efx;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.sound.Sound;
import com.wyozi.jaysound.util.MiscUtils;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
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

    private final Set<Sound> connectedSources = MiscUtils.weakSet();
    private final List<EffectSlot> effectSlots = new ArrayList<>();
    private Filter auxSendFilter;

    public SoundEnvironment() {
        if (MAX_SOURCE_SENDS == -1) {
            MAX_SOURCE_SENDS = ALC10.alcGetInteger(ALC10.alcGetCurrentContext(), EXTEfx.ALC_MAX_AUXILIARY_SENDS);
            Logger.debug("EFX max auxiliary sends: {}", MAX_SOURCE_SENDS);
        }
    }

    public void connectSound(Sound sound) {
        final int filterId = (auxSendFilter != null) ? auxSendFilter.getId() : EXTEfx.AL_FILTER_NULL;
        Logger.debug("Connecting sound environment to {} using auxSendFilter id {}", sound, filterId);

        for (int i = 0; i < effectSlots.size(); i++) {
            EffectSlot slot = effectSlots.get(i);
            if (slot == null) continue;

            AL11.alSource3i(sound.getOpenALSourceId(), EXTEfx.AL_AUXILIARY_SEND_FILTER, slot.slotId, i, filterId);
        }
        connectedSources.add(sound);
    }

    public void disconnectAllSources() {
        for (Sound source: connectedSources) {
            for (int i = 0; i < effectSlots.size(); i++) {
                AL11.alSource3i(source.getOpenALSourceId(), EXTEfx.AL_AUXILIARY_SEND_FILTER, EXTEfx.AL_EFFECTSLOT_NULL, i, EXTEfx.AL_FILTER_NULL);
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
        effectSlots.add(new EffectSlot(slot, effect));

        AudioContext.checkALError();

        effect.attached = true;
    }

    /**
     * Sets the filter to be used in between audio source and the effect.
     *
     * Note: the audible effect of this filter can be very subtle.
     * Use direct filter ({@link Sound#setDirectFilter(Filter)} to affect the sound directly.
     *
     * @param filter
     */
    public void setAuxSendFilter(Filter filter) {
        if (connectedSources.size() > 0) {
            throw new RuntimeException("Unable to set auxSendFilter of zone with a connected sound source. Please create an issue on github (wyozi/JaySound) if this is needed.");
        }

        this.auxSendFilter = filter;
    }

    private class EffectSlot {
        public final int slotId;
        public final Effect effect;

        private EffectSlot(int slot, Effect effect) {
            this.slotId = slot;
            this.effect = effect;
        }
    }
}
