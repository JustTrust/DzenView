package com.belichenko.a.dzenview;


import android.view.animation.Interpolator;

/**
 * Created by Admin on 03.02.2017.
 */

public class MechInterpolator implements Interpolator {

    // pow(x,2)*8;pow((x - 0.27),2)*8+0.25;pow((x - 0.53),2)*8+0.5;pow((x - 0.8),2)*7+0.75;
    @Override
    public float getInterpolation(float t) {
        if (t < 0.25f) return bounce(t);
        else if (t < 0.5f) return bounce(t - 0.27f) + 0.25f;
        else if (t < 0.75f) return bounce(t - 0.53f) + 0.5f;
        else return bounce(t - 0.8f) + 0.75f;
    }

    private static float bounce(float t) {
        return t * t * 8.0f;
    }
}
