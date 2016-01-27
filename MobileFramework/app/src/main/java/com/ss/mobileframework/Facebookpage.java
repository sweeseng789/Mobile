package com.ss.mobileframework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.ss.mobileframework.Utility.Data;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class Facebookpage extends FragmentActivity implements View.OnClickListener
{
    private TextView userName;
    private LoginButton loginBtn;
    private CallbackManager callbackManager;
    private LoginManager loginManager;

    ShareDialog shareDialog;

    private Button btn_FB;
    private Button Back_Button;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //SharedPreferences sharePrefscore;
    Data database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        // Initalize the SDK before executing any other operations
        //especially, if you're using Facebook UI elements
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        List<String> PERMISSIONS = Arrays.asList("publish_actions");

        //Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide top bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.facebookpage);

        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);

        loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, PERMISSIONS);

        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                userName.setText("Login successful");
                ProfilePictureView profilePictureView;
                profilePictureView = (ProfilePictureView) findViewById(R.id.picture);
                profilePictureView.setProfileId(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                userName.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                userName.setText("Login attempt failed.");

            }
        });

        btn_FB = (Button) findViewById(R.id.btn_FB);
        btn_FB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharePhotoToFacebook();
            }
        });

        Back_Button = (Button)findViewById(R.id.back_Button);
        Back_Button.setOnClickListener(this);

        //database = new Data(this, "Settings");
       // database.getDatabaseNaming()[Data.DATANAME.s_HIGHSCORE.ordinal()] = "Highscore";
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

    private void sharePhotoToFacebook(){
        int highscore = 0;

        //SharePrefscore = getSharedPreferences("Scoredata", Context.MODE_PRIVATE);

       // highscore = SharePrefscore.getInt("Passhighscore", 0);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("You have played Change. Your current Score is " + highscore + ".")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
