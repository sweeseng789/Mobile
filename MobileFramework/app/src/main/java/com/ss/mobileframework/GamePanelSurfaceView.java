package com.ss.mobileframework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Debug;
import android.os.Vibrator;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ss.mobileframework.GameAsset.Enemy;
import com.ss.mobileframework.GameAsset.GameObject;
import com.ss.mobileframework.GameAsset.Item;
import com.ss.mobileframework.GameAsset.Pause;
import com.ss.mobileframework.GameAsset.SpriteAnimation;
import com.ss.mobileframework.Highscore.Highscore;
import com.ss.mobileframework.Text.CText;
import com.ss.mobileframework.GameAsset.Player;
import com.ss.mobileframework.Utility.Alert;
import com.ss.mobileframework.Utility.Data;
import com.ss.mobileframework.Utility.SSDLC;
import com.ss.mobileframework.Utility.Sound;
import com.ss.mobileframework.Utility.Vector3;

import java.util.Objects;
import java.util.Vector;
import java.util.logging.Filter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.EditText;
import android.widget.Toast;


public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    // Variable for Game State check
    enum States
    {
        s_play,
        s_lose
    }

    //Variables for Game ID
    enum GAMEID
    {
        s_PLAYER,
        s_ENEMY,
        s_CABBAGE,
        s_DRUG,
        s_PAUSE,
    }

    //Variable for Sound List
    enum SOUNDLIST
    {
        s_CORRECT,
        s_INCORRECT,
        s_TOTAL
    }

    //==============VARIABLES==============//
    private Gamepage game;    // Implement this interface to receive information about changes to the surface.
    private GameThread myThread = null; // Thread to control the rendering
    private Bitmap m_Background, m_BackgroundScale; //Used for rendering background
    int m_screenWidth, m_screenHeight;     // 1b) Define Screen width and Screen height as integer
    private short m_Background_x = 0, m_Background_y = 0;     // 1c) Variables for defining background start and end point

    //Text
    CText pickUpText = new CText();
    float pickUpTextDuration = -1;

    // Variables for FPS
    float FPS;
    CText fpsText = new CText();

    private States GameState; //Game State

    Player player; //Player
    boolean movingPlayer = false;

    //GameObject List
    Vector<GameObject> m_cGameObjList = new Vector<>();

    //SSDLC
    SSDLC DLC = new SSDLC();

    //Game over vars
    private Bitmap loseScreen;
    CText retryText = new CText();
    CText exitText = new CText();

    //Sound
    Sound sound = new Sound();
    int soundList[];

    //vibration
    Vibrator v;

    //Pause
    Pause pause;

    //Load Shared
    Data database;
    int highscore;

    //Alert
    public AlertDialog.Builder alertDialog = null;
    public Activity activityTracker;
    private Alert alert;

    //facebook button
    private Bitmap facebookButton;
    
    //power-ups
    public boolean speedPowerUpActive = false;
    public float speedPowerUpTimer = 0;


    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context, Activity activity)
    {
        // Context is the current state of the application/object
        super(context);

        game = (Gamepage)context;

        // Adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        database = new Data(context, "Settings");

        //Set naming convention
        database.getDatabaseNaming()[Data.DATANAME.s_HIGHSCORE.ordinal()] = "Highscore";
        database.getDatabaseNaming()[Data.DATANAME.s_LATESTSCORE.ordinal()] = "Latestscore";
        database.getDatabaseNaming()[Data.DATANAME.s_POWERUP.ordinal()] = "PowerUp";
        init(context, activity);

        //Load Shared Preferences
//        sharePrefscore = getContext().getSharedPreferences("Scoredata", Context.MODE_PRIVATE);
//        editor = sharePrefscore.edit();
//        highscore = sharePrefscore.getInt("Keyhighscore", 0);



        //Set Variables
        highscore = database.getSharedDatabase().getInt(database.getDatabaseNaming(Data.DATANAME.s_HIGHSCORE.ordinal()), 0);

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

     void getScreenInfo(Context context)
    {
        DisplayMetrics m_metrics = context.getResources().getDisplayMetrics();
        m_screenWidth = m_metrics.widthPixels;
        m_screenHeight = m_metrics.heightPixels;

        //let GameObjects know about screen height and width
        GameObject.screenWidth = m_screenWidth;
        GameObject.screenHeight = m_screenHeight;
    }

     void setImage()
    {
        //Loading Image
        m_Background = BitmapFactory.decodeResource(getResources(), R.drawable.gamescene3);
        m_BackgroundScale = Bitmap.createScaledBitmap(m_Background, m_screenWidth, m_screenHeight, true); // Scaling of background

        //Game Over Lose Screen
        loseScreen = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lose), m_screenWidth, m_screenHeight, true);
    }

    void setText()
    {
        fpsText.setScale(30.f);
        fpsText.setColor(255, 255, 128, 0);
        fpsText.getPos().set(0, 50, 0);

        pickUpText.setScale(30.f);
        pickUpText.setColor(255, 0, 100, 255);
        pickUpText.setText("Good");

        retryText.setText("Retry");
        retryText.setScale(70.f);
        retryText.setColor(255, 255, 255, 0);
        retryText.getPos().set(m_screenWidth / 4 - (float) (35 * retryText.getText().length() / 2), m_screenHeight / 2, 0);

        exitText.setText("Exit");
        exitText.setScale(70.f);
        exitText.setColor(255, 255, 255, 0);
        exitText.getPos().set(m_screenWidth / 4 * 3 - (float) (35 * exitText.getText().length() / 2), m_screenHeight / 2, 0);
    }

    void setGameObject()
    {
        //Initialize Player
        player = new Player();
        player.setSpriteAnimation(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.player), (int) (m_screenWidth * 1.5f), (int) (m_screenWidth * 1.5f / 6), false), 320, 64, 6, 6);
        player.getPos().set(m_screenWidth / 2 - player.getSprite().getSpriteWidth() / 2, m_screenHeight / 2, 0);
        player.getNewPos().set(player.getPos().x, player.getPos().y, player.getPos().z);
        player.setGameID(GAMEID.s_PLAYER.ordinal());

        Item item = new Item();

        //Initialize Cabbage
        item.setBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cabage), m_screenWidth / 6, m_screenWidth / 6, false));
        item.setGameID(GAMEID.s_CABBAGE.ordinal());
        item.getPaint().setTextSize(10);
        m_cGameObjList.add(item);

        //Initialize Drug
        for(int a = 0; a < 3; ++a)
        {
            item = new Item();
            item.setBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.weed), m_screenWidth / 6, m_screenWidth / 6, false));
            item.setGameID(GAMEID.s_DRUG.ordinal());
            System.out.println(item.getGameID());
            item.getPaint().setTextSize(10);
            m_cGameObjList.add(item);
        }

        //Initialize Enemy
        Enemy enemy = new Enemy();
        enemy.setGameID(GAMEID.s_ENEMY.ordinal());
        enemy.setBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow), m_screenWidth, m_screenHeight, true));
        //enemy.setSpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.policeman), 0, 0, 4, 4);
        m_cGameObjList.add(enemy);

        //Initialize Pause Variables
        pause = new Pause();
        pause.setGameID(GAMEID.s_PAUSE.ordinal());
        pause.getPos().set(72, 72, 0);
        pause.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pause));
        m_cGameObjList.add(pause);

        facebookButton = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_share_button);
    }

    void setSound(Context context)
    {
        sound.setBackgroundMusic(context, R.raw.background_music);
        sound.getBackgroundMusic().setVolume(0.8f, 0.8f);
        sound.getBackgroundMusic().start();

        sound.setUpSoundPool(SOUNDLIST.s_TOTAL.ordinal() - 1);
        soundList = new int[SOUNDLIST.s_TOTAL.ordinal()];

        soundList[SOUNDLIST.s_CORRECT.ordinal()] = sound.getSoundPool().load(context, R.raw.correct, 1);
        soundList[SOUNDLIST.s_INCORRECT.ordinal()] = sound.getSoundPool().load(context, R.raw.incorrect, 1);
    }

    void setAlert(final Activity activity, final Context context)
    {
        //To Track an Activity
        activityTracker = activity;

        //Create Alert Dialog
        alert = new Alert(this);
        alertDialog = new AlertDialog.Builder(getContext());

        //Allow Player to enter their name
        final EditText input = new EditText(getContext());

        //Define the input method
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        //Define max of 20 characters for name field
        int maxLength = 20;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(FilterArray);

        alertDialog.setCancelable(false);
        //alertDialog.setView(input);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
//                Intent intent = new Intent();
//                intent.setClass(getContext(), Mainmenu.class);
//                activityTracker.startActivity(intent);

//                Intent intent = new Intent();
//                //Push highscore to another activity
//                intent.putExtra("highscore", highscore);
//                intent.setClass(getContext(), Highscore.class);
//                activityTracker.startActivity(intent);
            }
        });
    }

    public void init(Context context, Activity activity)
    {
        //Get Info of Screen Size
        getScreenInfo(context);

        //Set Image
        setImage();

        //Set Text
        setText();

        //Set GameObject
        setGameObject();

        //Set Sound
        setSound(context);

        //Set Alert
        setAlert(activity, context);


        if(database.getSharedDatabase().getBoolean(database.getDatabaseNaming()[Data.DATANAME.s_POWERUP.ordinal()], false) == true)
        {
            speedPowerUpActive = true;
            database.getEditor().putBoolean(database.getDatabaseNaming(Data.DATANAME.s_POWERUP.ordinal()), false);
            database.getEditor().commit();
        }

        //set game state
        GameState = States.s_play;
    }

    public void restartGame()
    {
        //set game state
        GameState = States.s_play;

        //Initialize Player
        player.getPos().set(m_screenWidth / 2 - player.getSprite().getSpriteWidth() / 2, m_screenHeight / 2, 0);
        player.getNewPos().set(player.getPos().x, player.getPos().y, player.getPos().z);
        player.setScore(0);

        for(GameObject gameObj : m_cGameObjList)
        {
            if(gameObj.getGameID() == GAMEID.s_ENEMY.ordinal())
            {
                Enemy enemy = (Enemy)gameObj;
                enemy.init();
            }
            else
            {
                Item item = (Item)gameObj;
                item.init();
            }
        }

        pickUpTextDuration = -1;
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
    {
        // Create the thread
        if (!myThread.isAlive())
        {
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

        sound.endMusic(soundList);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    GameObject fetchGameObject(int GameID)
    {
        for(GameObject gameObj : m_cGameObjList)
        {
            if(gameObj.getGameID() == GameID)
            {
                return gameObj;
            }
        }

        return null;
    }

    void CollisionUpdate(GameObject gameObj)
    {
        if(gameObj.getGameID() == GAMEID.s_ENEMY.ordinal())//Enemy Update
        {
            //showAlert = true;
            alert.setShowAlert(true);
            GameState = States.s_lose;

            StartVibrate(1000);
        }
        else if(gameObj.getGameID() == GAMEID.s_CABBAGE.ordinal())//Cabbage Update
        {
            Item item = (Item)gameObj;
            Enemy enemy = (Enemy)fetchGameObject(GAMEID.s_ENEMY.ordinal());

            player.addScore(10);
            item.randVars();
            item.setActive(false);
            enemy.setSpeedIncreaseForSomeTime(-5, 1.5f);

            pickUpText.setColor(255, 0, 100, 255);
            pickUpText.setText("Good");
            sound.getSoundPool().play(soundList[SOUNDLIST.s_CORRECT.ordinal()], 1.f, 1.f, 0, 0, 1.5f);
            pickUpTextDuration = 1;
        }
        else if (gameObj.getGameID() == GAMEID.s_DRUG.ordinal()) //Drug Update
        {
            Item item = (Item)gameObj;
            Enemy enemy = (Enemy)fetchGameObject(GAMEID.s_ENEMY.ordinal());

            item.randVars();
            item.setActive(false);
            enemy.setSpeedIncreaseForSomeTime(3, 1);

            pickUpText.setColor(255, 255, 0, 0);
            pickUpText.setText("Bad");
            sound.getSoundPool().play(soundList[SOUNDLIST.s_INCORRECT.ordinal()], 1.f, 1.f, 0, 0, 1.5f);
            pickUpTextDuration = 1;

            StartVibrate(200);
        }
    }

    void UsualUpdate(GameObject gameObj, float dt)
    {
        {
            if (gameObj.getGameID() == GAMEID.s_DRUG.ordinal() || gameObj.getGameID() == GAMEID.s_CABBAGE.ordinal()) {
                Item item = (Item) gameObj;
                item.update(dt);
            } else if (gameObj.getGameID() == GAMEID.s_ENEMY.ordinal()) {
                Enemy enemy = (Enemy) gameObj;
                enemy.update(dt);
            }
        }
    }

    void UpdateGameplay(float dt, float fps)
    {
        fpsText.setText(Float.toString(fps));
        m_Background_y += 500 * dt;
        if(m_Background_y > m_screenHeight)
        {
            m_Background_y = 0;
        }

        player.update(dt, System.currentTimeMillis());

        //Text Update
        float text_x = player.getPos().x + player.getWidth() / (pickUpText.getText().length());
        float text_y = player.getPos().y - player.getHeight() / 8;
        pickUpText.getPos().set(text_x, text_y, 0);
        if (pickUpTextDuration > 0)
        {
            pickUpTextDuration -= dt;
        }

        if(!speedPowerUpActive)
        {
            for (GameObject gameObj : m_cGameObjList) {
                Rect playerBound = DLC.getBoundingBox(player.getPos(), player.getWidth(), player.getHeight());
                Rect gameObjBound = DLC.getBoundingBox(gameObj.getPos(), gameObj.getWidth(), gameObj.getHeight());

                //Collision Occured
                if (DLC.CheckCollision(playerBound, gameObjBound)) {
                    CollisionUpdate(gameObj);
                } else {
                    UsualUpdate(gameObj, dt);
                }
            }
        }
        else
        {
            speedPowerUpTimer += dt;
            if(speedPowerUpTimer > 30)
            {
                speedPowerUpTimer = 0;
                speedPowerUpActive = false;
            }
        }

    }

    //============ UPDATE ============//
    public void update(float dt, float fps)
    {
        FPS = fps;

        switch (GameState)
        {
            case s_play:
            {
                if(speedPowerUpActive)
                {
                    dt *= 10;
                }
                UpdateGameplay(dt, fps);
            }
            break;

            case s_lose:
                if(alert.getShowAlert() && !alert.getShowed())
                {
                    alert.setShowed(true);
                    alertDialog.setMessage("Your Score is " + player.getScore());
                    alert.RunAlert();
                    alert.setShowAlert(false);
                    alert.setShowed(false);
                }

                if(player.getScore() > highscore)
                {
//                    editor.putInt("Keyhighscore", player.getScore());
//                    editor.commit();
                    database.getEditor().putInt(database.getDatabaseNaming(Data.DATANAME.s_HIGHSCORE.ordinal()), player.getScore());
                    database.getEditor().commit();
                }

                //save latest score
                database.getEditor().putInt(database.getDatabaseNaming(Data.DATANAME.s_LATESTSCORE.ordinal()), player.getScore());
                database.getEditor().commit();
                break;
        }
    }

    //============ RENDER ============//
    public void doDraw(Canvas canvas)
    {
        if(canvas != null)
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
    }

    //============ RENDER GAMEPLAY ============//
    public void RenderGameplay(Canvas canvas)
    {
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y, null);
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y - m_screenHeight, null);

        //Render player
        player.getSprite().draw(canvas, player.getPos());

        //Render GameObject
        for(GameObject gameObj : m_cGameObjList)
        {
            if(gameObj.getGameID() == GAMEID.s_DRUG.ordinal() || gameObj.getGameID() == GAMEID.s_CABBAGE.ordinal())
            {
                Item item = (Item)gameObj;
                canvas.drawBitmap(item.getBitmap(), item.getPos().x, item.getPos().y, item.getPaint());
            }
            else
            {
                canvas.drawBitmap(gameObj.getBitmap(), gameObj.getPos().x, gameObj.getPos().y, null);
            }
        }


        //Debug text
        canvas.drawText(fpsText.getText(), fpsText.getPos().x, fpsText.getPos().y, fpsText.getPaint()); // Align text to top left
        canvas.drawText(player.getText().getText(), player.getText().getPos().x, player.getText().getPos().y, player.getText().getPaint());

        if(pickUpTextDuration > 0)
        {
            canvas.drawText(pickUpText.getText(), pickUpText.getPos().x, pickUpText.getPos().y, pickUpText.getPaint());
        }
    }

    //============ RENDER LOSE SCREEN ============//
    public void RenderLose(Canvas canvas)
    {
        canvas.drawBitmap(loseScreen, 0, 0, null);

        canvas.drawText(retryText.getText(), retryText.getPos().x, retryText.getPos().y, retryText.getPaint());
        canvas.drawText(exitText.getText(), exitText.getPos().x, exitText.getPos().y, exitText.getPaint());

        canvas.drawBitmap(facebookButton, m_screenWidth / 2 - facebookButton.getWidth() / 2, m_screenHeight - facebookButton.getHeight(), null);
    }

    public void StartVibrate(long ms)
    {
        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(ms);
    }

    public void PauseUpdate()
    {
        if(pause.getGamePaused())//Currently Game is paused
        {
            pause.setGamePaused(false);
            myThread.unPause();
            sound.getBackgroundMusic().start();
        }
        else //Game is not paused
        {
            pause.setGamePaused(true);
            myThread.pause();
            sound.getBackgroundMusic().pause();
        }
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
                        Rect pauseBound = DLC.getBoundingBox(pause.getPos(), pause.getWidth(), pause.getHeight());

                        if(pauseBound.contains((int)X, (int)Y))//Pressing Pause
                        {
                            movingPlayer = false;
                            PauseUpdate();
                        }
                        else
                        {
                            movingPlayer = true;
                            //limit player on the road
                            if (X < m_screenWidth / 7.2)
                                X = (short) (m_screenWidth / 7.2);
                            if (X > (m_screenWidth - m_screenWidth / 7.2))
                                X = (short) (m_screenWidth - m_screenWidth / 7.2);

                            player.getNewPos().x = (short) (X - player.getSprite().getBitmap().getWidth() / 13);
                            player.getNewPos().y = (short) (Y - player.getSprite().getBitmap().getHeight() / 2);
                        }
                    }
                    break;
                    case s_lose:
                    {
                        Rect FBbuttonBound = DLC.getBoundingBox(new Vector3((m_screenWidth/2 - facebookButton.getWidth()/2), (m_screenHeight - facebookButton.getHeight()), 0),
                                facebookButton.getWidth(), facebookButton.getHeight());

                        if(FBbuttonBound.contains((int)X, (int)Y))//Pressing Pause
                        {
                            game.finish();
                            Intent intent = new Intent();
                            intent.setClass(getContext(), Facebookpage.class);
                            activityTracker.startActivity(intent);
                        }
                        else if (X < m_screenWidth / 2)
                        {
                            restartGame();
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
                if(movingPlayer)
                {
                    //limit player on the road
                    if (X < m_screenWidth / 7.2)
                        X = (short) (m_screenWidth / 7.2);
                    if (X > (m_screenWidth - m_screenWidth / 7.2))
                        X = (short) (m_screenWidth - m_screenWidth / 7.2);

                    player.getNewPos().x = (short) (X - player.getSprite().getBitmap().getWidth() / 13);
                    player.getNewPos().y = (short) (Y - player.getSprite().getBitmap().getHeight() / 2);
                }
            }
            break;
        }
        return true;
    }
}
