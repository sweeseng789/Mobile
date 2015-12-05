package com.ss.mobileframework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Optionspage extends Activity implements View.OnClickListener
{
    private Button Back_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.optionspage);

        Back_Button = (Button)findViewById(R.id.back_Button);
        Back_Button.setOnClickListener(this);
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
