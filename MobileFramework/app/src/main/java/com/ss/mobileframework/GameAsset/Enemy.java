package com.ss.mobileframework.GameAsset;

import android.graphics.Bitmap;

/**
 * Created by ShingLiya on 8/12/2015.
 */
public class Enemy extends GameObject
{
    //==============VARIABLES==============//
    private float defaultSpeed = 10;
    private float speed;
    private float timeOfSpeedIncrease;

    //==============CONSTRUCTOR==============//
    public Enemy(Bitmap bitmap)
    {
        pos.x = 0;
        pos.y = screenHeight;
        speed = defaultSpeed;
        timeOfSpeedIncrease = -1;
        this.bitmap = bitmap;
        active = true;
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

    public void setSpeedIncreaseForSomeTime(float speed, float time)
    {
        this.speed = speed;
        timeOfSpeedIncrease = time;
    }
}
