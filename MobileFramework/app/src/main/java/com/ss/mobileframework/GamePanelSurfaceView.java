package com.ss.mobileframework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ss.mobileframework.GameAsset.Enemy;
import com.ss.mobileframework.GameAsset.GameObject;
import com.ss.mobileframework.GameAsset.Item;
import com.ss.mobileframework.Text.CText;
import com.ss.mobileframework.GameAsset.Player;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    // Variable for Game State check
    enum States
    {
        s_play,
        s_lose
    };

    //==============VARIABLES==============//
    private Gamepage game;    // Implement this interface to receive information about changes to the surface.
    private GameThread myThread = null; // Thread to control the rendering
    private Bitmap m_Background, m_BackgroundScale; //Used for rendering background
    int m_screenWidth, m_screenHeight;     // 1b) Define Screen width and Screen height as integer
    private short m_Background_x = 0, m_Background_y = 0;     // 1c) Variables for defining background start and end point

    //Text
    CText text = new CText();
    CText debug = new CText();
    CText pickUpText = new CText();
    float pickUpTextDuration = -1;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    //Variable for location to move to
    private short mx = 0, my = 0;

    private States GameState; //Game State

    Player player; //Player

    //Items
    Vector<Item> m_cItemList;

    //Enemy
    Enemy enemy;

    //Game over vars
    private Bitmap loseScreen;
    CText retryText = new CText();
    CText exitText = new CText();

    Vector<Integer> highscoreList;
    boolean checkAgainstPreviousScore;
    File f;
    InputStream inputStream;
    OutputStream outputStream;

    MediaPlayer bgm;

    //Sound
    private SoundPool sounds;
    private int soundcorrect, soundwrong, soundbonus;

    //Load Shared
    int highscore;
    SharedPreferences sharePrefscore;
    Editor editor;

    //Method 2
    /*
    intent.putExtre("highscore", highscore)
    intent.setclass(getcontext(), scorepage.class);
    activityTracker.startActivity(intent):
     */

    /*
    if(score > highscore)
    {
        editor.putInt("keyhighscore", highscore);
        editor.commiit();
    }
    */



    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context)
    {
        // Context is the current state of the application/object
        super(context);

        game = (Gamepage)context;

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // 1d) Set information to get screen size
        DisplayMetrics m_metrics = context.getResources().getDisplayMetrics();
        m_screenWidth = m_metrics.widthPixels;
        m_screenHeight = m_metrics.heightPixels;

        // 1e)load the image when this class is being instantiated
        m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene);
        m_BackgroundScale = Bitmap.createScaledBitmap(m_Background, m_screenWidth, m_screenHeight, true); // Scaling of background

        //Load Shared Preferences
        sharePrefscore = getContext().getSharedPreferences("Scoredata", Context.MODE_PRIVATE);
        editor = sharePrefscore.edit();
        highscore = sharePrefscore.getInt("Keyhighscore", 0);

        //Load Text Data
        text.setScale(30.f);
        text.setColor(255, 255, 128, 0);
        text.getPos().set(0, 50, 0);

        debug.setScale(30.f);
        debug.setColor(255, 255, 128, 0);
        debug.getPos().set(0, 100, 0);

        pickUpText.setScale(30.f);
        pickUpText.setColor(255, 0, 100, 255);
        pickUpText.setText("Good");

        //set game state
        GameState = States.s_play;

        //let GameObjects know about screen height and width
        GameObject.screenWidth = m_screenWidth;
        GameObject.screenHeight = m_screenHeight;

        //Initialize Player
        player = new Player();
        player.setSpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.player), 320, 64, 6, 6);
        player.getPos().set(m_screenWidth / 2 - player.getSprite().getSpriteWidth() / 2, m_screenHeight / 2, 0);
        player.getNewPos().set(player.getPos().x, player.getPos().y, player.getPos().z);

        //Initialize item list
        m_cItemList = new Vector<>();

        Item item = new Item(Item.TYPE.s_CABBAGE, BitmapFactory.decodeResource(getResources(),R.drawable.cabage));
        item.getPaint().setTextSize(10);
        m_cItemList.add(item);

        for(int i = 0; i < 3; ++i) {
            item = new Item(Item.TYPE.s_DRUGS, BitmapFactory.decodeResource(getResources(), R.drawable.weed));
            item.getPaint().setTextSize(10);
            m_cItemList.add(item);
        }

        //init enemy
        enemy = new Enemy(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow), m_screenWidth, m_screenHeight, true));

        //game over stuff
        loseScreen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lose), m_screenWidth, m_screenHeight, true);

        retryText.setText("Retry");
        retryText.setScale(70.f);
        retryText.setColor(255, 255, 255, 0);
        retryText.getPos().set(m_screenWidth / 4 - (float) (35 * retryText.getText().length() / 2), m_screenHeight / 2, 0);

        exitText.setText("Exit");
        exitText.setScale(70.f);
        exitText.setColor(255, 255, 255, 0);
        exitText.getPos().set(m_screenWidth / 4 * 3 - (float) (35 * exitText.getText().length() / 2), m_screenHeight / 2, 0);

        checkAgainstPreviousScore = false;
        highscoreList = new Vector<>();

        try {
            inputStream = context.getAssets().open("highscore.txt");
            f = context.getFileStreamPath("highscore.txt");
            outputStream = new FileOutputStream(f);
        }catch (IOException e){

        }
        try
        {
            BufferedReader reader = new BufferedReader( new InputStreamReader(context.getAssets().open("highscore.txt")));
            String mline;
            while ((mline = reader.readLine()) != null)
            {
                highscoreList.add(Integer.parseInt(mline));
            }
        }
        catch (IOException e)
        {
        }

        bgm = MediaPlayer.create(context, R.raw.background_music);
        bgm.setVolume(0.8f, 0.8f);
        bgm.start();

        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundcorrect = sounds.load(context, R.raw.correct, 1);
        soundwrong = sounds.load(context, R.raw.incorrect, 1);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    public void init()
    {
        //set game state
        GameState = States.s_play;

        //Initialize Player
        player.getPos().set(m_screenWidth / 2 - player.getSprite().getSpriteWidth() / 2, m_screenHeight / 2, 0);
        player.getNewPos().set(player.getPos().x, player.getPos().y, player.getPos().z);
        player.setScore(0);

        //Initialize item list

        for(Item item : m_cItemList)
        {
            item.init();
        }

        //init enemy
        enemy.init();

        pickUpTextDuration = -1;
        checkAgainstPreviousScore = false;
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

        bgm.stop();;
        bgm.release();
        sounds.unload(soundcorrect);
        sounds.unload(soundwrong);
        sounds.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    //============ UPDATE ============//
    public void update(float dt, float fps)
    {
        FPS = fps;

        switch (GameState)
        {
            case s_play:
            {
                System.out.print(highscore);
                m_Background_y += 500 * dt;
                if(m_Background_y > m_screenHeight)
                {
                    m_Background_y = 0;
                }


                //Text Update
                text.setText(Float.toString(FPS));

                //Player Update
                player.update(dt, System.currentTimeMillis());
                float x1 = player.getPos().x;
                float y1 = player.getPos().y;
                float w1 = player.getSprite().getSpriteWidth();
                float h1 = player.getSprite().getSpriteHeight();

                //Text Update
                float text_x = player.getPos().x + player.getSprite().getSpriteWidth()/(pickUpText.getText().length());
                float text_y = player.getPos().y - player.getSprite().getSpriteHeight()/8;
                pickUpText.getPos().set(text_x, text_y, 0);
                if(pickUpTextDuration > 0)
                    pickUpTextDuration -= dt;

                //Item Update
                for(Item item : m_cItemList)
                {
                    item.update(dt);

                    float x2 = item.getPos().x;
                    float y2 = item.getPos().y;
                    float w2 = item.getBitmap().getWidth();
                    float h2 = item.getBitmap().getHeight();

                    //Collision Check with player
                    if (CheckCollision((int) x1, (int) y1, (int) w1, (int) h1, (int) x2, (int) y2, (int) w2, (int) h2) && item.getActive())
                    {
                        if(item.isCabbage())
                        {
                            player.addScore(10);
                            item.randVars();
                            item.setActive(false);
                            enemy.setSpeedIncreaseForSomeTime(-80, 2);

                            pickUpText.setColor(255, 0, 100, 255);
                            pickUpText.setText("Good");
                            sounds.play(soundcorrect, 1.f, 1.f, 0, 0, 1.5f);
                        }
                        else if(item.isDrug())
                        {
                            item.randVars();
                            item.setActive(false);
                            enemy.setSpeedIncreaseForSomeTime(50, 1);

                            pickUpText.setColor(255, 255, 0, 0);
                            pickUpText.setText("Bad");
                            sounds.play(soundwrong, 1.f, 1.f, 0, 0, 1.5f);
                        }

                        pickUpTextDuration = 1;
                    }
                }

                //update enemy
                enemy.update(dt);
                float x2 = enemy.getPos().x;
                float y2 = enemy.getPos().y;
                float w2 = enemy.getBitmap().getWidth();
                float h2 = enemy.getBitmap().getHeight();
                if (CheckCollision((int) x2, (int) y2, (int) w2, (int) h2, (int) x1, (int) y1, (int) w1, (int) h1) && enemy.getActive())
                {
                    GameState = States.s_lose;
                }
            }
            break;

            case s_lose:
                break;
        }
    }

    // Rendering is done on Canvas
    public void doDraw(Canvas canvas)
    {
        switch (GameState)
        {
            case s_play:
                RenderGameplay(canvas);
                break;
            case s_lose:
                RenderLose(canvas);
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
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y - m_screenHeight, null);

        //Render player
        player.getSprite().draw(canvas, player.getPos());

        //Render Items
        for(Item item: m_cItemList)
        {
            if(item.getActive())
                canvas.drawBitmap(item.getBitmap(), item.getPos().x, item.getPos().y, item.getPaint());
        }

        //Render Enemy
        canvas.drawBitmap(enemy.getBitmap(), enemy.getPos().x, enemy.getPos().y, null);

        //Debug text
        canvas.drawText(text.getText(), text.getPos().x, text.getPos().y, text.getPaint()); // Align text to top left
        canvas.drawText(player.getText().getText(), player.getText().getPos().x, player.getText().getPos().y, player.getText().getPaint());
        if(pickUpTextDuration > 0)
        canvas.drawText(pickUpText.getText(), pickUpText. getPos().x, pickUpText.getPos().y, pickUpText.getPaint());
    }

    //render lose screen
    public void RenderLose(Canvas canvas)
    {
        // 2) Re-draw 2nd image after the 1st image ends
        if(canvas == null) //New Canvas
        {
            return;
        }

        canvas.drawBitmap(loseScreen, 0, 0, null);

        canvas.drawText(retryText.getText(), retryText.getPos().x, retryText.getPos().y, retryText.getPaint());
        canvas.drawText(exitText.getText(), exitText.getPos().x, exitText.getPos().y, exitText.getPaint());
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short)event.getX();
        short Y = (short) event.getY();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                switch (GameState)
                {
                    case s_play:
                    {
                        //limit player on the road
                        if (X < m_screenWidth / 7.2)
                            X = (short) (m_screenWidth / 7.2);
                        if (X > (m_screenWidth - m_screenWidth / 7.2))
                            X = (short) (m_screenWidth - m_screenWidth / 7.2);

                        player.getNewPos().x = (short) (X - player.getSprite().getBitmap().getWidth() / 13);
                        player.getNewPos().y = (short) (Y - player.getSprite().getBitmap().getHeight() / 2);
                    }
                    break;
                    case s_lose:
                    {
                        if (X < m_screenWidth / 2)
                        {
                            init();
                        }
                        else
                        {
                            game.finish();
                        }
                    }
                    break;
                }
            }
            break;
            case MotionEvent.ACTION_MOVE:
            {
                //limit player on the road
                if (X < m_screenWidth / 7.2)
                    X = (short) (m_screenWidth / 7.2);
                if (X > (m_screenWidth - m_screenWidth / 7.2))
                    X = (short) (m_screenWidth - m_screenWidth / 7.2);

                player.getNewPos().x = (short) (X - player.getSprite().getBitmap().getWidth() / 13);
                player.getNewPos().y = (short) (Y - player.getSprite().getBitmap().getHeight() / 2);
            }
            break;
        }
        return true;
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2)
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
