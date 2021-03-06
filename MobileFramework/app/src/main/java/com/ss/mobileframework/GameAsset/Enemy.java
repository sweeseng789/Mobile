package com.ss.mobileframework.GameAsset;

import android.graphics.Bitmap;

/**
 * Created by ShingLiya on 8/12/2015.
 */
public class Enemy extends GameObject
{
    //==============VARIABLES==============//
    private float defaultSpeed = screenHeight/150;
    private float speed;
    private float timeOfSpeedIncrease;

    //==============CONSTRUCTOR==============//
    public Enemy(Bitmap bitmap)
    {
        init();
        this.bitmap = bitmap;
        active = true;
    }

    public Enemy()
    {
        init();
        active = true;
    }


    //==============Init==============//
    public void init()
    {
        pos.x = 0;
        pos.y = screenHeight;
        speed = defaultSpeed;
        timeOfSpeedIncrease = -1;
    }

    public void update(double dt)
    {
        if (dt < 1)
        {
            pos.y -= speed * dt;
            if(pos.y > screenHeight)
            {
                pos.y = screenHeight;
            }
            if(timeOfSpeedIncrease >= 0)
            {
                timeOfSpeedIncrease -= dt;
                if(timeOfSpeedIncrease < 0)
                {
                    speed = defaultSpeed;
                }
            }
        }
    }

    public void setSpeedIncreaseForSomeTime(float speedMultiplyer, float time)
    {
        this.speed = speedMultiplyer * defaultSpeed;
        timeOfSpeedIncrease = time;
    }
}
