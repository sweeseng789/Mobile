package com.ss.mobileframework.GameAsset;

import android.graphics.Bitmap;
import android.graphics.Paint;

import com.ss.mobileframework.GamePanelSurfaceView;
import com.ss.mobileframework.Utility.Vector3;

import java.util.Random;

/**
 * Created by sweeseng789 on 6/12/2015.
 */
public class Item extends GameObject
{
    public enum TYPE
    {
        s_DRUGS,
        s_CABBAGE,
        s_TOTAL
    };

    //==============VARIABLES==============//
    private TYPE type;
    private float respawnTime;
    private float speed;
    public float MinMaxSpeed;
    private Paint paint;

    //==============CONSTRUCTOR==============//
    public Item(TYPE type, Bitmap bitmap)
    {
        this.type = type;
        this.bitmap = bitmap;
        init();
        paint = new Paint();
    }
    //==============Init==============//
    public void init()
    {
        active = false;
        MinMaxSpeed = 200;
        randVars();
        respawnTime = (float) (Math.random() * 15 + 5); //from 5 to 15
    }

    //==============GETTER==============//
    public boolean isCabbage()
    {
        if(type == TYPE.s_CABBAGE)
            return  true;
        return false;
    }
    public boolean isDrug()
    {
        if(type == TYPE.s_DRUGS)
            return  true;
        return false;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public void update(double dt)
    {
       // bounds.contains((int)pos.x, (int)pos.y, bitmap.getWidth(), bitmap.getHeight());
        if(dt < 1)
        {
            MinMaxSpeed += dt * 10;
            if(getActive())
            {
                pos.y += speed * dt;
                if(pos.y > screenHeight + 100)
                {
                    active = false;
                    randVars();
                }
            }
            else
            {
                respawnTime -= dt;
                if(respawnTime < 0)
                {
                    active = true;
                }
            }
        }
    }

    public void randVars()
    {
        if(type == TYPE.s_CABBAGE)
        {
            pos.x = 0;
            while (pos.x < screenWidth/7.2 || pos.x > screenWidth - screenWidth/7.2 - getBitmap().getWidth()) //while it spawns on the pavement
                pos.x = (float) Math.random() * screenWidth + 1;
            pos.y = -50;
            respawnTime = (float) (Math.random() * 10 + 5);
            while(speed < MinMaxSpeed - 50 || speed > MinMaxSpeed + 50)
                speed = (float) (Math.random() * (MinMaxSpeed) + (MinMaxSpeed-50)); //from 600 to 300
        }
        else if(type == TYPE.s_DRUGS)
        {
            pos.x = 0;
            while (pos.x < screenWidth/7.2 || pos.x > screenWidth - screenWidth/7.2 - getBitmap().getWidth())
                pos.x = (float) Math.random() * screenWidth + 1;
            pos.y = -50;
            respawnTime = (float) (Math.random() * 5 + 1);
            while(speed < MinMaxSpeed - 50 || speed > MinMaxSpeed + 50)
                speed = (float) (Math.random() * (MinMaxSpeed+50) + (MinMaxSpeed-50));
        }
    }
}
