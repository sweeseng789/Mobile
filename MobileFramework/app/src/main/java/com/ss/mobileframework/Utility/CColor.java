package com.ss.mobileframework.Utility;

/**
 * Created by sweeseng789 on 26/11/2015.
 */
public class CColor
{
    public int Alpha;
    public int Red;
    public int Green;
    public int Blue;

    public CColor()
    {
        Alpha = Red = Green = Blue = 0;
    }

    public CColor(int Alpha, int Red, int Green, int Blue)
    {
        this.Alpha = Alpha;
        this.Red = Red;
        this.Green = Green;
        this.Blue = Blue;
    }

    public CColor(CColor color)
    {
        this.Alpha = color.Alpha;
        this.Red = color.Red;
        this.Green = color.Green;
        this.Blue = color.Blue;
    }
}
