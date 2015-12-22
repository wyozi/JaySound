package com.wyozi.jaysound.efx.effects;

import com.wyozi.jaysound.efx.Effect;
import org.lwjgl.openal.EFXUtil;
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
        this.setDiffusion(preset.flEnvironmentDiffusion);
        this.setDecayTime(preset.flDecayTime);
        this.setDecayHFRatio(preset.flDecayHFRatio);
        this.setReverbDelay(preset.flReverbDelay);
        this.setRoomRolloffFactor(preset.flRoomRolloffFactor);
        this.setReflectionsDelay(preset.flReflectionsDelay);
    }

    public abstract void setDiffusion(float f);
    public abstract void setDecayTime(float f);
    public abstract void setDecayHFRatio(float f);
    public abstract void setReverbDelay(float f);
    public abstract void setRoomRolloffFactor(float f);
    public abstract void setReflectionsDelay(float f);

    public static class EAXReverb extends Reverb {
        public EAXReverb(EFXPreset preset) {
            super(EXTEfx.AL_EFFECT_EAXREVERB, preset);
        }

        @Override
        protected void applyPreset(EFXPreset preset) {
            super.applyPreset(preset);

            this.setDecayLFRatio(preset.flDecayLFRatio);
            this.setEchoTime(preset.flEchoTime);
            this.setEchoDepth(preset.flEchoDepth);
            this.setModulationTime(preset.flModulationTime);
            this.setModulationDepth(preset.flModulationDepth);
            this.setHFReference(preset.flHFReference);
            this.setLFReference(preset.flLFReference);
        }

        public void setDiffusion(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_DIFFUSION, f);
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
        public void setReverbDelay(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, f);
        }
        public void setRoomRolloffFactor(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, f);
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
        public void setReflectionsDelay(float f) {
            setFloat(EXTEfx.AL_EAXREVERB_REFLECTIONS_DELAY, f);
        }
    }

    public static class StandardReverb extends Reverb {
        public StandardReverb(EFXPreset preset) {
            super(EXTEfx.AL_EFFECT_REVERB, preset);
        }

        public void setDiffusion(float f) {
            setFloat(EXTEfx.AL_REVERB_DIFFUSION, f);
        }
        public void setDecayTime(float f) {
            setFloat(EXTEfx.AL_REVERB_DECAY_TIME, f);
        }
        public void setDecayHFRatio(float f) {
            setFloat(EXTEfx.AL_REVERB_DECAY_HFRATIO, f);
        }
        public void setReverbDelay(float f) {
            setFloat(EXTEfx.AL_REVERB_LATE_REVERB_DELAY, f);
        }
        public void setRoomRolloffFactor(float f) {
            setFloat(EXTEfx.AL_REVERB_ROOM_ROLLOFF_FACTOR, f);
        }
        public void setReflectionsDelay(float f) {
            setFloat(EXTEfx.AL_REVERB_REFLECTIONS_DELAY, f);
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
        if (EFXUtil.isEffectSupported(EXTEfx.AL_EFFECT_EAXREVERB)) {
            return new EAXReverb(preset);
        } else {
            return new StandardReverb(preset);
        }
    }

    /**
     * Bunch of reverb presets.
     *
     * @author Creative’s audio specialists
     */
    public enum EFXPreset {
        GENERIC(0, 7.5f, 1.000f, -1000, -100, 0, 1.49f, 0.83f, 1.00f, -2602, 0.007f, 0.00f, 0.00f, 0.00f, 200, 0.011f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        PADDEDCELL(1, 1.4f, 1.000f, -1000, -6000, 0, 0.17f, 0.10f, 1.00f, -1204, 0.001f, 0.00f, 0.00f, 0.00f, 207, 0.002f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        ROOM(2, 1.9f, 1.000f, -1000, -454, 0, 0.40f, 0.83f, 1.00f, -1646, 0.002f, 0.00f, 0.00f, 0.00f, 53, 0.003f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        BATHROOM(3, 1.4f, 1.000f, -1000, -1200, 0, 1.49f, 0.54f, 1.00f, -370, 0.007f, 0.00f, 0.00f, 0.00f, 1030, 0.011f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        LIVINGROOM(4, 2.5f, 1.000f, -1000, -6000, 0, 0.50f, 0.10f, 1.00f, -1376, 0.003f, 0.00f, 0.00f, 0.00f, -1104, 0.004f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        STONEROOM(5, 11.6f, 1.000f, -1000, -300, 0, 2.31f, 0.64f, 1.00f, -711, 0.012f, 0.00f, 0.00f, 0.00f, 83, 0.017f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        AUDITORIUM(6, 21.6f, 1.000f, -1000, -476, 0, 4.32f, 0.59f, 1.00f, -789, 0.020f, 0.00f, 0.00f, 0.00f, -289, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        CONCERTHALL(7, 19.6f, 1.000f, -1000, -500, 0, 3.92f, 0.70f, 1.00f, -1230, 0.020f, 0.00f, 0.00f, 0.00f, -02, 0.029f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        CAVE(8, 14.6f, 1.000f, -1000, 0, 0, 2.91f, 1.30f, 1.00f, -602, 0.015f, 0.00f, 0.00f, 0.00f, -302, 0.022f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        ARENA(9, 36.2f, 1.000f, -1000, -698, 0, 7.24f, 0.33f, 1.00f, -1166, 0.020f, 0.00f, 0.00f, 0.00f, 16, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        HANGAR(10, 50.3f, 1.000f, -1000, -1000, 0, 10.05f, 0.23f, 1.00f, -602, 0.020f, 0.00f, 0.00f, 0.00f, 198, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        CARPETTEDHALLWAY(11, 1.9f, 1.000f, -1000, -4000, 0, 0.30f, 0.10f, 1.00f, -1831, 0.002f, 0.00f, 0.00f, 0.00f, -1630, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        HALLWAY(12, 1.8f, 1.000f, -1000, -300, 0, 1.49f, 0.59f, 1.00f, -1219, 0.007f, 0.00f, 0.00f, 0.00f, 441, 0.011f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        STONECORRIDOR(13, 13.5f, 1.000f, -1000, -237, 0, 2.70f, 0.79f, 1.00f, -1214, 0.013f, 0.00f, 0.00f, 0.00f, 395, 0.020f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        ALLEY(14, 7.5f, 0.300f, -1000, -270, 0, 1.49f, 0.86f, 1.00f, -1204, 0.007f, 0.00f, 0.00f, 0.00f, -4, 0.011f, 0.00f, 0.00f, 0.00f, 0.125f, 0.950f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        FOREST(15, 38.0f, 0.300f, -1000, -3300, 0, 1.49f, 0.54f, 1.00f, -2560, 0.162f, 0.00f, 0.00f, 0.00f, -229, 0.088f, 0.00f, 0.00f, 0.00f, 0.125f, 1.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        CITY(16, 7.5f, 0.500f, -1000, -800, 0, 1.49f, 0.67f, 1.00f, -2273, 0.007f, 0.00f, 0.00f, 0.00f, -1691, 0.011f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        MOUNTAINS(17, 100.0f, 0.270f, -1000, -2500, 0, 1.49f, 0.21f, 1.00f, -2780, 0.300f, 0.00f, 0.00f, 0.00f, -1434, 0.100f, 0.00f, 0.00f, 0.00f, 0.250f, 1.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        QUARRY(18, 17.5f, 1.000f, -1000, -1000, 0, 1.49f, 0.83f, 1.00f, -10000, 0.061f, 0.00f, 0.00f, 0.00f, 500, 0.025f, 0.00f, 0.00f, 0.00f, 0.125f, 0.700f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        PLAIN(19, 42.5f, 0.210f, -1000, -2000, 0, 1.49f, 0.50f, 1.00f, -2466, 0.179f, 0.00f, 0.00f, 0.00f, -1926, 0.100f, 0.00f, 0.00f, 0.00f, 0.250f, 1.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        PARKINGLOT(20, 8.3f, 1.000f, -1000, 0, 0, 1.65f, 1.50f, 1.00f, -1363, 0.008f, 0.00f, 0.00f, 0.00f, -1153, 0.012f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        SEWERPIPE(21, 1.7f, 0.800f, -1000, -1000, 0, 2.81f, 0.14f, 1.00f, 429, 0.014f, 0.00f, 0.00f, 0.00f, 1023, 0.021f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        UNDERWATER(22, 1.8f, 1.000f, -1000, -4000, 0, 1.49f, 0.10f, 1.00f, -449, 0.007f, 0.00f, 0.00f, 0.00f, 1700, 0.011f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 1.180f, 0.348f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        DRUGGED(23, 1.9f, 0.500f, -1000, 0, 0, 8.39f, 1.39f, 1.00f, -115, 0.002f, 0.00f, 0.00f, 0.00f, 985, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 1.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        DIZZY(24, 1.8f, 0.600f, -1000, -400, 0, 17.23f, 0.56f, 1.00f, -1713, 0.020f, 0.00f, 0.00f, 0.00f, -613, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 1.000f, 0.810f, 0.310f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        PSYCHOTIC(25, 1.0f, 0.500f, -1000, -151, 0, 7.56f, 0.91f, 1.00f, -626, 0.020f, 0.00f, 0.00f, 0.00f, 774, 0.030f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 4.000f, 1.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        // CASTLE PRESETS
        CASTLE_SMALLROOM(26, 8.3f, 0.890f, -1000, -800, -2000, 1.22f, 0.83f, 0.31f, -100, 0.022f, 0.00f, 0.00f, 0.00f, 600, 0.011f, 0.00f, 0.00f, 0.00f, 0.138f, 0.080f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_SHORTPASSAGE(26, 8.3f, 0.890f, -1000, -1000, -2000, 2.32f, 0.83f, 0.31f, -100, 0.007f, 0.00f, 0.00f, 0.00f, 200, 0.023f, 0.00f, 0.00f, 0.00f, 0.138f, 0.080f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_MEDIUMROOM(26, 8.3f, 0.930f, -1000, -1100, -2000, 2.04f, 0.83f, 0.46f, -400, 0.022f, 0.00f, 0.00f, 0.00f, 400, 0.011f, 0.00f, 0.00f, 0.00f, 0.155f, 0.030f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_LONGPASSAGE(26, 8.3f, 0.890f, -1000, -800, -2000, 3.42f, 0.83f, 0.31f, -100, 0.007f, 0.00f, 0.00f, 0.00f, 300, 0.023f, 0.00f, 0.00f, 0.00f, 0.138f, 0.080f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_LARGEROOM(26, 8.3f, 0.820f, -1000, -1100, -1800, 2.53f, 0.83f, 0.50f, -700, 0.034f, 0.00f, 0.00f, 0.00f, 200, 0.016f, 0.00f, 0.00f, 0.00f, 0.185f, 0.070f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_HALL(26, 8.3f, 0.810f, -1000, -1100, -1500, 3.14f, 0.79f, 0.62f, -1500, 0.056f, 0.00f, 0.00f, 0.00f, 100, 0.024f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_CUPBOARD(26, 8.3f, 0.890f, -1000, -1100, -2000, 0.67f, 0.87f, 0.31f, 300, 0.010f, 0.00f, 0.00f, 0.00f, 1100, 0.007f, 0.00f, 0.00f, 0.00f, 0.138f, 0.080f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        CASTLE_COURTYARD(26, 8.3f, 0.420f, -1000, -700, -1400, 2.13f, 0.61f, 0.23f, -1300, 0.160f, 0.00f, 0.00f, 0.00f, -300, 0.036f, 0.00f, 0.00f, 0.00f, 0.250f, 0.370f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        CASTLE_ALCOVE(26, 8.3f, 0.890f, -1000, -600, -2000, 1.64f, 0.87f, 0.31f, 00, 0.007f, 0.00f, 0.00f, 0.00f, 300, 0.034f, 0.00f, 0.00f, 0.00f, 0.138f, 0.080f, 0.250f, 0.000f, -5.0f, 5168.6f, 139.5f, 0.00f, 0x20),
        // FACTORY PRESETS
        FACTORY_ALCOVE(26, 1.8f, 0.590f, -1200, -200, -600, 3.14f, 0.65f, 1.31f, 300, 0.010f, 0.00f, 0.00f, 0.00f, 000, 0.038f, 0.00f, 0.00f, 0.00f, 0.114f, 0.100f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_SHORTPASSAGE(26, 1.8f, 0.640f, -1200, -200, -600, 2.53f, 0.65f, 1.31f, 0, 0.010f, 0.00f, 0.00f, 0.00f, 200, 0.038f, 0.00f, 0.00f, 0.00f, 0.135f, 0.230f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_MEDIUMROOM(26, 1.9f, 0.820f, -1200, -200, -600, 2.76f, 0.65f, 1.31f, -1100, 0.022f, 0.00f, 0.00f, 0.00f, 300, 0.023f, 0.00f, 0.00f, 0.00f, 0.174f, 0.070f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_LONGPASSAGE(26, 1.8f, 0.640f, -1200, -200, -600, 4.06f, 0.65f, 1.31f, 0, 0.020f, 0.00f, 0.00f, 0.00f, 200, 0.037f, 0.00f, 0.00f, 0.00f, 0.135f, 0.230f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_LARGEROOM(26, 1.9f, 0.750f, -1200, -300, -400, 4.24f, 0.51f, 1.31f, -1500, 0.039f, 0.00f, 0.00f, 0.00f, 100, 0.023f, 0.00f, 0.00f, 0.00f, 0.231f, 0.070f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_HALL(26, 1.9f, 0.750f, -1000, -300, -400, 7.43f, 0.51f, 1.31f, -2400, 0.073f, 0.00f, 0.00f, 0.00f, -100, 0.027f, 0.00f, 0.00f, 0.00f, 0.250f, 0.070f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_CUPBOARD(26, 1.7f, 0.630f, -1200, -200, -600, 0.49f, 0.65f, 1.31f, 200, 0.010f, 0.00f, 0.00f, 0.00f, 600, 0.032f, 0.00f, 0.00f, 0.00f, 0.107f, 0.070f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_COURTYARD(26, 1.7f, 0.570f, -1000, -1000, -400, 2.32f, 0.29f, 0.56f, -1300, 0.140f, 0.00f, 0.00f, 0.00f, -800, 0.039f, 0.00f, 0.00f, 0.00f, 0.250f, 0.290f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        FACTORY_SMALLROOM(26, 1.8f, 0.820f, -1000, -200, -600, 1.72f, 0.65f, 1.31f, -300, 0.010f, 0.00f, 0.00f, 0.00f, 500, 0.024f, 0.00f, 0.00f, 0.00f, 0.119f, 0.070f, 0.250f, 0.000f, -5.0f, 3762.6f, 362.5f, 0.00f, 0x20),
        // ICE PALACE PRESETS
        ICEPALACE_ALCOVE(26, 2.7f, 0.840f, -1000, -500, -1100, 2.76f, 1.46f, 0.28f, 100, 0.010f, 0.00f, 0.00f, 0.00f, -100, 0.030f, 0.00f, 0.00f, 0.00f, 0.161f, 0.090f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_SHORTPASSAGE(26, 2.7f, 0.750f, -1000, -500, -1100, 1.79f, 1.46f, 0.28f, -600, 0.010f, 0.00f, 0.00f, 0.00f, 100, 0.019f, 0.00f, 0.00f, 0.00f, 0.177f, 0.090f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_MEDIUMROOM(26, 2.7f, 0.870f, -1000, -500, -700, 2.22f, 1.53f, 0.32f, -800, 0.039f, 0.00f, 0.00f, 0.00f, 100, 0.027f, 0.00f, 0.00f, 0.00f, 0.186f, 0.120f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_LONGPASSAGE(26, 2.7f, 0.770f, -1000, -500, -800, 3.01f, 1.46f, 0.28f, -200, 0.012f, 0.00f, 0.00f, 0.00f, 200, 0.025f, 0.00f, 0.00f, 0.00f, 0.186f, 0.040f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_LARGEROOM(26, 2.9f, 0.810f, -1000, -500, -700, 3.14f, 1.53f, 0.32f, -1200, 0.039f, 0.00f, 0.00f, 0.00f, 000, 0.027f, 0.00f, 0.00f, 0.00f, 0.214f, 0.110f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_HALL(26, 2.9f, 0.760f, -1000, -700, -500, 5.49f, 1.53f, 0.38f, -1900, 0.054f, 0.00f, 0.00f, 0.00f, -400, 0.052f, 0.00f, 0.00f, 0.00f, 0.226f, 0.110f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_CUPBOARD(26, 2.7f, 0.830f, -1000, -600, -1300, 0.76f, 1.53f, 0.26f, 100, 0.012f, 0.00f, 0.00f, 0.00f, 600, 0.016f, 0.00f, 0.00f, 0.00f, 0.143f, 0.080f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_COURTYARD(26, 2.9f, 0.590f, -1000, -1100, -1000, 2.04f, 1.20f, 0.38f, -1000, 0.173f, 0.00f, 0.00f, 0.00f, -1000, 0.043f, 0.00f, 0.00f, 0.00f, 0.235f, 0.480f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        ICEPALACE_SMALLROOM(26, 2.7f, 0.840f, -1000, -500, -1100, 1.51f, 1.53f, 0.27f, -100, 0.010f, 0.00f, 0.00f, 0.00f, 300, 0.011f, 0.00f, 0.00f, 0.00f, 0.164f, 0.140f, 0.250f, 0.000f, -5.0f, 12428.5f, 99.6f, 0.00f, 0x20),
        // SPACE STATION PRESETS
        SPACESTATION_ALCOVE(26, 1.5f, 0.780f, -1000, -300, -100, 1.16f, 0.81f, 0.55f, 300, 0.007f, 0.00f, 0.00f, 0.00f, 000, 0.018f, 0.00f, 0.00f, 0.00f, 0.192f, 0.210f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_MEDIUMROOM(26, 1.5f, 0.750f, -1000, -400, -100, 3.01f, 0.50f, 0.55f, -800, 0.034f, 0.00f, 0.00f, 0.00f, 100, 0.035f, 0.00f, 0.00f, 0.00f, 0.209f, 0.310f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_SHORTPASSAGE(26, 1.5f, 0.870f, -1000, -400, -100, 3.57f, 0.50f, 0.55f, 0, 0.012f, 0.00f, 0.00f, 0.00f, 100, 0.016f, 0.00f, 0.00f, 0.00f, 0.172f, 0.200f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_LONGPASSAGE(26, 1.9f, 0.820f, -1000, -400, -100, 4.62f, 0.62f, 0.55f, 0, 0.012f, 0.00f, 0.00f, 0.00f, 200, 0.031f, 0.00f, 0.00f, 0.00f, 0.250f, 0.230f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_LARGEROOM(26, 1.8f, 0.810f, -1000, -400, -100, 3.89f, 0.38f, 0.61f, -1000, 0.056f, 0.00f, 0.00f, 0.00f, -100, 0.035f, 0.00f, 0.00f, 0.00f, 0.233f, 0.280f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_HALL(26, 1.9f, 0.870f, -1000, -400, -100, 7.11f, 0.38f, 0.61f, -1500, 0.100f, 0.00f, 0.00f, 0.00f, -400, 0.047f, 0.00f, 0.00f, 0.00f, 0.250f, 0.250f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_CUPBOARD(26, 1.4f, 0.560f, -1000, -300, -100, 0.79f, 0.81f, 0.55f, 300, 0.007f, 0.00f, 0.00f, 0.00f, 500, 0.018f, 0.00f, 0.00f, 0.00f, 0.181f, 0.310f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        SPACESTATION_SMALLROOM(26, 1.5f, 0.700f, -1000, -300, -100, 1.72f, 0.82f, 0.55f, -200, 0.007f, 0.00f, 0.00f, 0.00f, 300, 0.013f, 0.00f, 0.00f, 0.00f, 0.188f, 0.260f, 0.250f, 0.000f, -5.0f, 3316.1f, 458.2f, 0.00f, 0x20),
        // WOODEN GALLEON PRESETS
        WOODEN_ALCOVE(26, 7.5f, 1.000f, -1000, -1800, -1000, 1.22f, 0.62f, 0.91f, 100, 0.012f, 0.00f, 0.00f, 0.00f, -300, 0.024f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_SHORTPASSAGE(26, 7.5f, 1.000f, -1000, -1800, -1000, 1.75f, 0.50f, 0.87f, -100, 0.012f, 0.00f, 0.00f, 0.00f, -400, 0.024f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_MEDIUMROOM(26, 7.5f, 1.000f, -1000, -2000, -1100, 1.47f, 0.42f, 0.82f, -100, 0.049f, 0.00f, 0.00f, 0.00f, -100, 0.029f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_LONGPASSAGE(26, 7.5f, 1.000f, -1000, -2000, -1000, 1.99f, 0.40f, 0.79f, 000, 0.020f, 0.00f, 0.00f, 0.00f, -700, 0.036f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_LARGEROOM(26, 7.5f, 1.000f, -1000, -2100, -1100, 2.65f, 0.33f, 0.82f, -100, 0.066f, 0.00f, 0.00f, 0.00f, -200, 0.049f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_HALL(26, 7.5f, 1.000f, -1000, -2200, -1100, 3.45f, 0.30f, 0.82f, -100, 0.088f, 0.00f, 0.00f, 0.00f, -200, 0.063f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_CUPBOARD(26, 7.5f, 1.000f, -1000, -1700, -1000, 0.56f, 0.46f, 0.91f, 100, 0.012f, 0.00f, 0.00f, 0.00f, 100, 0.028f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_SMALLROOM(26, 7.5f, 1.000f, -1000, -1900, -1000, 0.79f, 0.32f, 0.87f, 00, 0.032f, 0.00f, 0.00f, 0.00f, -100, 0.029f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        WOODEN_COURTYARD(26, 7.5f, 0.650f, -1000, -2200, -1000, 1.79f, 0.35f, 0.79f, -500, 0.123f, 0.00f, 0.00f, 0.00f, -2000, 0.032f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 4705.0f, 99.6f, 0.00f, 0x3f),
        // SPORTS PRESETS
        SPORT_EMPTYSTADIUM(26, 7.2f, 1.000f, -1000, -700, -200, 6.26f, 0.51f, 1.10f, -2400, 0.183f, 0.00f, 0.00f, 0.00f, -800, 0.038f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        SPORT_SQUASHCOURT(26, 7.5f, 0.750f, -1000, -1000, -200, 2.22f, 0.91f, 1.16f, -700, 0.007f, 0.00f, 0.00f, 0.00f, -200, 0.011f, 0.00f, 0.00f, 0.00f, 0.126f, 0.190f, 0.250f, 0.000f, -5.0f, 7176.9f, 211.2f, 0.00f, 0x20),
        SPORT_SMALLSWIMMINGPOOL(26, 36.2f, 0.700f, -1000, -200, -100, 2.76f, 1.25f, 1.14f, -400, 0.020f, 0.00f, 0.00f, 0.00f, -200, 0.030f, 0.00f, 0.00f, 0.00f, 0.179f, 0.150f, 0.895f, 0.190f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x0),
        SPORT_LARGESWIMMINGPOO(26, 36.2f, 0.820f, -1000, -200, 0, 5.49f, 1.31f, 1.14f, -700, 0.039f, 0.00f, 0.00f, 0.00f, -600, 0.049f, 0.00f, 0.00f, 0.00f, 0.222f, 0.550f, 1.159f, 0.210f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x0),
        SPORT_GYMNASIUM(26, 7.5f, 0.810f, -1000, -700, -100, 3.14f, 1.06f, 1.35f, -800, 0.029f, 0.00f, 0.00f, 0.00f, -500, 0.045f, 0.00f, 0.00f, 0.00f, 0.146f, 0.140f, 0.250f, 0.000f, -5.0f, 7176.9f, 211.2f, 0.00f, 0x20),
        SPORT_FULLSTADIUM(26, 7.2f, 1.000f, -1000, -2300, -200, 5.25f, 0.17f, 0.80f, -2000, 0.188f, 0.00f, 0.00f, 0.00f, -1100, 0.038f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        SPORT_STADIUMTANNOY(26, 3.0f, 0.780f, -1000, -500, -600, 2.53f, 0.88f, 0.68f, -1100, 0.230f, 0.00f, 0.00f, 0.00f, -600, 0.063f, 0.00f, 0.00f, 0.00f, 0.250f, 0.200f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        // PREFAB PRESETS
        PREFAB_WORKSHOP(26, 1.9f, 1.000f, -1000, -1700, -800, 0.76f, 1.00f, 1.00f, 0, 0.012f, 0.00f, 0.00f, 0.00f, 100, 0.012f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x0),
        PREFAB_SCHOOLROOM(26, 1.86f, 0.690f, -1000, -400, -600, 0.98f, 0.45f, 0.18f, 300, 0.017f, 0.00f, 0.00f, 0.00f, 300, 0.015f, 0.00f, 0.00f, 0.00f, 0.095f, 0.140f, 0.250f, 0.000f, -5.0f, 7176.9f, 211.2f, 0.00f, 0x20),
        PREFAB_PRACTISEROOM(26, 1.86f, 0.870f, -1000, -800, -600, 1.12f, 0.56f, 0.18f, 200, 0.010f, 0.00f, 0.00f, 0.00f, 300, 0.011f, 0.00f, 0.00f, 0.00f, 0.095f, 0.140f, 0.250f, 0.000f, -5.0f, 7176.9f, 211.2f, 0.00f, 0x20),
        PREFAB_OUTHOUSE(26, 80.3f, 0.820f, -1000, -1900, -1600, 1.38f, 0.38f, 0.35f, -100, 0.024f, 0.00f, 0.00f, -0.00f, -400, 0.044f, 0.00f, 0.00f, 0.00f, 0.121f, 0.170f, 0.250f, 0.000f, -5.0f, 2854.4f, 107.5f, 0.00f, 0x0),
        PREFAB_CARAVAN(26, 8.3f, 1.000f, -1000, -2100, -1800, 0.43f, 1.50f, 1.00f, 0, 0.012f, 0.00f, 0.00f, 0.00f, 600, 0.012f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x1f),
        // for US developers, a caravan is the same as a trailer =o),
        // DOME AND PIPE PRESETS
        DOME_TOMB(26, 51.8f, 0.790f, -1000, -900, -1300, 4.18f, 0.21f, 0.10f, -825, 0.030f, 0.00f, 0.00f, 0.00f, 450, 0.022f, 0.00f, 0.00f, 0.00f, 0.177f, 0.190f, 0.250f, 0.000f, -5.0f, 2854.4f, 20.0f, 0.00f, 0x0),
        PIPE_SMALL(26, 50.3f, 1.000f, -1000, -900, -1300, 5.04f, 0.10f, 0.10f, -600, 0.032f, 0.00f, 0.00f, 0.00f, 800, 0.015f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 2854.4f, 20.0f, 0.00f, 0x3f),
        DOME_SAINTPAULS(26, 50.3f, 0.870f, -1000, -900, -1300, 10.48f, 0.19f, 0.10f, -1500, 0.090f, 0.00f, 0.00f, 0.00f, 200, 0.042f, 0.00f, 0.00f, 0.00f, 0.250f, 0.120f, 0.250f, 0.000f, -5.0f, 2854.4f, 20.0f, 0.00f, 0x3f),
        PIPE_LONGTHIN(26, 1.6f, 0.910f, -1000, -700, -1100, 9.21f, 0.18f, 0.10f, -300, 0.010f, 0.00f, 0.00f, 0.00f, -300, 0.022f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 2854.4f, 20.0f, 0.00f, 0x0),
        PIPE_LARGE(26, 50.3f, 1.000f, -1000, -900, -1300, 8.45f, 0.10f, 0.10f, -800, 0.046f, 0.00f, 0.00f, 0.00f, 400, 0.032f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 2854.4f, 20.0f, 0.00f, 0x3f),
        PIPE_RESONANT(26, 1.3f, 0.910f, -1000, -700, -1100, 6.81f, 0.18f, 0.10f, -300, 0.010f, 0.00f, 0.00f, 0.00f, 00, 0.022f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 2854.4f, 20.0f, 0.00f, 0x0),
        // OUTDOORS PRESETS
        OUTDOORS_BACKYARD(26, 80.3f, 0.450f, -1000, -1200, -600, 1.12f, 0.34f, 0.46f, -700, 0.069f, 0.00f, 0.00f, -0.00f, -300, 0.023f, 0.00f, 0.00f, 0.00f, 0.218f, 0.340f, 0.250f, 0.000f, -5.0f, 4399.1f, 242.9f, 0.00f, 0x0),
        OUTDOORS_ROLLINGPLAINS(26, 80.3f, 0.000f, -1000, -3900, -400, 2.13f, 0.21f, 0.46f, -1500, 0.300f, 0.00f, 0.00f, -0.00f, -700, 0.019f, 0.00f, 0.00f, 0.00f, 0.250f, 1.000f, 0.250f, 0.000f, -5.0f, 4399.1f, 242.9f, 0.00f, 0x0),
        OUTDOORS_DEEPCANYON(26, 80.3f, 0.740f, -1000, -1500, -400, 3.89f, 0.21f, 0.46f, -1000, 0.223f, 0.00f, 0.00f, -0.00f, -900, 0.019f, 0.00f, 0.00f, 0.00f, 0.250f, 1.000f, 0.250f, 0.000f, -5.0f, 4399.1f, 242.9f, 0.00f, 0x0),
        OUTDOORS_CREEK(26, 80.3f, 0.350f, -1000, -1500, -600, 2.13f, 0.21f, 0.46f, -800, 0.115f, 0.00f, 0.00f, -0.00f, -1400, 0.031f, 0.00f, 0.00f, 0.00f, 0.218f, 0.340f, 0.250f, 0.000f, -5.0f, 4399.1f, 242.9f, 0.00f, 0x0),
        OUTDOORS_VALLEY(26, 80.3f, 0.280f, -1000, -3100, -1600, 2.88f, 0.26f, 0.35f, -1700, 0.263f, 0.00f, 0.00f, -0.00f, -800, 0.100f, 0.00f, 0.00f, 0.00f, 0.250f, 0.340f, 0.250f, 0.000f, -5.0f, 2854.4f, 107.5f, 0.00f, 0x0),
        // MOOD PRESETS
        MOOD_HEAVEN(26, 19.6f, 0.940f, -1000, -200, -700, 5.04f, 1.12f, 0.56f, -1230, 0.020f, 0.00f, 0.00f, 0.00f, 200, 0.029f, 0.00f, 0.00f, 0.00f, 0.250f, 0.080f, 2.742f, 0.050f, -2.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        MOOD_HELL(26, 100.0f, 0.570f, -1000, -900, -700, 3.57f, 0.49f, 2.00f, -10000, 0.020f, 0.00f, 0.00f, 0.00f, 300, 0.030f, 0.00f, 0.00f, 0.00f, 0.110f, 0.040f, 2.109f, 0.520f, -5.0f, 5000.0f, 139.5f, 0.00f, 0x40),
        MOOD_MEMORY(26, 8.0f, 0.850f, -1000, -400, -900, 4.06f, 0.82f, 0.56f, -2800, 0.000f, 0.00f, 0.00f, 0.00f, 100, 0.000f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.474f, 0.450f, -10.0f, 5000.0f, 250.0f, 0.00f, 0x0),
        // DRIVING SIMULATION PRESETS
        DRIVING_COMMENTATOR(26, 3.0f, 0.000f, -1000, -500, -600, 2.42f, 0.88f, 0.68f, -1400, 0.093f, 0.00f, 0.00f, 0.00f, -1200, 0.017f, 0.00f, 0.00f, 0.00f, 0.250f, 1.000f, 0.250f, 0.000f, -10.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        DRIVING_PITGARAGE(26, 1.9f, 0.590f, -1000, -300, -500, 1.72f, 0.93f, 0.87f, -500, 0.000f, 0.00f, 0.00f, 0.00f, 200, 0.016f, 0.00f, 0.00f, 0.00f, 0.250f, 0.110f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x0),
        DRIVING_INCAR_RACER(26, 1.1f, 0.800f, -1000, 0, -200, 0.17f, 2.00f, 0.41f, 500, 0.007f, 0.00f, 0.00f, 0.00f, -300, 0.015f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 10268.2f, 251.0f, 0.00f, 0x20),
        DRIVING_INCAR_SPORTS(26, 1.1f, 0.800f, -1000, -400, 0, 0.17f, 0.75f, 0.41f, 0, 0.010f, 0.00f, 0.00f, 0.00f, -500, 0.000f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 10268.2f, 251.0f, 0.00f, 0x20),
        DRIVING_INCAR_LUXURY(26, 1.6f, 1.000f, -1000, -2000, -600, 0.13f, 0.41f, 0.46f, -200, 0.010f, 0.00f, 0.00f, 0.00f, 400, 0.010f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 10268.2f, 251.0f, 0.00f, 0x20),
        DRIVING_FULLGRANDSTAND(26, 8.3f, 1.000f, -1000, -1100, -400, 3.01f, 1.37f, 1.28f, -900, 0.090f, 0.00f, 0.00f, 0.00f, -1500, 0.049f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 10420.2f, 250.0f, 0.00f, 0x1f),
        DRIVING_EMPTYGRANDSTAND(26, 8.3f, 1.000f, -1000, 0, -200, 4.62f, 1.75f, 1.40f, -1363, 0.090f, 0.00f, 0.00f, 0.00f, -1200, 0.049f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.000f, -5.0f, 10420.2f, 250.0f, 0.00f, 0x1f),
        DRIVING_TUNNEL(26, 3.1f, 0.810f, -1000, -800, -100, 3.42f, 0.94f, 1.31f, -300, 0.051f, 0.00f, 0.00f, 0.00f, -300, 0.047f, 0.00f, 0.00f, 0.00f, 0.214f, 0.050f, 0.250f, 0.000f, -5.0f, 5000.0f, 155.3f, 0.00f, 0x20),
        // CITY PRESETS
        CITY_STREETS(26, 3.0f, 0.780f, -1000, -300, -100, 1.79f, 1.12f, 0.91f, -1100, 0.046f, 0.00f, 0.00f, 0.00f, -1400, 0.028f, 0.00f, 0.00f, 0.00f, 0.250f, 0.200f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        CITY_SUBWAY(26, 3.0f, 0.740f, -1000, -300, -100, 3.01f, 1.23f, 0.91f, -300, 0.046f, 0.00f, 0.00f, 0.00f, 200, 0.028f, 0.00f, 0.00f, 0.00f, 0.125f, 0.210f, 0.250f, 0.000f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        CITY_MUSEUM(26, 80.3f, 0.820f, -1000, -1500, -1500, 3.28f, 1.40f, 0.57f, -1200, 0.039f, 0.00f, 0.00f, -0.00f, -100, 0.034f, 0.00f, 0.00f, 0.00f, 0.130f, 0.170f, 0.250f, 0.000f, -5.0f, 2854.4f, 107.5f, 0.00f, 0x0),
        CITY_LIBRARY(26, 80.3f, 0.820f, -1000, -1100, -2100, 2.76f, 0.89f, 0.41f, -900, 0.029f, 0.00f, 0.00f, -0.00f, -100, 0.020f, 0.00f, 0.00f, 0.00f, 0.130f, 0.170f, 0.250f, 0.000f, -5.0f, 2854.4f, 107.5f, 0.00f, 0x0),
        CITY_UNDERPASS(26, 3.0f, 0.820f, -1000, -700, -100, 3.57f, 1.12f, 0.91f, -800, 0.059f, 0.00f, 0.00f, 0.00f, -100, 0.037f, 0.00f, 0.00f, 0.00f, 0.250f, 0.140f, 0.250f, 0.000f, -7.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        CITY_ABANDONED(26, 3.0f, 0.690f, -1000, -200, -100, 3.28f, 1.17f, 0.91f, -700, 0.044f, 0.00f, 0.00f, 0.00f, -1100, 0.024f, 0.00f, 0.00f, 0.00f, 0.250f, 0.200f, 0.250f, 0.000f, -3.0f, 5000.0f, 250.0f, 0.00f, 0x20),
        // MISC ROOMS
        DUSTYROOM(26, 1.8f, 0.560f, -1000, -200, -300, 1.79f, 0.38f, 0.21f, -600, 0.002f, 0.00f, 0.00f, 0.00f, 200, 0.006f, 0.00f, 0.00f, 0.00f, 0.202f, 0.050f, 0.250f, 0.000f, -10.0f, 13046.0f, 163.3f, 0.00f, 0x20),
        CHAPEL(26, 19.6f, 0.840f, -1000, -500, 0, 4.62f, 0.64f, 1.23f, -700, 0.032f, 0.00f, 0.00f, 0.00f, -200, 0.049f, 0.00f, 0.00f, 0.00f, 0.250f, 0.000f, 0.250f, 0.110f, -5.0f, 5000.0f, 250.0f, 0.00f, 0x3f),
        SMALLWATERROOM(26, 36.2f, 0.700f, -1000, -698, 0, 1.51f, 1.25f, 1.14f, -100, 0.020f, 0.00f, 0.00f, 0.00f, 300, 0.030f, 0.00f, 0.00f, 0.00f, 0.179f, 0.150f, 0.895f, 0.190f, -7.0f, 5000.0f, 250.0f, 0.00f, 0x0);

        private final int ulEnvironment;
        private final float flEnvironmentSize;
        private final float flEnvironmentDiffusion;
        private final int lRoom;
        private final int lRoomHF;
        private final int lRoomLF;
        private final float flDecayTime;
        private final float flDecayHFRatio;
        private final float flDecayLFRatio;
        private final int lReflections;
        private final float flReflectionsDelay;
        private final float vReflectionsPanX;
        private final float vReflectionsPanY;
        private final float vReflectionsPanZ;
        private final int lReverb;
        private final float flReverbDelay;
        private final float vReverbPanX;
        private final float vReverbPanY;
        private final float vReverbPanZ;
        private final float flEchoTime;
        private final float flEchoDepth;
        private final float flModulationTime;
        private final float flModulationDepth;
        private final float flAirAbsorptionHF;
        private final float flHFReference;
        private final float flLFReference;
        private final float flRoomRolloffFactor;
        private final int ulFlags;

        EFXPreset(
                int ulEnvironment,
                float flEnvironmentSize,
                float flEnvironmentDiffusion,
                int lRoom,
                int lRoomHF,
                int lRoomLF,
                float flDecayTime,
                float flDecayHFRatio,
                float flDecayLFRatio,
                int lReflections,
                float flReflectionsDelay,
                float vReflectionsPanX,
                float vReflectionsPanY,
                float vReflectionsPanZ,
                int lReverb,
                float flReverbDelay,
                float vReverbPanX,
                float vReverbPanY,
                float vReverbPanZ,
                float flEchoTime,
                float flEchoDepth,
                float flModulationTime,
                float flModulationDepth,
                float flAirAbsorptionHF,
                float flHFReference,
                float flLFReference,
                float flRoomRolloffFactor,
                int ulFlags
        ) {

            this.ulEnvironment = ulEnvironment;
            this.flEnvironmentSize = flEnvironmentSize;
            this.flEnvironmentDiffusion = flEnvironmentDiffusion;
            this.lRoom = lRoom;
            this.lRoomHF = lRoomHF;
            this.lRoomLF = lRoomLF;
            this.flDecayTime = flDecayTime;
            this.flDecayHFRatio = flDecayHFRatio;
            this.flDecayLFRatio = flDecayLFRatio;
            this.lReflections = lReflections;
            this.flReflectionsDelay = flReflectionsDelay;
            this.vReflectionsPanX = vReflectionsPanX;
            this.vReflectionsPanY = vReflectionsPanY;
            this.vReflectionsPanZ = vReflectionsPanZ;
            this.lReverb = lReverb;
            this.flReverbDelay = flReverbDelay;
            this.vReverbPanX = vReverbPanX;
            this.vReverbPanY = vReverbPanY;
            this.vReverbPanZ = vReverbPanZ;
            this.flEchoTime = flEchoTime;
            this.flEchoDepth = flEchoDepth;
            this.flModulationTime = flModulationTime;
            this.flModulationDepth = flModulationDepth;
            this.flAirAbsorptionHF = flAirAbsorptionHF;
            this.flHFReference = flHFReference;
            this.flLFReference = flLFReference;
            this.flRoomRolloffFactor = flRoomRolloffFactor;
            this.ulFlags = ulFlags;
        }
    }
}
