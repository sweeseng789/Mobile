package com.ss.mobileframework.GameAsset;

import android.graphics.Rect;

import com.ss.mobileframework.Text.CText;
import com.ss.mobileframework.Utility.Vector3;

/**
 * Created by sweeseng789 on 6/12/2015.
 */
public class Player extends GameObject
{
    //==============VARIABLES==============//
    private Vector3 newPos;
    private static float velocity;
    private Vector3 vel;
    private CText text;
    private int score;

    //==============CONSTRUCTOR==============//
    public Player()
    {
        vel = new Vector3();
        newPos = new Vector3();
        velocity = 100;
        score = 0;
        text = new CText();
        text.getPos().set(0, 100, 0);
        text.setColor(255, 255, 10, 0);
        text.setScale(50);
    }


    //==============GETTER==============//
    public Vector3 getNewPos()
    {
        return newPos;
    }

    public int getScore()
    {
        return score;
    }

    public CText getText()
    {
        return text;
    }

    //==============SETTER==============//
    public void update(double dt, long SA_dt)
    {
        //Pos update
        Vector3 diff = new Vector3();
        diff.x = newPos.x - pos.x;
        diff.y = newPos.y - pos.y;

        if(diff.length() > 5)
        {
            Vector3 normalizedPos = new Vector3();
            normalizedPos.x = newPos.x - pos.x;
            normalizedPos.y = newPos.y - pos.y;
            normalizedPos.normalize();

            vel.x += normalizedPos.x * velocity * dt;
            vel.y += normalizedPos.y * velocity * dt;

            //Update Vel
            if(!vel.isZero())
            {
                float forceX = 0 - vel.x;
                vel.x += forceX * dt * 8;

                float forceY = 0 - vel.y;
                vel.y += forceY * dt * 8;
            }
        }
        else
        {
            vel.setZero();
        }

        //Update Text
        text.setText(Integer.toString(score));

        //Sprite animation update
        sprite.update(SA_dt);

        //Update Pos
        pos.x += vel.x;
        pos.y += vel.y;
    }

    public void addScore(int scoreToAdd)
    {
        score += scoreToAdd;
    }
}
