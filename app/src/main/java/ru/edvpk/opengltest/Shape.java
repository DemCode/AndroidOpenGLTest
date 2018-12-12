package ru.edvpk.opengltest;

import android.opengl.GLES20;
import android.support.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class Shape {
    private FloatBuffer mVertexBuffer;
    private ByteBuffer mDrawListBuffer;
    private int mProgram;
    private int mDrawListCount;
    private int mCoordsPerVertex;
    private int mColorsPerVertex;

    public Shape(@NonNull float coords[], @NonNull byte drawList[], int coordsPerVertex, int colorsPerVertex) {
        ByteBuffer vb = ByteBuffer.allocateDirect(coords.length * 4);
        vb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vb.asFloatBuffer();
        mVertexBuffer.put(coords);

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawList.length); // size of byte is 1
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb;
        mDrawListBuffer.put(drawList);

        mCoordsPerVertex = coordsPerVertex;
        mColorsPerVertex = colorsPerVertex;

        mDrawListCount = drawList.length;

        mProgram = ShaderUtils.getProgram();
    }

    public void draw(float[] mVPMatrix) {
        GLES20.glUseProgram(mProgram);

        int stride = (mCoordsPerVertex + mColorsPerVertex) * 4;

        int positionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandle, mCoordsPerVertex, GLES20.GL_FLOAT, false, stride, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        int colorHandle = GLES20.glGetAttribLocation(mProgram, "a_Color");
        mVertexBuffer.position(mCoordsPerVertex);
        GLES20.glVertexAttribPointer(colorHandle, mColorsPerVertex, GLES20.GL_FLOAT, false, stride, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(colorHandle);

        int matrixHandle = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mVPMatrix, 0);

        mDrawListBuffer.position(0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawListCount, GLES20.GL_UNSIGNED_BYTE, mDrawListBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
    }
}
