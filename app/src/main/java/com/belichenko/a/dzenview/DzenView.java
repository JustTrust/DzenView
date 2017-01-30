package com.belichenko.a.dzenview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Belichenko Anton on 30.01.17.
 * mailto: a.belichenko@gmail.com
 */

public class DzenView extends View {

    private int maxPadding;

    public DzenView(Context context) {
        super(context);
    }

    public DzenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DzenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Point center = getViewCenter();
        maxPadding = getMaxPadding();
        Paint paint = new Paint();

    }

    private Point getViewCenter() {
        return new Point(getWidth() / 2, getHeight() / 2);
    }

    private int getMaxPadding() {
        int padding = Math.max(getPaddingTop(), Math.max(getPaddingBottom(), Math.max(getPaddingLeft(), getPaddingRight())));
        return padding < 0 ? 0 : padding;
    }
}
