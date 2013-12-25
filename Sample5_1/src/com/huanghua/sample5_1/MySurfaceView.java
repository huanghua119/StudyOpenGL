
package com.huanghua.sample5_1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private SceneRenderer mRenderer;
    private float mPreviousY;
    private float mPreviousX;

    public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2);// 使用OpenGL ES 2.0需要设置该值为2
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);// 设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                for (SixPointedStar h : mRenderer.ha) {
                    h.yAngle += dx * TOUCH_SCALE_FACTOR;
                    h.xAngle += dy * TOUCH_SCALE_FACTOR;
                }
                break;
        }
        mPreviousY = y;
        mPreviousX = x;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {

        SixPointedStar[] ha = new SixPointedStar[6];

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            for (SixPointedStar h : ha) {
                h.drawSelf();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height); // 设置视口
            float ratio = (float) width / height;// 计算屏幕的宽度和高度比例
            // 设置正交投影
            // MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
            // 设置透视投影
            MatrixState.setProjectFrustum(-ratio * 0.4f, ratio * 0.4f, -1 * 0.4f, 1 * 0.4f,
                    1, 50);
            // 设置摄像机
            MatrixState.setCamera(0, 0, 3f, 0, 0, 0f, 0f, 1.0f, 0.0f);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);// 设置屏幕背景色
            for (int i = 0; i < ha.length; i++) {
                ha[i] = new SixPointedStar(MySurfaceView.this, 0.2f, 0.5f, -0.3f * i);
            }
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);// 打开深度检测
        }
    }

}
