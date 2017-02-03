package com.belichenko.a.dzenview;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Belichenko Anton on 02.02.17.
 * mailto: a.belichenko@gmail.com
 */

class GestureListener extends GestureDetector.SimpleOnGestureListener {

    DzenView mDzenView;
    Context mContext;

    public GestureListener(DzenView view, Context context) {
        mDzenView = view;
        mContext = context;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        mDzenView.onLongPress();
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mDzenView.onDoubleTap();
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mDzenView.onSingleTap();
        return super.onSingleTapConfirmed(e);
    }

}
