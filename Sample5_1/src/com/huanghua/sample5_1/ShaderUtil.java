
package com.huanghua.sample5_1;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ShaderUtil {

    /**
     * 加载指定着色器和方法
     * 
     * @param shaderType 着色器类型
     * @param source 着色器的脚本字符串
     * @return
     */
    public static final int loadShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType); // 创建一个shader, 并记录其id
        if (shader != 0) {
            GLES20.glShaderSource(shader, source); // 加载着色器的源代码
            GLES20.glCompileShader(shader); // 编译
            int[] compiled = new int[1];
            // 获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
                Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 创建着色器程序
     * 
     * @param vertexSource
     * @param fragmentSource
     * @return
     */
    public static final int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource); // 加载顶点着色器
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource); // 加载片元着色器
        if (pixelShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram(); // 创建程序
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGlError("glAttachShader");
            GLES20.glAttachShader(program, pixelShader);
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program); // 链接程序
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e("ES20_ERROR", "Could not link program");
                Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
                program = 0;
            }
        }
        return program;
    }

    /**
     * 检查每一步操作是否有错误的方法
     * 
     * @param op
     */
    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("ES20_ERROR", op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    /**
     * 从sh脚本中加载着色器内容的方法
     * 
     * @param fname
     * @param r
     * @return
     */
    public static String loadFromAssetsFile(String fname, Resources r) {
        String result = null;
        try {
            InputStream in = r.getAssets().open(fname);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
