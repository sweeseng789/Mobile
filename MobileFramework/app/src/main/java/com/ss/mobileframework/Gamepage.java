package com.ss.mobileframework;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by 142128G on 11/23/2015.
 */
public class Gamepage extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Making it fullscreen window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Set GamePanelSurfaceView as the View
        setContentView(new GamePanelSurfaceView(this));
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}
