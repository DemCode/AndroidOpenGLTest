package ru.edvpk.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer mRenderer;
    private static final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = x;
                mPreviousY = y;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mPreviousX = x;
                mPreviousY = y;

                mRenderer.setViewAngleY(mRenderer.getViewAngleY() + dx * TOUCH_SCALE_FACTOR);
                mRenderer.setViewAngleZ(mRenderer.getViewAngleZ() + dy * TOUCH_SCALE_FACTOR);
        }

        return true; //super.onTouchEvent(event);
    }
}
