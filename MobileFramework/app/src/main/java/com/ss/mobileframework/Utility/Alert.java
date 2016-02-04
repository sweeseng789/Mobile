package com.ss.mobileframework.Utility;

import android.os.Looper;

import com.ss.mobileframework.GamePanelSurfaceView;
import com.ss.mobileframework.ShopPage.View_Shop;

import android.os.Handler;

/**
 * Created by sweeseng789 on 18/1/2016.
 */
public class Alert
{
    //============== VARIABLES ==============//
    boolean showAlert = false;
    boolean showed = false;
    GamePanelSurfaceView Game;
    View_Shop shop;

    //============== CONSTRUCTOR ==============//
    public Alert(GamePanelSurfaceView Game)
    {
        this.Game = Game;
        showAlert = false;
        showed = false;
    }

    public Alert(View_Shop shop)
    {
        this.shop = shop;
        showAlert = false;
        showed = false;
    }

    //============== SETTER ==============//
    public void RunAlert()
    {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Game.alertDialog.show();
            }
        }, 0);
    }

    public void RunAlertShop()
    {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                shop.alertDialog.show();
            }
        }, 0);
    }

    public void setShowed(boolean showed)
    {
        this.showed = showed;
    }

    public void setShowAlert(boolean showAlert)
    {
        this.showAlert = showAlert;
    }

    //============== GETTER ==============//
    public boolean getShowed()
    {
        return showed;
    }

    public boolean getShowAlert()
    {
        return showAlert;
    }
}
