package com.wyozi.jaysound;

import com.wyozi.jaysound.adapter.JayVec3f;

class ThrowawayVec3f implements JayVec3f {
    private final float x, y, z;

    ThrowawayVec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public float getJayX() {
        return x;
    }

    @Override
    public float getJayY() {
        return y;
    }

    @Override
    public float getJayZ() {
        return z;
    }
}