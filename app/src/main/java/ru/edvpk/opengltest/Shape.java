package ru.edvpk.opengltest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;

class Shape {
    private FloatBuffer mVertexBuffer;
    private ByteBuffer mDrawListBuffer;
    private int mProgram;
    private int mVertexCount;
    private int mDrawListCount;
    private static final float[] color = { 0.60f, 0.35f, 0.85f, 1.0f };

    private final static int COORDS_PER_VERTEX = 3;
    private final static int STRIDE = COORDS_PER_VERTEX * 4;

    private final String vertexShaderCode =
            "attribute vec4 a_Position;" +
            "uniform mat4 u_Matrix;" +
            "void main() {" +
            "   gl_Position = u_Matrix * a_Position;" +
            "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 u_Color;" +
            "void main() {" +
            "   gl_FragColor = u_Color;" +
            "}";

    public Shape(float coords[], byte drawList[]) {
        ByteBuffer vb = ByteBuffer.allocateDirect(coords.length * 4);
        vb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vb.asFloatBuffer();
        mVertexBuffer.put(coords);
        mVertexBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawList.length * 1);
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb; //.asShortBuffer();
        mDrawListBuffer.put(drawList);
        mDrawListBuffer.position(0);

        mVertexCount = coords.length / COORDS_PER_VERTEX;
        mDrawListCount = drawList.length;

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw(float[] mVPMatrix) {
        GLES20.glUseProgram(mProgram);

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0 /* STRIDE */, mVertexBuffer);

        int matrixHandle = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mVPMatrix, 0);

        int colorHandle = GLES20.glGetUniformLocation(mProgram, "u_Color");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawListCount, GLES20.GL_UNSIGNED_BYTE, mDrawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
