package com.ss.mobileframework.Utility;

import android.graphics.Rect;

/**
 * Created by 142128G on 1/14/2016.
 */
public class SSDLC
{
    public Rect getBoundingBox(Vector3 pos, int width, int height)
    {
        return new Rect((int)pos.x, (int)pos.y, (int)pos.x + width, (int)pos.y + height);
    }

    public boolean CheckCollision(Rect rect1, Rect rect2)
    {
        if(rect1.intersect(rect2))
            return true;

        return false;
    }
}
