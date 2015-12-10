package com.ss.mobileframework.Highscore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ss.mobileframework.GameThread;
import com.ss.mobileframework.R;
import com.ss.mobileframework.Text.CText;
import com.ss.mobileframework.Utility.Vector3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by sweeseng789 on 10/12/2015.
 */

public class View_Highscore extends SurfaceView implements SurfaceHolder.Callback
{
    private Highscore highscore;                        // Implement this interface to receive information about changes to the surface.
    private GameThread myThread = null;                 // Thread to control the rendering
    private Bitmap m_Background, m_BackgroundScale;     //Used for rendering background
    private short m_Background_x = 0, m_Background_y = 0;
    int m_screenWidth, m_screenHeight;                  //Define Screen width and Screen height as integer
    public float FPS;                                   // Variables for FPS
    float deltaTime;                                    // Variables for FPS
    long dt;                                            // Variables for FPS
    Vector3 buttonPos;
    Bitmap button, buttonScale;


    CText highScore = new CText();
    CText back = new CText();
    Vector<CText> textList = new Vector<>();

    //==============CONSTRUCTOR==============//
    public View_Highscore(Context context)
    {
        // Context is the current state of the application/object
        super(context);

        highscore = (Highscore) context;

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        init(context);
    }

    //==============SETTER==============//
    public void init(Context context) {
        // 1d) Set information to get screen size
        DisplayMetrics m_metrics = context.getResources().getDisplayMetrics();
        m_screenWidth = m_metrics.widthPixels;
        m_screenHeight = m_metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
        m_BackgroundScale = Bitmap.createScaledBitmap(m_Background, m_screenWidth, m_screenHeight, true); // Scaling of background

        button = BitmapFactory.decodeResource(getResources(), R.drawable.back_button);
        buttonScale = Bitmap.createScaledBitmap(button, m_screenWidth / 3, m_screenHeight / 10, true);
        buttonPos = new Vector3(m_screenWidth - buttonScale.getWidth(), m_screenHeight - buttonScale.getHeight(), 0);

        highScore.setText("High Score");
        highScore.setScale(120);
        highScore.getPos().set(m_screenWidth * 0.5f, m_screenHeight * 0.1f, 0);
        highScore.setColor(255, 255, 0, 0);
        highScore.setFont(context);
        highScore.getPaint().setTextAlign(Paint.Align.CENTER);

        int diff = 80;
        int a = 0;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("highscore.txt")));
            String mline;
            while ((mline = reader.readLine()) != null)
            {
                a++;
                CText text = new CText();
                text.setScale(diff);
                text.setFont(context);
                text.getPos().set(m_screenWidth * 0.5f, m_screenHeight * 0.2f + (a * diff) * 2, 0);
                text.setText(mline);
                text.setColor(255, 253, 172, 92);
                text.getPaint().setTextAlign(Paint.Align.CENTER);
                textList.add(text);
            }
        }
        catch (IOException e)
        {
        }
    }


    public void surfaceCreated(SurfaceHolder holder) //must implement inherited abstract methods
    {
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // Destroy the thread
        if (myThread.isAlive())
        {
            myThread.startRun(false);
        }
        boolean retry = true;
        while (retry)
        {
            try
            {
                myThread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    //============ UPDATE ============//
    public void update(float dt, float fps)
    {
        FPS = fps;
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        RenderGameplay(canvas);
    }

    //============ RENDER ============//
    public void RenderGameplay(Canvas canvas)
    {
        // 2) Re-draw 2nd image after the 1st image ends
        if(canvas == null) //New Canvas
        {
            return;
        }

        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y, null);
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y - m_screenHeight, null);

        canvas.drawBitmap(buttonScale, buttonPos.x, buttonPos.y, null);

        highScore.renderText(canvas);

        for(CText text : textList)
        {
            text.renderText(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        short X = (short)event.getX();
        short Y = (short) event.getY();

        Vector3 diff = new Vector3(X - buttonPos.x - (buttonScale.getWidth() * 0.5f), Y - buttonPos.y - (buttonScale.getHeight() * 0.5f), 0);

        if(diff.length() <= 150)
        {
            highscore.returnToMenu();
        }

        return super.onTouchEvent(event);
    }
}

