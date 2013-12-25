
package com.huanghua.sample5_1;

import android.opengl.Matrix;

/**
 * 存储系统矩阵状态的类
 * 
 * @author huanghua
 */
public class MatrixState {

    private static float[] mProjMatrix = new float[16]; // ４x４矩阵 投影用
    private static float[] mVMatrix = new float[16]; // 摄像机位置朝向9参数矩阵
    private static float[] mMVPMatrix; // 最终的总变换矩阵

    /**
     * 设置摄像机的方法
     * 
     * @param cx 摄像机位置的x,y,z坐标
     * @param cy
     * @param cz
     * @param tx 观察目标点的x,y,z坐标
     * @param ty
     * @param tz
     * @param upx up向量在x,y,z轴上的分量
     * @param upy
     * @param upz
     */
    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz,
            float upx, float upy, float upz) {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    /**
     * 设置正交投影的方法
     * 
     * @param left near面的left, right
     * @param right
     * @param bottom near面的bottom, top
     * @param top
     * @param near near面,far面的视频距离
     * @param far
     */
    public static void setProjectOrtho(float left, float right, float bottom, float top,
            float near, float far) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 设置透视投影的方法
     * 
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    public static void setProjectFrustum(float left, float right, float bottom, float top,
            float near, float far) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 生成物体总变换矩阵的方法
     * 
     * @param spec
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        // 将摄像机矩阵乘以变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        // 将投影矩阵乘以上一步的结果矩阵得到最终变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
