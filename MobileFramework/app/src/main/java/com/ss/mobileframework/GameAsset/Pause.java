package com.ss.mobileframework.GameAsset;


import com.ss.mobileframework.GameThread;

/**
 * Created by sweeseng789 on 18/1/2016.
 */
public class Pause extends GameObject
{
    //============== VARIABLES ==============//
    boolean isGamePaused;

    //============== CONSTRUCTOR ==============//
    public Pause()
    {
        active = true;
        isGamePaused = false;
    }

    //============== SETTER ==============//
    public void setGamePaused(boolean isGamePaused)
    {
        this.isGamePaused = isGamePaused;
    }

    //============== GETTER ==============//
    public boolean getGamePaused()
    {
        return isGamePaused;
    }
}
