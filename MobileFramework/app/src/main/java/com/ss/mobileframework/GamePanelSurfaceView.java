package com.ss.mobileframework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.PaintDrawable;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ss.mobileframework.Text.CText;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{

    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap m_Background, m_BackgroundScale; //Used for rendering background

    // 1b) Define Screen width and Screen height as integer
    int m_screenWidth, m_screenHeight;

    // 1c) Variables for defining background start and end point
    private short m_Background_x = 0, m_Background_y = 0;

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] m_Spaceship = new Bitmap[4];

    // 4b) Variable as an index to keep track of the spaceship images
    private short m_SpaceshipIndex = 0;

    //Text
    CText text = new CText();

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    //Variable for location to move to
    private short mX = 0, mY = 0;
    private short mx = 0, my = 0;

    // Variable for Game State check
    private short GameState;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context)
    {
        // Context is the current state of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics m_metrics = context.getResources().getDisplayMetrics();
        m_screenWidth = m_metrics.widthPixels;
        m_screenHeight = m_metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
        m_BackgroundScale = Bitmap.createScaledBitmap(m_Background, m_screenWidth, m_screenHeight, true); // Scaling of background

        // 4c) Load the images of the spaceships
        m_Spaceship[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_1);
        m_Spaceship[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_2);
        m_Spaceship[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_3);
        m_Spaceship[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ship2_4);

        //Load Text Data
        text.setScale(30.f);
        text.setColor(255, 255, 128, 0);
        text.getPos().setPos(0, 50);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
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
        if (myThread.isAlive()){
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

        switch (GameState) {
            case 0:
            {
                // 3) Update the background to allow panning effect
                //m_Background_x -= 500 * dt; // Allow panning speed
                //if(m_Background_x < -m_screenWidth)
                {
                    // m_Background_x = 0;
                }
                m_Background_y += 500 * dt;
                if(m_Background_y > m_screenHeight)
                {
                    m_Background_y = 0;
                }

                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                m_SpaceshipIndex++;
                m_SpaceshipIndex %= 4;

                //Text Update
                text.setText(Float.toString(FPS));

                if(mx != mX)
                {
                    if(mx < mX)
                        mx += (mX-mx)/10;

                    if(mx > mX)
                        mx -= (mx-mX)/10;
                }
                if(my != mY)
                {
                    if(my < mY)
                        my += (mY-my)/10;

                    if(my > mY)
                        my -= (my-mY)/10;
                }
            }
            break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        switch (GameState)
        {
            case 0:
                RenderGameplay(canvas);
                break;
        }
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
        //canvas.drawBitmap(m_BackgroundScale, m_Background_x + m_screenWidth, m_Background_y, null);
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y - m_screenHeight, null);

        // 4d) Draw the spaceships
        canvas.drawBitmap(m_Spaceship[m_SpaceshipIndex], mx, my, null);


        // Bonus) To print FPS on the screen
        canvas.drawText(text.getText(), text.getPos().x, text.getPos().y, text.getPaint()); // Align text to top left
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short)event.getX();
        short Y = (short) event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            // New location where the image to land on
            mX= (short)(X -m_Spaceship[m_SpaceshipIndex].getWidth()/2);
            mY= (short)(Y -m_Spaceship[m_SpaceshipIndex].getHeight()/2);
        }
        return super.onTouchEvent(event);
    }
}
