package ru.edvpk.opengltest;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MyGLRenderer implements GLSurfaceView.Renderer {
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mVPMatrix = new float[16];
    private float viewAngleX = 270.0f;
    private float viewAngleY = 0.0f;
    private Shape mShape;

    static int loadShader(int type, String code) {
        int id = GLES20.glCreateShader(type);
        GLES20.glShaderSource(id, code);
        GLES20.glCompileShader(id);
        return id;
    }

    public float getViewAngleX() {
        return viewAngleX;
    }

    public void setViewAngleX(float value) {
        viewAngleX = value % 360.0f;
    }

    public float getViewAngleY() {
        return viewAngleY;
    }

    public void setViewAngleY(float value) {
        viewAngleY = Math.max(Math.min(value, 90.0f), -90.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mShape = new Shape(new float[] {
                 0.0f,  0.3f, -1.0f, // 0
                -0.5f, -0.4f,  0.0f, // 1
                 0.5f, -0.4f,  0.5f, // 2
                -0.2f,  0.8f,  0.0f  // 3
        }, new byte[] { 0, 1, 2, 0, 1, 3 });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        float eyeDistance = 3.0f;
        float eyeX = eyeDistance * (float) Math.cos(Math.toRadians(viewAngleX));
        float eyeY = eyeDistance * (float) Math.sin(Math.toRadians(viewAngleY));
        float eyeZ = eyeDistance * (float) Math.sin(Math.toRadians(viewAngleX) * (float) Math.cos(Math.toRadians(viewAngleY)));
        Matrix.setLookAtM(mViewMatrix, 0,
                eyeX, eyeY, eyeZ,
                0.0f, 0.0f, 0.0f,
                0.0f, eyeDistance, 0.0f);
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        mShape.draw(mVPMatrix);
    }
}
