package com.belichenko.a.dzenview;


import android.view.animation.Interpolator;

/**
 * Created by Admin on 03.02.2017.
 */

public class ChainInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float t) {
        if (t>0.1f & t<=0.2f) return 0.2f;
        if (t>0.2f & t<=0.3f) return t*2-0.2f;
        if (t>0.3f & t<=0.4f) return 0.4f;
        if (t>0.4f & t<=0.5f) return t*2-0.4f;
        if (t>0.5f & t<=0.6f) return 0.6f;
        if (t>0.6f & t<=0.7f) return t*2-0.6f;
        if (t>0.7f & t<=0.8f) return 0.8f;
        if (t>0.8f & t<=0.9f) return t*2-0.8f;
        if (t>0.9f & t<=1f) return 1f;
        return t*2;
    }

}
