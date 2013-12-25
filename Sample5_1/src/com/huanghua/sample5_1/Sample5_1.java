
package com.huanghua.sample5_1;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Sample5_1 extends Activity {

    MySurfaceView mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mview = new MySurfaceView(this);
        mview.requestFocus();
        mview.setFocusableInTouchMode(true);
        setContentView(mview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mview.onResume();
    }
}
