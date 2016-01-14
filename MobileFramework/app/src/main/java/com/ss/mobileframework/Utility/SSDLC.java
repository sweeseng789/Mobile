package com.ss.mobileframework.Utility;

/**
 * Created by 142128G on 1/14/2016.
 */
public class SSDLC
{
    public static boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
    {
        if(x2 >= x1 && x2 <= x1 + w1)           // Checking top Left
        {
            if(y2 >= y1 && y2 <= y1 + h1)
                return true;
            if(y2 + h2 >= y1 && y2 + h2 <= y1 + h1) // Check bottom left
                return true;
        }

        if(x2 + w2 >= x1 && x2 + w2 <= x1 + w1) // Check top Right
        {
            if (y2 >= y1 && y2 <= y1 + h1)
                return true;

            if (y2 + h2 >= y1 && y2 + h2 <= y1 + h1) // Check bottom Right
                return true;
        }
        return false;
    }
}
