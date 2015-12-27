package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.AudioContext;
import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.EXTEfx;

/**
 * Created by wyozi on 20.12.2015.
 */
public abstract class Reverb extends Effect {
    private Reverb(int reverbEffectId, EFXPreset preset) {
        super(reverbEffectId);
        this.applyPreset(preset);
    }

    protected void applyPreset(EFXPreset preset) {
        this.setDensity(preset.flDensity);
        this.setDiffusion(preset.flDiffusion);
        this.setGain(preset.flGain);
        this.setGainHF(preset.flGainHF);
        this.setDecayTime(preset.flDecayTime);
        this.setDecayHFRatio(preset.flDecayHFRatio);
        this.setReflectionsGain(preset.flReflectionsGain);
        this.setReflectionsDelay(preset.flReflectionsDelay);
        this.setLateReverbGain(preset.flLateReverbGain);
        this.setLateReverbDelay(preset.flLateReverbDelay);
        this.setAirAbsorptionGainHF(preset.flAirAbsorptionGainHF);
        this.setRoomRolloffFactor(preset.flRoomRolloffFactor);
        this.setReflectionsDelay(preset.flReflectionsDelay);
        this.setDecayHFLimit(preset.iDecayHFLimit);
    }

    public abstract void setDensity(float f);
    public abstract void setDiffusion(float f);
    public abstract void setGain(float f);
    public abstract void setGainHF(float f);
    public abstract void setDecayTime(float f);
    public abstract void setDecayHFRatio(float f);
    public abstract void setReflectionsGain(float f);
    public abstract void setReflectionsDelay(float f);
    public abstract void setLateReverbGain(float f);
    public abstract void setLateReverbDelay(float f);
    public abstract void setAirAbsorptionGainHF(float f);
    public abstract void setRoomRolloffFactor(float f);
    public abstract void setDecayHFLimit(int i);

    public static class EAXReverb extends Reverb {
        public EAXReverb(EFXPreset preset) {
            super(EXTEfx.AL_EFFECT_EAXREVERB, preset);
        }

        @Override
        protected void applyPreset(EFXPreset preset) {
            super.applyPreset(preset);

            this.setGainLF(preset.flGainLF);
            this.setDecayLFRatio(preset.flDecayLFRatio);

            this.setEchoTime(preset.flEchoTime);
            this.setEchoDepth(preset.flEchoDepth);
            this.setModulationTime(preset.flModulationTime);
            this.setModulationDepth(preset.flModulationDepth);
            this.setHFReference(preset.flHFReference);
            this.setLFReference(preset.flLFReference);
        }

        public void setDensity(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_DENSITY, f);
        }
        public void setDiffusion(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_DIFFUSION, f);
        }
        public void setGain(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_GAIN, f);
        }
        public void setGainHF(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_GAINHF, f);
        }
        public void setGainLF(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_GAINLF, f);
        }
        public void setDecayTime(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_DECAY_TIME, f);
        }
        public void setDecayHFRatio(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_DECAY_HFRATIO, f);
        }
        public void setDecayLFRatio(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_DECAY_LFRATIO, f);
        }
        public void setReflectionsGain(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN, f);
        }
        public void setReflectionsDelay(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY, f);
        }
        public void setLateReverbGain(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN, f);
        }
        public void setLateReverbDelay(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, f);
        }
        public void setAirAbsorptionGainHF(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, f);
        }
        public void setRoomRolloffFactor(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, f);
        }
        public void setDecayHFLimit(int i) {
            setInt(EXTEfx.AL_EAXREVERB_DECAY_HFLIMIT, i);
        }

        public void setEchoTime(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_ECHO_TIME, f);
        }
        public void setEchoDepth(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_ECHO_DEPTH, f);
        }
        public void setModulationTime(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_MODULATION_TIME, f);
        }
        public void setModulationDepth(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_MODULATION_DEPTH, f);
        }
        public void setHFReference(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_HFREFERENCE, f);
        }
        public void setLFReference(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_LFREFERENCE, f);
        }
    }

    public static class StandardReverb extends Reverb {
        public StandardReverb(EFXPreset preset) {
            super(EXTEfx.AL_EFFECT_REVERB, preset);
        }

        public void setDensity(float f) {
            setFloat(EXTEfx.AL_REVERB_DENSITY, f);
        }
        public void setDiffusion(float f) {
            setFloat(EXTEfx.AL_REVERB_DIFFUSION, f);
        }
        public void setGain(float f) {
            setFloat(EXTEfx.AL_REVERB_GAIN, f);
        }
        public void setGainHF(float f) {
            setFloat(EXTEfx.AL_REVERB_GAINHF, f);
        }
        public void setDecayTime(float f) {
            setFloat(EXTEfx.AL_REVERB_DECAY_TIME, f);
        }
        public void setDecayHFRatio(float f) {
            setFloat(EXTEfx.AL_REVERB_DECAY_HFRATIO, f);
        }
        public void setReflectionsGain(float f) {
            setFloat(EXTEfx.AL_REVERB_REFLECTIONS_GAIN, f);
        }
        public void setReflectionsDelay(float f) {
            setFloat(EXTEfx.AL_REVERB_REFLECTIONS_DELAY, f);
        }
        public void setLateReverbGain(float f) {
            setFloat(EXTEfx.AL_REVERB_LATE_REVERB_GAIN, f);
        }
        public void setLateReverbDelay(float f) {
            setFloat(EXTEfx.AL_REVERB_LATE_REVERB_DELAY, f);
        }
        public void setAirAbsorptionGainHF(float f) {
            setFloat(EXTEfx.AL_REVERB_AIR_ABSORPTION_GAINHF, f);
        }
        public void setRoomRolloffFactor(float f) {
            setFloat(EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR, f);
        }
        public void setDecayHFLimit(int i) {
            setInt(EXTEfx.AL_REVERB_DECAY_HFLIMIT, i);
        }
    }

    /**
     * Creates a reverb effect using given preset. There are two reverb effects: EAX and standard. EAX reverb supports
     * a few more parameters than the standard reverb but is less widely available. This method automatically returns
     * EAX reverb if it is available or standard reverb if it is not.
     * @param preset preset from {@link EFXPreset}
     * @return best reverb that is available on this system
     */
    public static Reverb getBestAvailableReverb(EFXPreset preset) {
        AudioContext.checkALError(); // make sure there's no error BEFORE trying to create eax reverb

        Reverb reverb = new EAXReverb(preset);
        if (AL10.alGetError() == AL10.AL_NO_ERROR) {
            return reverb;
        }

        return new StandardReverb(preset);
    }

    /**
     * Bunch of reverb presets.
     *
     * @author Creative’s audio specialists
     */
    public enum EFXPreset {
        GENERIC(1.0000f, 1.0000f, 0.3162f, 0.8913f, 1.0000f, 1.4900f, 0.8300f, 1.0000f, 0.0500f, 0.0070f, 1.2589f, 0.0110f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        PADDEDCELL(0.1715f, 1.0000f, 0.3162f, 0.0010f, 1.0000f, 0.1700f, 0.1000f, 1.0000f, 0.2500f, 0.0010f, 1.2691f, 0.0020f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        ROOM(0.4287f, 1.0000f, 0.3162f, 0.5929f, 1.0000f, 0.4000f, 0.8300f, 1.0000f, 0.1503f, 0.0020f, 1.0629f, 0.0030f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        BATHROOM(0.1715f, 1.0000f, 0.3162f, 0.2512f, 1.0000f, 1.4900f, 0.5400f, 1.0000f, 0.6531f, 0.0070f, 3.2734f, 0.0110f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        LIVINGROOM(0.9766f, 1.0000f, 0.3162f, 0.0010f, 1.0000f, 0.5000f, 0.1000f, 1.0000f, 0.2051f, 0.0030f, 0.2805f, 0.0040f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        STONEROOM(1.0000f, 1.0000f, 0.3162f, 0.7079f, 1.0000f, 2.3100f, 0.6400f, 1.0000f, 0.4411f, 0.0120f, 1.1003f, 0.0170f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        AUDITORIUM(1.0000f, 1.0000f, 0.3162f, 0.5781f, 1.0000f, 4.3200f, 0.5900f, 1.0000f, 0.4032f, 0.0200f, 0.7170f, 0.0300f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CONCERTHALL(1.0000f, 1.0000f, 0.3162f, 0.5623f, 1.0000f, 3.9200f, 0.7000f, 1.0000f, 0.2427f, 0.0200f, 0.9977f, 0.0290f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CAVE(1.0000f, 1.0000f, 0.3162f, 1.0000f, 1.0000f, 2.9100f, 1.3000f, 1.0000f, 0.5000f, 0.0150f, 0.7063f, 0.0220f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        ARENA(1.0000f, 1.0000f, 0.3162f, 0.4477f, 1.0000f, 7.2400f, 0.3300f, 1.0000f, 0.2612f, 0.0200f, 1.0186f, 0.0300f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        HANGAR(1.0000f, 1.0000f, 0.3162f, 0.3162f, 1.0000f, 10.0500f, 0.2300f, 1.0000f, 0.5000f, 0.0200f, 1.2560f, 0.0300f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CARPETEDHALLWAY(0.4287f, 1.0000f, 0.3162f, 0.0100f, 1.0000f, 0.3000f, 0.1000f, 1.0000f, 0.1215f, 0.0020f, 0.1531f, 0.0300f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        HALLWAY(0.3645f, 1.0000f, 0.3162f, 0.7079f, 1.0000f, 1.4900f, 0.5900f, 1.0000f, 0.2458f, 0.0070f, 1.6615f, 0.0110f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        STONECORRIDOR(1.0000f, 1.0000f, 0.3162f, 0.7612f, 1.0000f, 2.7000f, 0.7900f, 1.0000f, 0.2472f, 0.0130f, 1.5758f, 0.0200f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        ALLEY(1.0000f, 0.3000f, 0.3162f, 0.7328f, 1.0000f, 1.4900f, 0.8600f, 1.0000f, 0.2500f, 0.0070f, 0.9954f, 0.0110f, 0.1250f, 0.9500f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        FOREST(1.0000f, 0.3000f, 0.3162f, 0.0224f, 1.0000f, 1.4900f, 0.5400f, 1.0000f, 0.0525f, 0.1620f, 0.7682f, 0.0880f, 0.1250f, 1.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CITY(1.0000f, 0.5000f, 0.3162f, 0.3981f, 1.0000f, 1.4900f, 0.6700f, 1.0000f, 0.0730f, 0.0070f, 0.1427f, 0.0110f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        MOUNTAINS(1.0000f, 0.2700f, 0.3162f, 0.0562f, 1.0000f, 1.4900f, 0.2100f, 1.0000f, 0.0407f, 0.3000f, 0.1919f, 0.1000f, 0.2500f, 1.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        QUARRY(1.0000f, 1.0000f, 0.3162f, 0.3162f, 1.0000f, 1.4900f, 0.8300f, 1.0000f, 0.0000f, 0.0610f, 1.7783f, 0.0250f, 0.1250f, 0.7000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        PLAIN(1.0000f, 0.2100f, 0.3162f, 0.1000f, 1.0000f, 1.4900f, 0.5000f, 1.0000f, 0.0585f, 0.1790f, 0.1089f, 0.1000f, 0.2500f, 1.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        PARKINGLOT(1.0000f, 1.0000f, 0.3162f, 1.0000f, 1.0000f, 1.6500f, 1.5000f, 1.0000f, 0.2082f, 0.0080f, 0.2652f, 0.0120f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        SEWERPIPE(0.3071f, 0.8000f, 0.3162f, 0.3162f, 1.0000f, 2.8100f, 0.1400f, 1.0000f, 1.6387f, 0.0140f, 3.2471f, 0.0210f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        UNDERWATER(0.3645f, 1.0000f, 0.3162f, 0.0100f, 1.0000f, 1.4900f, 0.1000f, 1.0000f, 0.5963f, 0.0070f, 7.0795f, 0.0110f, 0.2500f, 0.0000f, 1.1800f, 0.3480f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        DRUGGED(0.4287f, 0.5000f, 0.3162f, 1.0000f, 1.0000f, 8.3900f, 1.3900f, 1.0000f, 0.8760f, 0.0020f, 3.1081f, 0.0300f, 0.2500f, 0.0000f, 0.2500f, 1.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        DIZZY(0.3645f, 0.6000f, 0.3162f, 0.6310f, 1.0000f, 17.2300f, 0.5600f, 1.0000f, 0.1392f, 0.0200f, 0.4937f, 0.0300f, 0.2500f, 1.0000f, 0.8100f, 0.3100f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        PSYCHOTIC(0.0625f, 0.5000f, 0.3162f, 0.8404f, 1.0000f, 7.5600f, 0.9100f, 1.0000f, 0.4864f, 0.0200f, 2.4378f, 0.0300f, 0.2500f, 0.0000f, 4.0000f, 1.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
/* Castle Presets */
        CASTLE_SMALLROOM(1.0000f, 0.8900f, 0.3162f, 0.3981f, 0.1000f, 1.2200f, 0.8300f, 0.3100f, 0.8913f, 0.0220f, 1.9953f, 0.0110f, 0.1380f, 0.0800f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_SHORTPASSAGE(1.0000f, 0.8900f, 0.3162f, 0.3162f, 0.1000f, 2.3200f, 0.8300f, 0.3100f, 0.8913f, 0.0070f, 1.2589f, 0.0230f, 0.1380f, 0.0800f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_MEDIUMROOM(1.0000f, 0.9300f, 0.3162f, 0.2818f, 0.1000f, 2.0400f, 0.8300f, 0.4600f, 0.6310f, 0.0220f, 1.5849f, 0.0110f, 0.1550f, 0.0300f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_LARGEROOM(1.0000f, 0.8200f, 0.3162f, 0.2818f, 0.1259f, 2.5300f, 0.8300f, 0.5000f, 0.4467f, 0.0340f, 1.2589f, 0.0160f, 0.1850f, 0.0700f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_LONGPASSAGE(1.0000f, 0.8900f, 0.3162f, 0.3981f, 0.1000f, 3.4200f, 0.8300f, 0.3100f, 0.8913f, 0.0070f, 1.4125f, 0.0230f, 0.1380f, 0.0800f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_HALL(1.0000f, 0.8100f, 0.3162f, 0.2818f, 0.1778f, 3.1400f, 0.7900f, 0.6200f, 0.1778f, 0.0560f, 1.1220f, 0.0240f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_CUPBOARD(1.0000f, 0.8900f, 0.3162f, 0.2818f, 0.1000f, 0.6700f, 0.8700f, 0.3100f, 1.4125f, 0.0100f, 3.5481f, 0.0070f, 0.1380f, 0.0800f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
        CASTLE_COURTYARD(1.0000f, 0.4200f, 0.3162f, 0.4467f, 0.1995f, 2.1300f, 0.6100f, 0.2300f, 0.2239f, 0.1600f, 0.7079f, 0.0360f, 0.2500f, 0.3700f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        CASTLE_ALCOVE(1.0000f, 0.8900f, 0.3162f, 0.5012f, 0.1000f, 1.6400f, 0.8700f, 0.3100f, 1.0000f, 0.0070f, 1.4125f, 0.0340f, 0.1380f, 0.0800f, 0.2500f, 0.0000f, 0.9943f, 5168.6001f, 139.5000f, 0.0000f, 0x1),
/* Factory Presets */
        FACTORY_SMALLROOM(0.3645f, 0.8200f, 0.3162f, 0.7943f, 0.5012f, 1.7200f, 0.6500f, 1.3100f, 0.7079f, 0.0100f, 1.7783f, 0.0240f, 0.1190f, 0.0700f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_SHORTPASSAGE(0.3645f, 0.6400f, 0.2512f, 0.7943f, 0.5012f, 2.5300f, 0.6500f, 1.3100f, 1.0000f, 0.0100f, 1.2589f, 0.0380f, 0.1350f, 0.2300f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_MEDIUMROOM(0.4287f, 0.8200f, 0.2512f, 0.7943f, 0.5012f, 2.7600f, 0.6500f, 1.3100f, 0.2818f, 0.0220f, 1.4125f, 0.0230f, 0.1740f, 0.0700f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_LARGEROOM(0.4287f, 0.7500f, 0.2512f, 0.7079f, 0.6310f, 4.2400f, 0.5100f, 1.3100f, 0.1778f, 0.0390f, 1.1220f, 0.0230f, 0.2310f, 0.0700f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_LONGPASSAGE(0.3645f, 0.6400f, 0.2512f, 0.7943f, 0.5012f, 4.0600f, 0.6500f, 1.3100f, 1.0000f, 0.0200f, 1.2589f, 0.0370f, 0.1350f, 0.2300f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_HALL(0.4287f, 0.7500f, 0.3162f, 0.7079f, 0.6310f, 7.4300f, 0.5100f, 1.3100f, 0.0631f, 0.0730f, 0.8913f, 0.0270f, 0.2500f, 0.0700f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_CUPBOARD(0.3071f, 0.6300f, 0.2512f, 0.7943f, 0.5012f, 0.4900f, 0.6500f, 1.3100f, 1.2589f, 0.0100f, 1.9953f, 0.0320f, 0.1070f, 0.0700f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_COURTYARD(0.3071f, 0.5700f, 0.3162f, 0.3162f, 0.6310f, 2.3200f, 0.2900f, 0.5600f, 0.2239f, 0.1400f, 0.3981f, 0.0390f, 0.2500f, 0.2900f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
        FACTORY_ALCOVE(0.3645f, 0.5900f, 0.2512f, 0.7943f, 0.5012f, 3.1400f, 0.6500f, 1.3100f, 1.4125f, 0.0100f, 1.0000f, 0.0380f, 0.1140f, 0.1000f, 0.2500f, 0.0000f, 0.9943f, 3762.6001f, 362.5000f, 0.0000f, 0x1),
/* Ice Palace Presets */
        ICEPALACE_SMALLROOM(1.0000f, 0.8400f, 0.3162f, 0.5623f, 0.2818f, 1.5100f, 1.5300f, 0.2700f, 0.8913f, 0.0100f, 1.4125f, 0.0110f, 0.1640f, 0.1400f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_SHORTPASSAGE(1.0000f, 0.7500f, 0.3162f, 0.5623f, 0.2818f, 1.7900f, 1.4600f, 0.2800f, 0.5012f, 0.0100f, 1.1220f, 0.0190f, 0.1770f, 0.0900f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_MEDIUMROOM(1.0000f, 0.8700f, 0.3162f, 0.5623f, 0.4467f, 2.2200f, 1.5300f, 0.3200f, 0.3981f, 0.0390f, 1.1220f, 0.0270f, 0.1860f, 0.1200f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_LARGEROOM(1.0000f, 0.8100f, 0.3162f, 0.5623f, 0.4467f, 3.1400f, 1.5300f, 0.3200f, 0.2512f, 0.0390f, 1.0000f, 0.0270f, 0.2140f, 0.1100f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_LONGPASSAGE(1.0000f, 0.7700f, 0.3162f, 0.5623f, 0.3981f, 3.0100f, 1.4600f, 0.2800f, 0.7943f, 0.0120f, 1.2589f, 0.0250f, 0.1860f, 0.0400f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_HALL(1.0000f, 0.7600f, 0.3162f, 0.4467f, 0.5623f, 5.4900f, 1.5300f, 0.3800f, 0.1122f, 0.0540f, 0.6310f, 0.0520f, 0.2260f, 0.1100f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_CUPBOARD(1.0000f, 0.8300f, 0.3162f, 0.5012f, 0.2239f, 0.7600f, 1.5300f, 0.2600f, 1.1220f, 0.0120f, 1.9953f, 0.0160f, 0.1430f, 0.0800f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_COURTYARD(1.0000f, 0.5900f, 0.3162f, 0.2818f, 0.3162f, 2.0400f, 1.2000f, 0.3800f, 0.3162f, 0.1730f, 0.3162f, 0.0430f, 0.2350f, 0.4800f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
        ICEPALACE_ALCOVE(1.0000f, 0.8400f, 0.3162f, 0.5623f, 0.2818f, 2.7600f, 1.4600f, 0.2800f, 1.1220f, 0.0100f, 0.8913f, 0.0300f, 0.1610f, 0.0900f, 0.2500f, 0.0000f, 0.9943f, 12428.5000f, 99.6000f, 0.0000f, 0x1),
/* Space Station Presets */
        SPACESTATION_SMALLROOM(0.2109f, 0.7000f, 0.3162f, 0.7079f, 0.8913f, 1.7200f, 0.8200f, 0.5500f, 0.7943f, 0.0070f, 1.4125f, 0.0130f, 0.1880f, 0.2600f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_SHORTPASSAGE(0.2109f, 0.8700f, 0.3162f, 0.6310f, 0.8913f, 3.5700f, 0.5000f, 0.5500f, 1.0000f, 0.0120f, 1.1220f, 0.0160f, 0.1720f, 0.2000f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_MEDIUMROOM(0.2109f, 0.7500f, 0.3162f, 0.6310f, 0.8913f, 3.0100f, 0.5000f, 0.5500f, 0.3981f, 0.0340f, 1.1220f, 0.0350f, 0.2090f, 0.3100f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_LARGEROOM(0.3645f, 0.8100f, 0.3162f, 0.6310f, 0.8913f, 3.8900f, 0.3800f, 0.6100f, 0.3162f, 0.0560f, 0.8913f, 0.0350f, 0.2330f, 0.2800f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_LONGPASSAGE(0.4287f, 0.8200f, 0.3162f, 0.6310f, 0.8913f, 4.6200f, 0.6200f, 0.5500f, 1.0000f, 0.0120f, 1.2589f, 0.0310f, 0.2500f, 0.2300f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_HALL(0.4287f, 0.8700f, 0.3162f, 0.6310f, 0.8913f, 7.1100f, 0.3800f, 0.6100f, 0.1778f, 0.1000f, 0.6310f, 0.0470f, 0.2500f, 0.2500f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_CUPBOARD(0.1715f, 0.5600f, 0.3162f, 0.7079f, 0.8913f, 0.7900f, 0.8100f, 0.5500f, 1.4125f, 0.0070f, 1.7783f, 0.0180f, 0.1810f, 0.3100f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
        SPACESTATION_ALCOVE(0.2109f, 0.7800f, 0.3162f, 0.7079f, 0.8913f, 1.1600f, 0.8100f, 0.5500f, 1.4125f, 0.0070f, 1.0000f, 0.0180f, 0.1920f, 0.2100f, 0.2500f, 0.0000f, 0.9943f, 3316.1001f, 458.2000f, 0.0000f, 0x1),
/* Wooden Galleon Presets */
        WOODEN_SMALLROOM(1.0000f, 1.0000f, 0.3162f, 0.1122f, 0.3162f, 0.7900f, 0.3200f, 0.8700f, 1.0000f, 0.0320f, 0.8913f, 0.0290f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_SHORTPASSAGE(1.0000f, 1.0000f, 0.3162f, 0.1259f, 0.3162f, 1.7500f, 0.5000f, 0.8700f, 0.8913f, 0.0120f, 0.6310f, 0.0240f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_MEDIUMROOM(1.0000f, 1.0000f, 0.3162f, 0.1000f, 0.2818f, 1.4700f, 0.4200f, 0.8200f, 0.8913f, 0.0490f, 0.8913f, 0.0290f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_LARGEROOM(1.0000f, 1.0000f, 0.3162f, 0.0891f, 0.2818f, 2.6500f, 0.3300f, 0.8200f, 0.8913f, 0.0660f, 0.7943f, 0.0490f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_LONGPASSAGE(1.0000f, 1.0000f, 0.3162f, 0.1000f, 0.3162f, 1.9900f, 0.4000f, 0.7900f, 1.0000f, 0.0200f, 0.4467f, 0.0360f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_HALL(1.0000f, 1.0000f, 0.3162f, 0.0794f, 0.2818f, 3.4500f, 0.3000f, 0.8200f, 0.8913f, 0.0880f, 0.7943f, 0.0630f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_CUPBOARD(1.0000f, 1.0000f, 0.3162f, 0.1413f, 0.3162f, 0.5600f, 0.4600f, 0.9100f, 1.1220f, 0.0120f, 1.1220f, 0.0280f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_COURTYARD(1.0000f, 0.6500f, 0.3162f, 0.0794f, 0.3162f, 1.7900f, 0.3500f, 0.7900f, 0.5623f, 0.1230f, 0.1000f, 0.0320f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
        WOODEN_ALCOVE(1.0000f, 1.0000f, 0.3162f, 0.1259f, 0.3162f, 1.2200f, 0.6200f, 0.9100f, 1.1220f, 0.0120f, 0.7079f, 0.0240f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 4705.0000f, 99.6000f, 0.0000f, 0x1),
/* Sports Presets */
        SPORT_EMPTYSTADIUM(1.0000f, 1.0000f, 0.3162f, 0.4467f, 0.7943f, 6.2600f, 0.5100f, 1.1000f, 0.0631f, 0.1830f, 0.3981f, 0.0380f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        SPORT_SQUASHCOURT(1.0000f, 0.7500f, 0.3162f, 0.3162f, 0.7943f, 2.2200f, 0.9100f, 1.1600f, 0.4467f, 0.0070f, 0.7943f, 0.0110f, 0.1260f, 0.1900f, 0.2500f, 0.0000f, 0.9943f, 7176.8999f, 211.2000f, 0.0000f, 0x1),
        SPORT_SMALLSWIMMINGPOOL(1.0000f, 0.7000f, 0.3162f, 0.7943f, 0.8913f, 2.7600f, 1.2500f, 1.1400f, 0.6310f, 0.0200f, 0.7943f, 0.0300f, 0.1790f, 0.1500f, 0.8950f, 0.1900f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        SPORT_LARGESWIMMINGPOOL(1.0000f, 0.8200f, 0.3162f, 0.7943f, 1.0000f, 5.4900f, 1.3100f, 1.1400f, 0.4467f, 0.0390f, 0.5012f, 0.0490f, 0.2220f, 0.5500f, 1.1590f, 0.2100f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        SPORT_GYMNASIUM(1.0000f, 0.8100f, 0.3162f, 0.4467f, 0.8913f, 3.1400f, 1.0600f, 1.3500f, 0.3981f, 0.0290f, 0.5623f, 0.0450f, 0.1460f, 0.1400f, 0.2500f, 0.0000f, 0.9943f, 7176.8999f, 211.2000f, 0.0000f, 0x1),
        SPORT_FULLSTADIUM(1.0000f, 1.0000f, 0.3162f, 0.0708f, 0.7943f, 5.2500f, 0.1700f, 0.8000f, 0.1000f, 0.1880f, 0.2818f, 0.0380f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        SPORT_STADIUMTANNOY(1.0000f, 0.7800f, 0.3162f, 0.5623f, 0.5012f, 2.5300f, 0.8800f, 0.6800f, 0.2818f, 0.2300f, 0.5012f, 0.0630f, 0.2500f, 0.2000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
/* Prefab Presets */
        PREFAB_WORKSHOP(0.4287f, 1.0000f, 0.3162f, 0.1413f, 0.3981f, 0.7600f, 1.0000f, 1.0000f, 1.0000f, 0.0120f, 1.1220f, 0.0120f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        PREFAB_SCHOOLROOM(0.4022f, 0.6900f, 0.3162f, 0.6310f, 0.5012f, 0.9800f, 0.4500f, 0.1800f, 1.4125f, 0.0170f, 1.4125f, 0.0150f, 0.0950f, 0.1400f, 0.2500f, 0.0000f, 0.9943f, 7176.8999f, 211.2000f, 0.0000f, 0x1),
        PREFAB_PRACTISEROOM(0.4022f, 0.8700f, 0.3162f, 0.3981f, 0.5012f, 1.1200f, 0.5600f, 0.1800f, 1.2589f, 0.0100f, 1.4125f, 0.0110f, 0.0950f, 0.1400f, 0.2500f, 0.0000f, 0.9943f, 7176.8999f, 211.2000f, 0.0000f, 0x1),
        PREFAB_OUTHOUSE(1.0000f, 0.8200f, 0.3162f, 0.1122f, 0.1585f, 1.3800f, 0.3800f, 0.3500f, 0.8913f, 0.0240f, 0.6310f, 0.0440f, 0.1210f, 0.1700f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 107.5000f, 0.0000f, 0x0),
        PREFAB_CARAVAN(1.0000f, 1.0000f, 0.3162f, 0.0891f, 0.1259f, 0.4300f, 1.5000f, 1.0000f, 1.0000f, 0.0120f, 1.9953f, 0.0120f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
/* Dome and Pipe Presets */
        DOME_TOMB(1.0000f, 0.7900f, 0.3162f, 0.3548f, 0.2239f, 4.1800f, 0.2100f, 0.1000f, 0.3868f, 0.0300f, 1.6788f, 0.0220f, 0.1770f, 0.1900f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 20.0000f, 0.0000f, 0x0),
        PIPE_SMALL(1.0000f, 1.0000f, 0.3162f, 0.3548f, 0.2239f, 5.0400f, 0.1000f, 0.1000f, 0.5012f, 0.0320f, 2.5119f, 0.0150f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 20.0000f, 0.0000f, 0x1),
        DOME_SAINTPAULS(1.0000f, 0.8700f, 0.3162f, 0.3548f, 0.2239f, 10.4800f, 0.1900f, 0.1000f, 0.1778f, 0.0900f, 1.2589f, 0.0420f, 0.2500f, 0.1200f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 20.0000f, 0.0000f, 0x1),
        PIPE_LONGTHIN(0.2560f, 0.9100f, 0.3162f, 0.4467f, 0.2818f, 9.2100f, 0.1800f, 0.1000f, 0.7079f, 0.0100f, 0.7079f, 0.0220f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 20.0000f, 0.0000f, 0x0),
        PIPE_LARGE(1.0000f, 1.0000f, 0.3162f, 0.3548f, 0.2239f, 8.4500f, 0.1000f, 0.1000f, 0.3981f, 0.0460f, 1.5849f, 0.0320f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 20.0000f, 0.0000f, 0x1),
        PIPE_RESONANT(0.1373f, 0.9100f, 0.3162f, 0.4467f, 0.2818f, 6.8100f, 0.1800f, 0.1000f, 0.7079f, 0.0100f, 1.0000f, 0.0220f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 20.0000f, 0.0000f, 0x0),
/* Outdoors Presets */
        OUTDOORS_BACKYARD(1.0000f, 0.4500f, 0.3162f, 0.2512f, 0.5012f, 1.1200f, 0.3400f, 0.4600f, 0.4467f, 0.0690f, 0.7079f, 0.0230f, 0.2180f, 0.3400f, 0.2500f, 0.0000f, 0.9943f, 4399.1001f, 242.9000f, 0.0000f, 0x0),
        OUTDOORS_ROLLINGPLAINS(1.0000f, 0.0000f, 0.3162f, 0.0112f, 0.6310f, 2.1300f, 0.2100f, 0.4600f, 0.1778f, 0.3000f, 0.4467f, 0.0190f, 0.2500f, 1.0000f, 0.2500f, 0.0000f, 0.9943f, 4399.1001f, 242.9000f, 0.0000f, 0x0),
        OUTDOORS_DEEPCANYON(1.0000f, 0.7400f, 0.3162f, 0.1778f, 0.6310f, 3.8900f, 0.2100f, 0.4600f, 0.3162f, 0.2230f, 0.3548f, 0.0190f, 0.2500f, 1.0000f, 0.2500f, 0.0000f, 0.9943f, 4399.1001f, 242.9000f, 0.0000f, 0x0),
        OUTDOORS_CREEK(1.0000f, 0.3500f, 0.3162f, 0.1778f, 0.5012f, 2.1300f, 0.2100f, 0.4600f, 0.3981f, 0.1150f, 0.1995f, 0.0310f, 0.2180f, 0.3400f, 0.2500f, 0.0000f, 0.9943f, 4399.1001f, 242.9000f, 0.0000f, 0x0),
        OUTDOORS_VALLEY(1.0000f, 0.2800f, 0.3162f, 0.0282f, 0.1585f, 2.8800f, 0.2600f, 0.3500f, 0.1413f, 0.2630f, 0.3981f, 0.1000f, 0.2500f, 0.3400f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 107.5000f, 0.0000f, 0x0),
/* Mood Presets */
        MOOD_HEAVEN(1.0000f, 0.9400f, 0.3162f, 0.7943f, 0.4467f, 5.0400f, 1.1200f, 0.5600f, 0.2427f, 0.0200f, 1.2589f, 0.0290f, 0.2500f, 0.0800f, 2.7420f, 0.0500f, 0.9977f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        MOOD_HELL(1.0000f, 0.5700f, 0.3162f, 0.3548f, 0.4467f, 3.5700f, 0.4900f, 2.0000f, 0.0000f, 0.0200f, 1.4125f, 0.0300f, 0.1100f, 0.0400f, 2.1090f, 0.5200f, 0.9943f, 5000.0000f, 139.5000f, 0.0000f, 0x0),
        MOOD_MEMORY(1.0000f, 0.8500f, 0.3162f, 0.6310f, 0.3548f, 4.0600f, 0.8200f, 0.5600f, 0.0398f, 0.0000f, 1.1220f, 0.0000f, 0.2500f, 0.0000f, 0.4740f, 0.4500f, 0.9886f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
/* Driving Presets */
        DRIVING_COMMENTATOR(1.0000f, 0.0000f, 0.3162f, 0.5623f, 0.5012f, 2.4200f, 0.8800f, 0.6800f, 0.1995f, 0.0930f, 0.2512f, 0.0170f, 0.2500f, 1.0000f, 0.2500f, 0.0000f, 0.9886f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        DRIVING_PITGARAGE(0.4287f, 0.5900f, 0.3162f, 0.7079f, 0.5623f, 1.7200f, 0.9300f, 0.8700f, 0.5623f, 0.0000f, 1.2589f, 0.0160f, 0.2500f, 0.1100f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x0),
        DRIVING_INCAR_RACER(0.0832f, 0.8000f, 0.3162f, 1.0000f, 0.7943f, 0.1700f, 2.0000f, 0.4100f, 1.7783f, 0.0070f, 0.7079f, 0.0150f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 10268.2002f, 251.0000f, 0.0000f, 0x1),
        DRIVING_INCAR_SPORTS(0.0832f, 0.8000f, 0.3162f, 0.6310f, 1.0000f, 0.1700f, 0.7500f, 0.4100f, 1.0000f, 0.0100f, 0.5623f, 0.0000f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 10268.2002f, 251.0000f, 0.0000f, 0x1),
        DRIVING_INCAR_LUXURY(0.2560f, 1.0000f, 0.3162f, 0.1000f, 0.5012f, 0.1300f, 0.4100f, 0.4600f, 0.7943f, 0.0100f, 1.5849f, 0.0100f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 10268.2002f, 251.0000f, 0.0000f, 0x1),
        DRIVING_FULLGRANDSTAND(1.0000f, 1.0000f, 0.3162f, 0.2818f, 0.6310f, 3.0100f, 1.3700f, 1.2800f, 0.3548f, 0.0900f, 0.1778f, 0.0490f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 10420.2002f, 250.0000f, 0.0000f, 0x0),
        DRIVING_EMPTYGRANDSTAND(1.0000f, 1.0000f, 0.3162f, 1.0000f, 0.7943f, 4.6200f, 1.7500f, 1.4000f, 0.2082f, 0.0900f, 0.2512f, 0.0490f, 0.2500f, 0.0000f, 0.2500f, 0.0000f, 0.9943f, 10420.2002f, 250.0000f, 0.0000f, 0x0),
        DRIVING_TUNNEL(1.0000f, 0.8100f, 0.3162f, 0.3981f, 0.8913f, 3.4200f, 0.9400f, 1.3100f, 0.7079f, 0.0510f, 0.7079f, 0.0470f, 0.2140f, 0.0500f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 155.3000f, 0.0000f, 0x1),
/* City Presets */
        CITY_STREETS(1.0000f, 0.7800f, 0.3162f, 0.7079f, 0.8913f, 1.7900f, 1.1200f, 0.9100f, 0.2818f, 0.0460f, 0.1995f, 0.0280f, 0.2500f, 0.2000f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CITY_SUBWAY(1.0000f, 0.7400f, 0.3162f, 0.7079f, 0.8913f, 3.0100f, 1.2300f, 0.9100f, 0.7079f, 0.0460f, 1.2589f, 0.0280f, 0.1250f, 0.2100f, 0.2500f, 0.0000f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CITY_MUSEUM(1.0000f, 0.8200f, 0.3162f, 0.1778f, 0.1778f, 3.2800f, 1.4000f, 0.5700f, 0.2512f, 0.0390f, 0.8913f, 0.0340f, 0.1300f, 0.1700f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 107.5000f, 0.0000f, 0x0),
        CITY_LIBRARY(1.0000f, 0.8200f, 0.3162f, 0.2818f, 0.0891f, 2.7600f, 0.8900f, 0.4100f, 0.3548f, 0.0290f, 0.8913f, 0.0200f, 0.1300f, 0.1700f, 0.2500f, 0.0000f, 0.9943f, 2854.3999f, 107.5000f, 0.0000f, 0x0),
        CITY_UNDERPASS(1.0000f, 0.8200f, 0.3162f, 0.4467f, 0.8913f, 3.5700f, 1.1200f, 0.9100f, 0.3981f, 0.0590f, 0.8913f, 0.0370f, 0.2500f, 0.1400f, 0.2500f, 0.0000f, 0.9920f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        CITY_ABANDONED(1.0000f, 0.6900f, 0.3162f, 0.7943f, 0.8913f, 3.2800f, 1.1700f, 0.9100f, 0.4467f, 0.0440f, 0.2818f, 0.0240f, 0.2500f, 0.2000f, 0.2500f, 0.0000f, 0.9966f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
/* Misc. Presets */
        DUSTYROOM(0.3645f, 0.5600f, 0.3162f, 0.7943f, 0.7079f, 1.7900f, 0.3800f, 0.2100f, 0.5012f, 0.0020f, 1.2589f, 0.0060f, 0.2020f, 0.0500f, 0.2500f, 0.0000f, 0.9886f, 13046.0000f, 163.3000f, 0.0000f, 0x1),
        CHAPEL(1.0000f, 0.8400f, 0.3162f, 0.5623f, 1.0000f, 4.6200f, 0.6400f, 1.2300f, 0.4467f, 0.0320f, 0.7943f, 0.0490f, 0.2500f, 0.0000f, 0.2500f, 0.1100f, 0.9943f, 5000.0000f, 250.0000f, 0.0000f, 0x1),
        SMALLWATERROOM(1.0000f, 0.7000f, 0.3162f, 0.4477f, 1.0000f, 1.5100f, 1.2500f, 1.1400f, 0.8913f, 0.0200f, 1.4125f, 0.0300f, 0.1790f, 0.1500f, 0.8950f, 0.1900f, 0.9920f, 5000.0000f, 250.0000f, 0.0000f, 0x0);

        private final float flDensity;
        private final float flDiffusion;
        private final float flGain;
        private final float flGainHF;
        private final float flGainLF;
        private final float flDecayTime;
        private final float flDecayHFRatio;
        private final float flDecayLFRatio;
        private final float flReflectionsGain;
        private final float flReflectionsDelay;
        private final float flLateReverbGain;
        private final float flLateReverbDelay;
        private final float flEchoTime;
        private final float flEchoDepth;
        private final float flModulationTime;
        private final float flModulationDepth;
        private final float flAirAbsorptionGainHF;
        private final float flHFReference;
        private final float flLFReference;
        private final float flRoomRolloffFactor;
        private final int iDecayHFLimit;

        EFXPreset(
                float flDensity,
                float flDiffusion,
                float flGain,
                float flGainHF,
                float flGainLF,
                float flDecayTime,
                float flDecayHFRatio,
                float flDecayLFRatio,
                float flReflectionsGain,
                float flReflectionsDelay,
                float flLateReverbGain,
                float flLateReverbDelay,
                float flEchoTime,
                float flEchoDepth,
                float flModulationTime,
                float flModulationDepth,
                float flAirAbsorptionGainHF,
                float flHFReference,
                float flLFReference,
                float flRoomRolloffFactor,
                int   iDecayHFLimit
        ) {

            this.flDensity = flDensity;
            this.flDiffusion = flDiffusion;
            this.flGain = flGain;
            this.flGainHF = flGainHF;
            this.flGainLF = flGainLF;
            this.flDecayTime = flDecayTime;
            this.flDecayHFRatio = flDecayHFRatio;
            this.flDecayLFRatio = flDecayLFRatio;
            this.flReflectionsGain = flReflectionsGain;
            this.flReflectionsDelay = flReflectionsDelay;
            this.flLateReverbGain = flLateReverbGain;
            this.flLateReverbDelay = flLateReverbDelay;
            this.flEchoTime = flEchoTime;
            this.flEchoDepth = flEchoDepth;
            this.flModulationTime = flModulationTime;
            this.flModulationDepth = flModulationDepth;
            this.flAirAbsorptionGainHF = flAirAbsorptionGainHF;
            this.flHFReference = flHFReference;
            this.flLFReference = flLFReference;
            this.flRoomRolloffFactor = flRoomRolloffFactor;
            this.iDecayHFLimit = iDecayHFLimit;
        }

    }
}
