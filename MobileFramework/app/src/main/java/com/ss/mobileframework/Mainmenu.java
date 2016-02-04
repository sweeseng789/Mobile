package com.ss.mobileframework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.ss.mobileframework.ShopPage.Shop;
import com.ss.mobileframework.Utility.Data;

public class Mainmenu extends Activity implements View.OnClickListener
{
    private Button Start_Button;
    private Button Shop_Button;
    private Button Help_Button;
    private Button Highscore_Button;
    private Button Exit_Button;

    //SharedPreferences sharePrefscore;
    Data database;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.mainmenu);

        Start_Button = (Button)findViewById(R.id.Start_Button);
        Start_Button.setOnClickListener(this);

        Shop_Button = (Button)findViewById(R.id.Shop_Button);
        Shop_Button.setOnClickListener(this);

        Highscore_Button = (Button)findViewById(R.id.Highscore_Button);
        Highscore_Button.setOnClickListener(this);

        Help_Button = (Button)findViewById(R.id.Help_Button);
        Help_Button.setOnClickListener(this);

        Exit_Button = (Button)findViewById(R.id.Exit_Button);
        Exit_Button.setOnClickListener(this);

        database = new Data(this, "Settings");
        database.getDatabaseNaming()[Data.DATANAME.s_HIGHSCORE.ordinal()] = "Highscore";
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if(v != Highscore_Button)
        {
            if(v == Start_Button)
            {
                intent.setClass(this, Gamepage.class);
            }
            else if (v == Help_Button)
            {
                intent.setClass(this, Helppage.class);
            }
            else if(v == Shop_Button)
            {
                intent.setClass(this, Shop.class);
            }
            startActivity(intent);
        }
        else
        {
            loadHighscore();
        }
    }

    public void loadHighscore()
    {
        int highscore = database.getSharedDatabase().getInt(database.getDatabaseNaming(Data.DATANAME.s_HIGHSCORE.ordinal()), 0);
        Toast.makeText(this, "Highscore: " + highscore, Toast.LENGTH_LONG).show();
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
