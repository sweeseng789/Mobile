package com.ss.mobileframework.Highscore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ss.mobileframework.Mainmenu;
import com.ss.mobileframework.R;

/**
 * Created by sweeseng789 on 9/12/2015.
 */
public class Highscore extends Activity implements View.OnClickListener
{
    private Button Back_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.highscore);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Supernatural_Knight.ttf");
        TextView myTextView = (TextView)findViewById(R.id.textView);
        myTextView.setTypeface(myTypeface);


        Back_Button = (Button)findViewById(R.id.back_Button);
        Back_Button.setOnClickListener(this);

        //Set GamePanelSurfaceView as the View
        setContentView(new View_Highscore(this));
    }

    public void onClick(View v)
    {
        Intent intent = new Intent();

        if(v == Back_Button)
        {
            intent.setClass(this, Mainmenu.class);
        }
        startActivity(intent);
    }
    protected void returnToMenu()
    {
        Intent intent = new Intent();
        intent.setClass(this, Mainmenu.class);
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
