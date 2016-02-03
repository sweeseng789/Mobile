package com.ss.mobileframework.ShopPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

import com.ss.mobileframework.GameThread;
import com.ss.mobileframework.R;
import com.ss.mobileframework.Text.CText;
import com.ss.mobileframework.Utility.Alert;
import com.ss.mobileframework.Utility.Data;
import com.ss.mobileframework.Utility.SSDLC;
import com.ss.mobileframework.Utility.Vector3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by sweeseng789 on 10/12/2015.
 */

public class View_Shop extends SurfaceView implements SurfaceHolder.Callback
{
    private Shop shop;                                  // Implement this interface to receive information about changes to the surface.
    private GameThread myThread = null;                 // Thread to control the rendering
    private Bitmap m_Background, m_BackgroundScale;     //Used for rendering background
    private short m_Background_x = 0, m_Background_y = 0;
    int m_screenWidth, m_screenHeight;                  //Define Screen width and Screen height as integer
    public float FPS;                                   // Variables for FPS
    float deltaTime;                                    // Variables for FPS
    long dt;                                            // Variables for FPS
    Vector3 buttonPos;
    Bitmap button, buttonScale;
    Bitmap CoinLogo;
    Bitmap ShopIcon;
    Bitmap flash;
    Bitmap notAvailable;
    Bitmap icon_Buy;
    Bitmap icon_notAvailable;
    Bitmap icon_back;

    SSDLC DLC = new SSDLC();
    Data database;

    boolean firstTouch = false;
    boolean show = false;

    //Alert
    public AlertDialog.Builder alertDialog = null;
    public Activity activityTracker;
    private Alert alert;
    boolean purchase;


    CText highScore = new CText();
    CText back = new CText();
    Vector<CText> textList = new Vector<>();

    //==============CONSTRUCTOR==============//
    public View_Shop(Context context, Activity activity)
    {
        // Context is the current state of the application/object
        super(context);

        //highscore = (Highscore) context;
        shop = (Shop)context;

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);

        init(context, activity);
    }

    void setImage()
    {
        // 1e)load the image when this class is being instantiated
        m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene3);
        m_BackgroundScale = Bitmap.createScaledBitmap(m_Background, m_screenWidth, m_screenHeight, true); // Scaling of background

        //Set Coin Image
        CoinLogo = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coin), m_screenWidth / 10, m_screenHeight / 15, true);
        ShopIcon = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shop), m_screenWidth / 2, m_screenHeight / 4, true);
        flash = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.speed_up), m_screenWidth / 3, m_screenHeight / 5, false);
        notAvailable = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.coming_soon), m_screenWidth / 3, m_screenHeight / 5, false);
        icon_Buy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_buy), m_screenWidth / 2, m_screenHeight / 5, false);
        icon_notAvailable = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_not_available), m_screenWidth / 2, m_screenHeight / 5, false);
        icon_back = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_back), m_screenWidth / 10, m_screenHeight / 15, false);
    }


    //==============SETTER==============//
    public void init(final Context context, Activity activity)
    {
        // 1d) Set information to get screen size
        DisplayMetrics m_metrics = context.getResources().getDisplayMetrics();
        m_screenWidth = m_metrics.widthPixels;
        m_screenHeight = m_metrics.heightPixels;

        setImage();

        database = new Data(context, "Settings");
        database.getDatabaseNaming()[Data.DATANAME.s_COINCOUNT.ordinal()] = "CoinCount";
        database.getDatabaseNaming()[Data.DATANAME.s_POWERUP.ordinal()] = "PowerUp";


        if(database.getSharedDatabase().getInt(database.getDatabaseNaming()[Data.DATANAME.s_COINCOUNT.ordinal()], 500) < 0)
        {
            database.getEditor().putInt(database.getDatabaseNaming(Data.DATANAME.s_COINCOUNT.ordinal()), 500);
            database.getEditor().commit();
        }

//        if(havePowerUP == false)
//        {
//            if (coinCount > 0)
//            {
//                int newCoinCount = coinCount - 200;
//                database.getEditor().putInt(database.getDatabaseNaming(Data.DATANAME.s_COINCOUNT.ordinal()), newCoinCount);
//                database.getEditor().commit();
//
        activityTracker = activity;

        alert = new Alert(this);
        alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }


    public void surfaceCreated(SurfaceHolder holder) //must implement inherited abstract methods
    {
        // Create the thread
        if (!myThread.isAlive())
        {
            //myThread = new GameThread(getHolder(), this);
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
        if(purchase)
        {
            boolean havePowerUP = database.getSharedDatabase().getBoolean(database.getDatabaseNaming()[Data.DATANAME.s_POWERUP.ordinal()], false);
            int coinCount = database.getSharedDatabase().getInt(database.getDatabaseNaming()[Data.DATANAME.s_COINCOUNT.ordinal()], 500);

            if(havePowerUP == false)
            {
                if (coinCount > 0)
                {
                    int newCoinCount = coinCount - 200;
                    database.getEditor().putInt(database.getDatabaseNaming(Data.DATANAME.s_COINCOUNT.ordinal()), newCoinCount);
                    database.getEditor().commit();

                    database.getEditor().putBoolean(database.getDatabaseNaming(Data.DATANAME.s_POWERUP.ordinal()), true);
                    database.getEditor().commit();
                }
            }
            purchase = false;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        if(canvas == null) //New Canvas
            return;

        RenderGameplay(canvas);
    }

    //============ RENDER ============//
    public void RenderGameplay(Canvas canvas)
    {
        // 2) Re-draw 2nd image after the 1st image ends

        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y, null);
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y - m_screenHeight, null);

//        canvas.drawBitmap(buttonScale, buttonPos.x, buttonPos.y, null);
//
//        highScore.renderText(canvas);

//        for(CText text : textList)
//        {
//            text.renderText(canvas);
//        }

        canvas.drawBitmap(flash, 150, m_screenHeight / 2 - 600, null);

        canvas.drawBitmap(notAvailable, m_screenWidth - 650, m_screenHeight / 2 - 600, null);
        canvas.drawBitmap(notAvailable, m_screenWidth - 650, m_screenHeight / 2, null);
        canvas.drawBitmap(notAvailable, 200, m_screenHeight / 2, null);

        canvas.drawBitmap(icon_back, m_screenWidth - icon_back.getWidth(), 0, null);

        canvas.drawBitmap(CoinLogo, 0, 0, null);

        int coinCount = database.getSharedDatabase().getInt(database.getDatabaseNaming()[Data.DATANAME.s_COINCOUNT.ordinal()], 500);
        CText text = new CText();
        text.setText(Integer.toString(coinCount));
        text.setScale(CoinLogo.getScaledWidth(canvas));
        text.setColor(255, 255, 255, 255);
        text.getPos().set(CoinLogo.getScaledHeight(canvas), CoinLogo.getScaledHeight(canvas) * 0.8f, 0);
        text.renderText(canvas);

        CText text2 = new CText();
        text2.setText("Shop");
        text2.setScale(m_screenWidth / 4);
        text2.setColor(255, 255, 255, 255);
        text2.getPos().set(m_screenWidth / 4, m_screenHeight * 0.2f, 0);
        text2.renderText(canvas);

        if(firstTouch)
        {
            Bitmap toShow;
            if(show)
                toShow = icon_Buy;
            else
                toShow = icon_notAvailable;
            canvas.drawBitmap(toShow, m_screenWidth / 2 - toShow.getWidth() / 2, m_screenHeight / 2 + 600, null);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        short X = (short)event.getX();
        short Y = (short) event.getY();


        firstTouch = true;
        Rect backRect = DLC.getBoundingBox(new Vector3(m_screenWidth - icon_back.getWidth(), 0, 0), icon_back.getWidth(), icon_back.getHeight());
        Rect flashRect = DLC.getBoundingBox(new Vector3(150, m_screenHeight / 2 - 600, 0), flash.getWidth(), flash.getHeight());
        Rect NA1 = DLC.getBoundingBox(new Vector3(m_screenWidth - 650, m_screenHeight / 2 - 600, 0), notAvailable.getWidth(), notAvailable.getHeight());
        Rect NA2 = DLC.getBoundingBox(new Vector3(m_screenWidth - 650, m_screenHeight / 2, 0), notAvailable.getWidth(), notAvailable.getHeight());
        Rect NA3 = DLC.getBoundingBox(new Vector3(200, m_screenHeight / 2, 0), notAvailable.getWidth(), notAvailable.getHeight());
        Rect purchaseRect = DLC.getBoundingBox(new Vector3(m_screenWidth / 2 - icon_Buy.getWidth() / 2, m_screenHeight / 2 + 600, 0),icon_Buy.getWidth(), icon_Buy.getHeight());

        if(!backRect.contains(X, Y))
        {
            if (flashRect.contains(X, Y))
                show = true;
            else if (NA1.contains(X, Y) || NA2.contains(X, Y) || NA3.contains(X, Y))
                show = false;
            else
                firstTouch = false;

        }
        else
        {
            shop.returnToMenu();
        }

        if(show)
        {
            if(purchaseRect.contains(X, Y))
            {
                purchase = true;
                //alert.setShowAlert(true);
            }
        }

        return super.onTouchEvent(event);
    }
}

