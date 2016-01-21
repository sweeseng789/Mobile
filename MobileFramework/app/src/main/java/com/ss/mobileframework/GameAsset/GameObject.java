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
    protected Bitmap bitmap = null; //For displaying image only
    protected SpriteAnimation sprite = null; //For displaying Sprite Animation
    protected int gameID;
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

    public void setGameID(int gameID)
    {
        this.gameID = gameID;
    }

    public int getWidth()
    {
        if(bitmap != null) //User is using Bitmap
        {
            return bitmap.getWidth();
        }
        else //User is using Sprite Animation
        {
            return getSprite().getSpriteWidth();
        }
    }

    public int getHeight()
    {
        if(bitmap != null) //User is using Bitmap
        {
            return bitmap.getHeight();
        }
        else //User is using Sprite Animation
        {
            return getSprite().getSpriteHeight();
        }

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

    public int getGameID()
    {
        return gameID;
    }
}