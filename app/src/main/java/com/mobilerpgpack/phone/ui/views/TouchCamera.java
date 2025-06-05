package com.mobilerpgpack.phone.ui.views;

import static org.libsdl.app.SDLSurface.fixedHeight;
import static org.libsdl.app.SDLSurface.fixedWidth;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import org.libsdl.app.SDLActivity;

public class TouchCamera extends View {

    private float mWidth, mHeight;

    public TouchCamera(Context context) {
        super(context);
    }

    public TouchCamera(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (fixedWidth > 0) {
            float myAspect = 1.0f * fixedWidth / fixedHeight;
            float resultWidth = widthSize;
            float resultHeight = resultWidth / myAspect;
            if (resultHeight > heightSize) {
                resultHeight = heightSize;
                resultWidth = resultHeight * myAspect;
            }

            mWidth = resultWidth;
            mHeight = resultHeight;
            setMeasuredDimension((int) resultWidth, (int) resultHeight);
        } else {
            mWidth = widthSize;
            mHeight = heightSize;
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("CALLED", event.toString());
        /* Ref: http://developer.android.com/training/gestures/multi.html */
        int touchDevId = event.getDeviceId();
        final int pointerCount = event.getPointerCount();
        int action = event.getActionMasked();
        int pointerFingerId;
        int i = -1;
        float x, y, p;

        /*
         * Prevent id to be -1, since it's used in SDL internal for synthetic events
         * Appears when using Android emulator, eg:
         *  adb shell input mouse tap 100 100
         *  adb shell input touchscreen tap 100 100
         */
        if (touchDevId < 0) {
            touchDevId -= 1;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                for (i = 0; i < pointerCount; i++) {
                    pointerFingerId = event.getPointerId(i);
                    x = event.getX(i) / mWidth;
                    y = event.getY(i) / mHeight;
                    p = event.getPressure(i);
                    if (p > 1.0f) {
                        // may be larger than 1.0f on some devices
                        // see the documentation of getPressure(i)
                        p = 1.0f;
                    }
                    SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_DOWN:
                // Primary pointer up/down, the index is always zero
                i = 0;
                /* fallthrough */
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_DOWN:
                // Non primary pointer up/down
                if (i == -1) {
                    i = event.getActionIndex();
                }

                pointerFingerId = event.getPointerId(i);
                x = event.getX(i) / mWidth;
                y = event.getY(i) / mHeight;
                p = event.getPressure(i);
                if (p > 1.0f) {
                    // may be larger than 1.0f on some devices
                    // see the documentation of getPressure(i)
                    p = 1.0f;
                }
                SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x, y, p);
                break;

            case MotionEvent.ACTION_CANCEL:
                for (i = 0; i < pointerCount; i++) {
                    pointerFingerId = event.getPointerId(i);
                    x = event.getX(i) / mWidth;
                    y = event.getY(i) / mHeight;
                    p = event.getPressure(i);
                    if (p > 1.0f) {
                        // may be larger than 1.0f on some devices
                        // see the documentation of getPressure(i)
                        p = 1.0f;
                    }
                    SDLActivity.onNativeTouch(touchDevId, pointerFingerId, MotionEvent.ACTION_UP, x, y, p);
                }
                break;

            default:
                break;
        }

        return true;
    }
}