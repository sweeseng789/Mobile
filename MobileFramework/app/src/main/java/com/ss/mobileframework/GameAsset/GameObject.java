package com.ss.mobileframework.GameAsset;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.ss.mobileframework.Utility.Vector3;

/**
 * Created by ShingLiya on 6/12/2015.
 */
public class GameObject
{
    //==============VARIABLES==============//
    protected Vector3 pos;
    protected boolean active;
    protected Bitmap bitmap; //For displaying image only
    protected SpriteAnimation sprite; //For displaying Sprite Animation
    public static int screenWidth, screenHeight;

    //==============CONSTRUCTOR==============//
    public GameObject()
    {
        pos = new Vector3();
        sprite = new SpriteAnimation();
        active = false;
    }

    //==============SETTER==============//
    public void setSpriteAnimation(Bitmap bitmap, int x, int y, int fps, int frameCount)
    {
        sprite.setSpriteAnimation(bitmap, fps, frameCount);
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    //==============GETTER==============//
    public Vector3 getPos()
    {
        return pos;
    }

    public SpriteAnimation getSprite()
    {
        return sprite;
    }

    public boolean getActive()
    {
        return active;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }
}