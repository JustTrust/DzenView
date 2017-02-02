package com.belichenko.a.dzenview;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Belichenko Anton on 02.02.17.
 * mailto: a.belichenko@gmail.com
 */

class GestureListener extends GestureDetector.SimpleOnGestureListener {

    DzenView mDzenView;
    Context mContext;

    public GestureListener(DzenView view, Context context){
        mDzenView = view;
        mContext = context;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        Toast.makeText(mContext, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(mContext, "onDoubleTap", Toast.LENGTH_SHORT).show();
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Toast.makeText(mContext, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
        return super.onSingleTapConfirmed(e);
    }

}
