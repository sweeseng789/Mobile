package com.ss.mobileframework;

import android.os.Handler;
import android.os.Looper;


/**
 * Created by tim on 17/12/2015.
 */
public class Alerts{
    private GamePanelSurfaceView game;
    public Alerts(GamePanelSurfaceView game)
    {
        this.game = game;
    }
    public void runAlert() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                game.alert.show();
            }
        }, 1000);
    }

}
