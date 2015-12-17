package com.ss.mobileframework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.nfc.Tag;
import android.os.Vibrator;
import android.preference.DialogPreference;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import android.content.DialogInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.text.InputFilter;
import android.text.InputType;

public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    // Implement this interface to receive information about changes to the surface.

    private GameThread myThread = null; // Thread to control the rendering

    // 1a) Variables used for background rendering
    private Bitmap m_Background, m_BackgroundScale; //Used for rendering background
   // private Bitmap m_Spaceship; //Used for rendering spaceship

    // 1b) Define Screen width and Screen height as integer
    int m_screenWidth, m_screenHeight;

    // 1c) Variables for defining background start and end point
    private short m_Background_x = 0, m_Background_y = 0;

    // 4a) bitmap array to stores 4 images of the spaceship
    private Bitmap[] m_Spaceship = new Bitmap[4];

    // 4b) Variable as an index to keep track of the spaceship images
    private short m_SpaceshipIndex = 0;

    // Variables for FPS
    public float FPS;
    float deltaTime;
    long dt;

    // Variable for Game State check
    private short GameState;

    //virables for new location upon touching screen
    private short mX = 0, mY = 0;

    //SpriteAnimation
    private SpriteAnimation stone_anim;

    //score system and lives
    int scoreno, hits;
    Bitmap score;
    boolean moveShip;

    //vibration
    public Vibrator v;

    protected static final String TAG = null;

    //bgm playeer
    MediaPlayer bgm;

    //sounds player- short simple easy
    private SoundPool sounds;
    private int soundCorrect , soundWrong, soundBonus;


    //pause variables
    private boolean pausepress = true;
    private Objects PauseB1;
    private Objects PauseB2;

    //alert variables - popout message
    public boolean showAlert = false;
    AlertDialog.Builder alert = null;
    Activity activityTracker;
    public boolean showed = false;
    private Alerts AlertObj;

    //constructor for this GamePanelSurfaceView class
    public GamePanelSurfaceView (Context context, final Activity activity)
    {
        // Context is the current state of the application/object
        super(context);

        //to track an activity
        activityTracker = activity;

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

        // Create the game loop thread
        myThread = new GameThread(getHolder(), this);

        //load spriteAnimation
        stone_anim = new SpriteAnimation(BitmapFactory.decodeResource(getResources(), R.drawable.flystone), 320, 64, 5, 5);

        scoreno = 0;
        hits = 3;
        score = BitmapFactory.decodeResource(getResources(), R.drawable.star);
        moveShip = false;

        //background music
        bgm = MediaPlayer.create(context, R.raw.background_music);

        //soundPool - sound effect
        //SoundPool(Max number of sound effects, stream type used, rate converter[0 means no change effect])
        sounds = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        //*NOTE: last parameter is the priority of sound - usually set to 1
        soundCorrect = sounds.load(context, R.raw.correct, 1);
        soundWrong= sounds.load(context, R.raw.incorrect, 1);

        //pause button
        PauseB1 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause), 72, 72);
        PauseB2 = new Objects(BitmapFactory.decodeResource(getResources(), R.drawable.pause1), 72, 72);

        //alert variables
            //create alert dialog
            AlertObj = new Alerts(this);
            alert = new AlertDialog.Builder(getContext());

            //allow player to input their name
            final EditText input = new EditText(getContext());

            //define the input method where 'enter' key is disabled
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            //define max of 20 character to be entered in to the 'name' field;
            int maxLength = 20;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            input.setFilters(FilterArray);

            alert.setCancelable(false);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                //do something when button is clicked
                        public void onClick(DialogInterface arg0, int arg1){
                            Intent intent = new Intent();
                            intent.setClass(getContext(),Mainmenu.class);
                            activityTracker.startActivity(intent);
                        }
            }
            );

        // Make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    //must implement inherited abstract methods
    public void surfaceCreated(SurfaceHolder holder)
    {
        //setVolume(float Left vol, float Right vol)
        bgm.setVolume(0.8f,0.8f);
        bgm.start();
        // Create the thread
        if (!myThread.isAlive()){
            myThread = new GameThread(getHolder(), this);
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void startVibrate(){
        long pattern[] = {0, 200, 500};
        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(pattern, 0);
        Log.v(TAG, "testV");
    }

    public void stopVibrate(){
        v.cancel();
    }

    public void surfaceDestroyed(SurfaceHolder holder)
    {
        bgm.stop();
        bgm.release();

        sounds.unload(soundCorrect);
        sounds.unload(soundWrong);
        sounds.release();

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

    public void RenderGameplay(Canvas canvas) {
        // 2) Re-draw 2nd image after the 1st image ends
        if(canvas == null) //New Canvas
        {
            return;
        }
        canvas.drawBitmap(m_BackgroundScale, m_Background_x, m_Background_y, null);
        canvas.drawBitmap(m_BackgroundScale, m_Background_x + m_screenWidth, m_Background_y, null);

        // 4d) Draw the spaceships
        canvas.drawBitmap(m_Spaceship[m_SpaceshipIndex], mX, mY, null);


        // Bonus) To print FPS on the screen
        Paint paint = new Paint();
        paint.setARGB(255,0,0,0);
        paint.setStrokeWidth(100);
        paint.setTextSize(30);
        canvas.drawText("FPS: " + FPS, 130, 75,paint);
        //render score
        canvas.drawText("Score: " + scoreno, 130, 110, paint);
        // render lives
        for(int i = 1 ; i <= hits; i++)
        {
            canvas.drawBitmap(score, 28 + i * 30 , m_screenHeight - 700 , null );
        }

        //draw sprite animation
        stone_anim.draw(canvas);
        RenderPause(canvas);
    }


    //Update method to update the game play
    public void update(float dt, float fps)
    {
        FPS = fps;

        if(hits == 0)
        {
          //  showAlert = true;
          //  Game Ends
        }
        if(showAlert == true && !showed)
        {
            showed = true;
            alert.setMessage("GameOver");
            AlertObj.runAlert();
            showAlert = false;
            showed = false;
        }

        //update sprite animation
        stone_anim.update(System.currentTimeMillis());

        switch (GameState) {
            case 0:
            {
                // 3) Update the background to allow panning effect
                m_Background_x -= 500 * dt; // Allow panning speed
                if(m_Background_x < -m_screenWidth)
                {
                    m_Background_x = 0;
                }


                // 4e) Update the spaceship images / shipIndex so that the animation will occur.
                m_SpaceshipIndex++;
                m_SpaceshipIndex %= 4;

            }
            break;
        }
    }

    public void RenderPause(Canvas canvas)
    {
        //draw pause button
        canvas.drawBitmap(PauseB1.getBitmap(), PauseB1.getX(), PauseB1.getY(), null);

        if(pausepress == true) {
            canvas.drawBitmap(PauseB2.getBitmap(), PauseB2.getX(), PauseB2.getY(), null);
        }
        else{
            canvas.drawBitmap(PauseB1.getBitmap(), PauseB1.getX(), PauseB1.getY(), null);
            pausepress = false;
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // 5) In event of touch on screen, the spaceship will relocate to the point of touch
        short X = (short)event.getX();
        short Y = (short)event.getY();
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                if(pausepress == false && CheckCollision(mX, mY , m_Spaceship[m_SpaceshipIndex].getWidth(), m_Spaceship[m_SpaceshipIndex].getHeight(),X, Y, 0,0) )
                    moveShip = true;
                else
                    moveShip = false;
                if(moveShip == false && pausepress == false && CheckCollision(PauseB1.getX(), PauseB1.getY(), PauseB1.getWidth(), PauseB1.getHeight(), X, Y, 0, 0) ) {
                    pausepress = true;
                    myThread.pause();
                    bgm.pause();
                }
                else {
                    pausepress = false;
                    myThread.unPause();
                    bgm.start();
                }
            }
            case MotionEvent.ACTION_MOVE:
            {
                if(moveShip == true)
                {
                    //new location where the imgae to land on
                    mX = (short)(X - m_Spaceship[m_SpaceshipIndex].getWidth() / 2);
                    mY = (short)(Y - m_Spaceship[m_SpaceshipIndex].getHeight() / 2);

                }
                //check for collision with asteroid
                if( CheckCollision(mX, mY , m_Spaceship[m_SpaceshipIndex].getWidth(), m_Spaceship[m_SpaceshipIndex].getHeight(),
                        stone_anim.getX(), stone_anim.getY(), stone_anim.getSpriteWidth(), stone_anim.getSpriteHeight()) )
                {
                    Random r = new Random();
                    stone_anim.setX(r.nextInt(m_screenWidth));
                    stone_anim.setY(r.nextInt(m_screenHeight));
                    scoreno += 10;
                    hits -= 1;
                    startVibrate();
                    // play(SoundID, leftVolume, rightVolume, stream priority(0 = lowest),
                    //              loop (0 = false, 1 = true), playbackRate(1.0 = normal, range [ 0.5 - 2.0] );
                    sounds.play(soundCorrect, 1.0f, 1.0f, 0, 0, 1.5f);
                }
                break;
            }

        }
        return true;


        //return super.onTouchEvent(event);
    }

    public boolean CheckCollision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2){

        //start to detect collision of the top left corner
        if(x2 >= x1 && x2 <= x1 + w1) // checking leftside
        {
            if(y2 > y1 && y2 < y1 + h1)
            {
                //checking top left corner
                return true;
            }
            if(y2 + h2 > y1 && y2 + h2 < y1 + h1)
            {
                //checking bottom left corner
                return true;
            }
        }
        if(x2 + w2 >= x1 && x2 + w2 <= x1 + w1) // checkng right side
        {
            if(y2 > y1 && y2 < y1 + h1)
            {
                //checking top right corner
                return true;
            }
            if(y2 + h2 > y1 && y2 + h2 < y1 + h1)
            {
                //cheking bottom right
                return true;
            }
        }
        return false;
    }
}
