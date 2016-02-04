package com.ss.mobileframework;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.view.View;

import com.ss.mobileframework.Highscore.View_Highscore;
import com.ss.mobileframework.ShopPage.View_Shop;

public class GameThread extends Thread
{
    enum UPDATESTATE
    {
        e_GAME,
        e_HIGHSCORE,
        e_SHOPPAGE
    };

    //==============VARIABLES==============//
    UPDATESTATE state;
    private GamePanelSurfaceView myView;    // The actual view that handles inputs and draws to the surface
    private View_Highscore highscoreView;   // The actual view that handles inputs and draws to the surface
    private View_Shop shopView;   // The actual view that handles inputs and draws to the surface
    private SurfaceHolder holder;           // Surface holder that can access the physical surface
    private boolean isRun;                  // Flag to hold game state
    private boolean isPause;
    int frameCount = 0;                     // get actual fps
    long lastTime = 0;                      // get actual fps
    long lastFPSTime = 0;                   // get actual fps
    float fps = 0;                          // get actual fps
    float dt = 0;                           // get actual fps

    //==============CONSTRUCTOR==============//
    public GameThread(SurfaceHolder holder, GamePanelSurfaceView myView)
    {
        super();
        isRun = true; // for running
        isPause = false; // for pause
        this.myView = myView;
        this.holder = holder;
        state = UPDATESTATE.e_GAME;
    }

    public GameThread(SurfaceHolder holder, View_Highscore myView)
    {
        super();
        isRun = true;
        isPause = false;
        highscoreView = myView;
        this.holder = holder;
        state = UPDATESTATE.e_HIGHSCORE;
    }

    public GameThread(SurfaceHolder holder, View_Shop myView)
    {
        super();
        isRun = true;
        isPause = false;
        shopView = myView;
        this.holder = holder;
        state = UPDATESTATE.e_SHOPPAGE;
    }

    //==============SETTER==============//
    public void startRun(boolean r)
    {
        isRun = r;
    }

    public void pause()
    {
        synchronized (holder)
        {
            isPause = true;
        }
    }

    public void unPause()
    {
        synchronized (holder)
        {
            isPause = false;
            holder.notifyAll();
        }
    }

    public void calculateFPS()
    {
        frameCount++;

        long currentTime = System.currentTimeMillis();
        dt = (currentTime - lastTime) / 1000.f;
        lastTime = currentTime;

        if(currentTime - lastFPSTime > 1000)
        {
            fps = (frameCount * 1000.f) / (currentTime - lastFPSTime);
            lastFPSTime = currentTime;
            frameCount = 0;
        }
    }

    public void update_Game()
    {
        //Update game state and render state to the screen
        Canvas c = null;
        try
        {
            c = this.holder.lockCanvas();
            synchronized(holder)
            {
                if (myView != null)
                {
                    if (getPause() == false)
                    {
                        myView.update(dt, fps);
                        myView.doDraw(c);
                    }
                }
            }

            synchronized(holder)
            {
                while (getPause()==true)
                {
                    try
                    {
                        holder.wait();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }

        finally
        {
            if (c!=null)
            {
                holder.unlockCanvasAndPost(c);
            }
        }
        calculateFPS();
    }

    public void update_Highscore()
    {
        //Update game state and render state to the screen
        Canvas c = null;
        try
        {
            c = this.holder.lockCanvas();
            synchronized(holder)
            {
                if (highscoreView != null)
                {
                    if (getPause() == false)
                    {
                        highscoreView.update(dt, fps);
                        highscoreView.doDraw(c);
                    }
                }
            }

            synchronized(holder)
            {
                while (getPause()==true)
                {
                    try
                    {
                        holder.wait();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }

        finally
        {
            if (c!=null)
            {
                holder.unlockCanvasAndPost(c);
            }
        }
        calculateFPS();
    }

    public void update_ShopPage()
    {
        //Update game state and render state to the screen
        Canvas c = null;
        try
        {
            c = this.holder.lockCanvas();
            synchronized(holder)
            {
                if (shopView != null)
                {
                    if (getPause() == false)
                    {
                        shopView.update(dt, fps);
                        shopView.doDraw(c);
                    }
                }
            }

            synchronized(holder)
            {
                while (getPause()==true)
                {
                    try
                    {
                        holder.wait();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }

        finally
        {
            if (c!=null)
            {
                holder.unlockCanvasAndPost(c);
            }
        }
        calculateFPS();
    }



    @Override
    public void run()
    {
        while (isRun)
        {
            switch(state)
            {
                case e_GAME:
                    update_Game();
                    break;

                case e_HIGHSCORE:
                    update_Highscore();
                    break;

                case e_SHOPPAGE:
                    update_ShopPage();
                    break;

                default:
                    break;
            }
        }

    }

    //==============GETTER==============//
    public boolean getPause() //Return Pause
    {
        return isPause;
    }
}