package com.belichenko.a.dzenview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Belichenko Anton on 30.01.17.
 * mailto: a.belichenko@gmail.com
 */

public class DzenView extends View {

    private int mViewDiameter;
    private Paint mPaint = new Paint();
    private Point mCenter;
    private RotateAnimation mRotate;
    private GestureDetector mGestureDetector;

    @ColorInt private int mFirstColor = 0xFF000000;
    @ColorInt private int mSecondColor = 0xDDDDDDDD;

    public DzenView(Context context) {
        super(context);
        initView();
    }

    public DzenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DzenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mGestureDetector = new GestureDetector(getContext(), new GestureListener(this, getContext()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateViewDiameterAndCenter();
        if (mViewDiameter < 40) return; // too small to draw
        mPaint.setColor(mFirstColor);
        canvas.drawCircle((float) mCenter.x, (float) mCenter.y, (float) mViewDiameter / 2, mPaint);
        mPaint.setColor(mSecondColor);
        canvas.drawCircle((float) mCenter.x, (float) mCenter.y, getSecondCircleRadius(), mPaint);
        mPaint.setColor(mFirstColor);
        canvas.drawCircle((float) mCenter.x, (float) mCenter.y - mViewDiameter / 4, getInnerCircleRadius(), mPaint);
        canvas.drawCircle((float) mCenter.x, (float) mCenter.y + mViewDiameter / 4, getInnerCircleRadius(), mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    private float getSecondCircleRadius() {
        return (mViewDiameter - percentFromViewDiametr(6, 2)) / 2;
    }

    private float getInnerCircleRadius() {
        return percentFromViewDiametr(12, 10) / 2;
    }

    private float percentFromViewDiametr(int persent, float minValue) {
        float result = (mViewDiameter / 100f) * persent;
        if (minValue > 0 && result < minValue) return minValue;
        return result;
    }

    private int getMaxPadding() {
        int padding = Math.max(getPaddingTop(), Math.max(getPaddingBottom(), Math.max(getPaddingLeft(), getPaddingRight())));
        return padding < 0 ? 0 : padding;
    }

    private void calculateViewDiameterAndCenter() {
        mViewDiameter = Math.min(getHeight() - getPaddingTop() - getPaddingBottom(), getWidth() - getPaddingLeft() - getPaddingRight());
        if (mViewDiameter == 0) {
            mCenter = new Point(0, 0);
        } else {
            mCenter = new Point(mViewDiameter / 2 + getPaddingLeft(), mViewDiameter / 2 + getPaddingTop());
            setPivotX(mCenter.x);
            setPivotY(mCenter.y);
        }
        Log.d("Tag", "Diameter = " + String.valueOf(mViewDiameter) + ", Center = " + mCenter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = mGestureDetector.onTouchEvent(event);
        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        return true;
    }

    protected void startRotation() {
        mRotate = new RotateAnimation(0, 360,
                Animation.ABSOLUTE, mCenter.x, Animation.ABSOLUTE, mCenter.y);
        mRotate.setInterpolator(new BounceInterpolator());
        mRotate.setDuration(2000);
        mRotate.setRepeatCount(Animation.INFINITE);
        startAnimation(mRotate);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

}
