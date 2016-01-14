package com.ss.mobileframework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ss.mobileframework.Highscore.Highscore;

public class Mainmenu extends Activity implements View.OnClickListener
{
    private Button Start_Button;
    private Button Help_Button;
    private Button Highscore_Button;
    private Button Exit_Button;

    /*
    public void alertDialog_highscore()
    {
        int highscore = 0;
        sharePrefscore = getSharedPreferences("Scoredata", Context.MODE_PRIVATE);

        highscore = SharePrefscore.getInt("Keyhighscore", 0);

        alert_score = new AlertDialog.Builder(this);
        alert_score.setCancelable(false);
        alert_score.setIcon(R.drawable.score);
        alert_score.setTitle("Your highscore").setMessage("Highscore :" + highscore).setPositiveButton("Ok", new DialogInterface.OnClickListener()
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        Start_Button = (Button)findViewById(R.id.Start_Button);
        Start_Button.setOnClickListener(this);

        Highscore_Button = (Button)findViewById(R.id.Highscore_Button);
        Highscore_Button.setOnClickListener(this);

        Help_Button = (Button)findViewById(R.id.Help_Button);
        Help_Button.setOnClickListener(this);

        Exit_Button = (Button)findViewById(R.id.Exit_Button);
        Exit_Button.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if(v == Start_Button)
        {
            intent.setClass(this, Gamepage.class);
        }
        else if(v == Highscore_Button)
        {
            intent.setClass(this, Highscore.class);
        }
        else if (v == Help_Button)
        {
            intent.setClass(this, Helppage.class);
        }

        else if (v == Exit_Button)
        {

        }

        startActivity(intent);
    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onStop()
    {
        super.onStop();
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}
