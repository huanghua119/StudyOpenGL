
package com.huanghua.sample3_1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyTDView extends GLSurfaceView {

    final float ANGLE_SPAN = 0.375f;
    RotateThread rthread;
    SceneRenderer mRenderer;

    public MyTDView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);// 使用OpenGL ES 2.0需要设置该值为2
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);// 设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        Triangle tle;

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            tle.drawSelf();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height); // 设置视口
            float ratio = (float) width / height;// 计算屏幕的宽度和高度比例
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10); // 设置透视投影
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);// 设置摄像机
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0, 0, 0, 1.0f);// 设置屏幕背景色
            tle = new Triangle(MyTDView.this);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            rthread = new RotateThread();
            rthread.start();
        }
    }

    public class RotateThread extends Thread {
        public boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                mRenderer.tle.xAngle = mRenderer.tle.xAngle + ANGLE_SPAN;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
