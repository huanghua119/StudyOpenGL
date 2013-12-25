
package com.huanghua.sample3_1;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    public static float[] mProjMatrix = new float[16]; // 4x4投影矩阵
    public static float[] mVMatrix = new float[16]; // 摄像机位置朝向的参建矩阵
    public static float[] mMVPMatrix; // 总变换矩阵

    int mProgram; // 自定义渲染管线着色器程序id
    int muMVPMatrixHandle; // 总变换矩阵引用
    int maPositionHandler; // 顶点位置属性引用
    int maColorHandler;// 顶点颜色属性引用
    String mVertexShader; // 顶点着色器代码脚本
    String mFragmentShader; // 片元着色器代码脚本
    static float[] mMMatrix = new float[16];// 具体物体的３Ｄ变换矩阵,包括旋转,平衡,绽放

    FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
    FloatBuffer mColorBuffer; // 顶点着色器数据缓冲
    int vCount = 0; // 顶点数量
    float xAngle = 0;// 绕x轴旋转的角度

    public Triangle(MyTDView mv) {
        initVertexData();
        initShader(mv);
    }

    /**
     * 自定义的初始化顶点数据的方法
     */
    public void initVertexData() {
        vCount = 3;
        final float UNIT_SIZE = 0.2f;// 设置单位长度
        float vertices[] = new float[] {
                -4 * UNIT_SIZE, 0, 0,
                0, -4 * UNIT_SIZE, 0,
                4 * UNIT_SIZE, 0, 0
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        float colors[] = new float[] {
                1, 1, 1, 0,
                0, 0, 1, 0,
                0, 1, 0, 0
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    /**
     * 创建并初始化着色器的方法
     * 
     * @param mv
     */
    private void initShader(MyTDView mv) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandler = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandler = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    /**
     * 产生最终变换矩阵的方法
     * 
     * @param spec
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMMatrix;
    }

    public void drawSelf() {
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);// 初始化变换矩阵
        Matrix.translateM(mMMatrix, 0, 0, 0, 1);// 设置沿z轴正向位移
        Matrix.rotateM(mMMatrix, 0, xAngle, 1, 0, 0);// 设置绕x轴旋转
        // 将顶点位置数据传送进渲染管线
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, Triangle.getFinalMatrix(mMMatrix), 0);
        // 将顶点颜色数据传送进渲染管线
        GLES20.glVertexAttribPointer(maPositionHandler, 3, GLES20.GL_FLOAT, false, 3 * 4,
                mVertexBuffer);
        GLES20.glVertexAttribPointer(maColorHandler, 4, GLES20.GL_FLOAT, false, 4 * 4, mColorBuffer);
        GLES20.glEnableVertexAttribArray(maPositionHandler);// 启用顶点位置数据
        GLES20.glEnableVertexAttribArray(maColorHandler);// 启用顶点着色数据
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);// 执行绘制
    }

}
