package com.ss.mobileframework.GameAsset;

import android.graphics.Paint;

/**
 * Created by sweeseng789 on 6/12/2015.
 */
public class Item extends GameObject
{
    enum TYPE
    {
        s_DRUGS,
        s_APPLE,
        s_TOTAL
    };

    //==============VARIABLES==============//
    private TYPE type;
    private Paint paint;

    //==============CONSTRUCTOR==============//
    public Item()
    {
        active = true;
        type = TYPE.s_APPLE;
        paint = new Paint();
    }

    //==============GETTER==============//
    public boolean isApple()
    {
        if(type == TYPE.s_APPLE)
            return  true;
        return false;
    }

    public Paint getPaint()
    {
        return paint;
    }

    //==============SETTER==============//
    public void update(double dt)
    {
       // bounds.contains((int)pos.x, (int)pos.y, bitmap.getWidth(), bitmap.getHeight());
    }
}
