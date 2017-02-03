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
    private double mStartAngle;
    private GestureDetector mGestureDetector;
    private AnimationState mLastAnimation = AnimationState.NONE;
    private RotateAnimation mRotateAnimation;
    private DzenViewListener mListener;

    @ColorInt private int mFirstColor = 0xFF000000;
    @ColorInt private int mSecondColor = 0xDDDDDDDD;
    @ColorInt private int mTherdColor = 0xDDDD0000;

    enum AnimationState {FORWARD, BACKWARD, NONE}

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

    public void setListener(DzenViewListener listener) {
        mListener = listener;
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
        mPaint.setColor(mTherdColor);
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
        int[] topLeft = new int[2];
        getLocationOnScreen(topLeft);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float startX = event.getRawX() - topLeft[0];
                float startY = event.getRawY() - topLeft[1];
                mStartAngle = getAngel(startX, startY);
                Log.d("Ang", "start Angle = " + mStartAngle);
                return true;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getRawX() - topLeft[0];
                float currentY = event.getRawY() - topLeft[1];
                Log.d("Ang", "current X, Y = " + currentX + ", " + currentY);

                double currentAngle = getAngel(currentX, currentY);
                if (mListener != null) {
                    mListener.onAngelChanged(currentAngle);
                }

                //rotateView((float) (currentAngle - mStartAngle));
                mStartAngle = currentAngle;
                return true;
            case MotionEvent.ACTION_UP:
                return false;
            default:
                return false;
        }
    }

    private void rotateView(float angel) {
        setRotation(angel);
    }

    private double getAngel(float x, float y) {
        float currentX = x - (float) mCenter.x;
        float currentY = y - (float) mCenter.y;
        if (currentX == 0) return y > 0 ? 180 : 0;
        double a = Math.atan(currentY / currentX) / Math.PI * 180;
        a = (currentX > 0) ? a + 90 : a + 270;
        return a;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
        mLastAnimation = AnimationState.NONE;
    }

    public void onLongPress() {

    }

    public void onDoubleTap() {

    }

    public void onSingleTap() {
        changeAnimation();
    }

    private void changeAnimation() {
        Log.d("Anim", " Direction = " + mLastAnimation);
        mStartAngle = 0;
        if (mRotateAnimation != null && !mRotateAnimation.hasEnded()) {
            mRotateAnimation.setRepeatCount(0);
        } else {
            switch (mLastAnimation) {
                case FORWARD:
                    getRotateAnimation(false);
                    mLastAnimation = AnimationState.BACKWARD;
                    break;
                default:
                    getRotateAnimation(true);
                    mLastAnimation = AnimationState.FORWARD;
                    break;
            }
            startAnimation(mRotateAnimation);
        }
    }

    protected void getRotateAnimation(boolean forward) {
        mRotateAnimation = new RotateAnimation(forward ? 0 : 360, forward ? 360 : 0,
                Animation.ABSOLUTE, mCenter.x, Animation.ABSOLUTE, mCenter.y);
        mRotateAnimation.setInterpolator(new BounceInterpolator());
        mRotateAnimation.setDuration(2000);
        mRotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    public interface DzenViewListener {
        void onAngelChanged(double angel);
    }

}
