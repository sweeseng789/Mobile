package com.ss.mobileframework.Utility;

/**
 * Created by sweeseng789 on 26/11/2015.
 */
public class Vector2 {
    public float x;
    public float y;

    public Vector2()
    {
        x = 0;
        y = 0;
    }

    public Vector2(Vector2 pos)
    {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setPos(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
}
