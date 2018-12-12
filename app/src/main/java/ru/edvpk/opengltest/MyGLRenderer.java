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
    private float viewAngleX = 0.0f;
    private float viewAngleY = 270.0f;
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
        viewAngleX = Math.max(Math.min(value, 80.0f), -80.0f);
    }

    public float getViewAngleY() {
        return viewAngleY;
    }

    public void setViewAngleY(float value) {
        viewAngleY = value % 360.0f;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mShape = new Shape(new float[] {
                 0.0f,  0.3f, -1.0f, 0.20f, 0.25f, 0.85f, // 0
                -0.5f, -0.4f,  0.0f, 0.60f, 0.35f, 0.25f, // 1
                 0.5f, -0.4f,  0.5f, 0.50f, 0.35f, 0.40f, // 2
                -0.2f,  0.8f,  0.0f, 0.70f, 0.35f, 0.55f, // 3
                -0.5f, -0.4f,  0.0f, 0.80f, 0.80f, 0.80f, // 4
        }, new byte[] { 0, 1, 2, 4, 2, 3 }, 3, 3);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 1, 5);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glEnable(GLES20.GL_CULL_FACE);

        Matrix.setLookAtM(mViewMatrix, 0,
                0.0f, 0.0f, -3.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1, 0.0f);

        float[] rotationXMatrix = new float[16];
        Matrix.setRotateM(rotationXMatrix, 0, viewAngleX, 1, 0, 0);
        float[] rotationYMatrix = new float[16];
        Matrix.setRotateM(rotationYMatrix, 0, viewAngleY, 0, 1, 0);
        float[] rotationMatrix = new float[16];
        Matrix.multiplyMM(rotationMatrix, 0, rotationXMatrix, 0, rotationYMatrix, 0);
        float[] resultMatrix = new float[16];
        Matrix.multiplyMM(resultMatrix, 0, mViewMatrix, 0, rotationMatrix, 0);

        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, resultMatrix, 0);
        mShape.draw(mVPMatrix);
    }
}
