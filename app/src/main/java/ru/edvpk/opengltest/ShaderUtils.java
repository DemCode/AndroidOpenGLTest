package ru.edvpk.opengltest;

import android.opengl.GLES20;

public final class ShaderUtils {
    private static int mProgram = 0;

    public static String getVertexShaderText() {
        return "" +
                "attribute vec4 a_Position;" +
                "attribute vec4 a_Color;" +
                "uniform mat4 u_Matrix;" +
                "varying vec4 v_Color;" +
                "void main() {" +
                "    gl_Position = u_Matrix * a_Position;" +
                "    v_Color = a_Color;" +
                "}";
    }

    public static String getFragmentShaderText() {
        return "" +
                "precision mediump float;" +
                "varying vec4 v_Color;" +
                "void main() {" +
                "    gl_FragColor = v_Color;" +
                "}";
    }

    public static int getProgram() {
        if (mProgram == 0) {
            String vertexShaderCode = getVertexShaderText();
            String fragmentShaderCode = getFragmentShaderText();

            int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexShader);
            GLES20.glAttachShader(mProgram, fragmentShader);
            GLES20.glLinkProgram(mProgram);
        }

        return mProgram;
    }
}
