package com.ss.mobileframework.GameAsset;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.ss.mobileframework.SpriteAnimation;
import com.ss.mobileframework.Utility.Vector3;

/**
 * Created by ShingLiya on 6/12/2015.
 */
public class GameObject
{
    //==============VARIABLES==============//
    protected Vector3 pos;
    protected boolean active;
    protected Bitmap bitmap; //For displaying image only
    protected SpriteAnimation sprite; //For displaying Sprite Animation

    //==============CONSTRUCTOR==============//
    public GameObject()
    {
        pos = new Vector3();
        sprite = new SpriteAnimation();
        active = false;
    }


    //==============SETTER==============//
    public void setSpriteAnimation(Bitmap bitmap, int x, int y, int fps, int frameCount)
    {
        sprite.setSpriteAnimation(bitmap, fps, frameCount);
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    //==============GETTER==============//
    public Vector3 getPos()
    {
        return pos;
    }

    public SpriteAnimation getSprite()
    {
        return sprite;
    }

    public boolean getActive()
    {
        return active;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public Rect getBound_SA()
    {
        //Top Left
//        Vector3(Pos.x - (Sprite.getSpriteWidth()/2), Pos.y - (Sprite.getSpriteHeight()/2), 0);
        //Bottom Right
//        new Vector3(Pos.x + (Sprite.getSpriteWidth()/2), Pos.y + (Sprite.getSpriteHeight()/2), 0);

        Vector3 topLeft = new Vector3();
        topLeft.x = pos.x - (sprite.getBitmap().getWidth() / 2);
        topLeft.y = pos.y - (sprite.getBitmap().getHeight() / 2);

        Vector3 bottomRight = new Vector3();
        bottomRight.x = pos.x + (sprite.getBitmap().getWidth() / 2);
        bottomRight.y = pos.y + (sprite.getBitmap().getHeight() / 2);

        return new Rect((int)topLeft.x, (int)topLeft.y, (int)bottomRight.x, (int)bottomRight.y);
    }

    public Rect getBound_Image()
    {
//        return new Rect((int)pos.x, (int)pos.y, bitmap.getWidth(), -bitmap.getHeight());

        Vector3 topLeft = new Vector3();
        topLeft.x = pos.x - (bitmap.getWidth() / 2);
        topLeft.y = pos.y - (bitmap.getHeight() / 2);

        Vector3 bottomRight = new Vector3();
        bottomRight.x = pos.x + (bitmap.getWidth() / 2);
        bottomRight.y = pos.y + (bitmap.getHeight() / 2);

        return new Rect((int)topLeft.x, (int)topLeft.y, (int)bottomRight.x, (int)bottomRight.y);
    }

//    public GameObject()
//    {
//        Pos = new Vector3();
//        Type = GO_TYPE.Unknown;
//    }

//    public GameObject(GO_TYPE type, Vector3 pos, SpriteAnimation sprite)
//    {
//        Type = type;
//        Pos = pos;
//        Sprite = sprite;
//    }
//
//    public GO_TYPE getType() {
//        return Type;
//    }
//
//    public void setType(GO_TYPE type) {
//        this.Type = type;
//    }
//
//    public Vector3 getPos() {
//        return Pos;
//    }
//
//    public void setPos(Vector3 pos) {
//        this.Pos = pos;
//    }
//
//    public SpriteAnimation getSprite() {
//        return Sprite;
//    }
//
//    public void setSpriteAnimation(SpriteAnimation sprite) {
//        this.Sprite = sprite;
//    }
//
//    public Vector3 getTopLeft() {
//        return new Vector3(Pos.x - (Sprite.getSpriteWidth()/2), Pos.y - (Sprite.getSpriteHeight()/2), 0);
//    }
//
//    public Vector3 getTopRight() {
//        return new Vector3(Pos.x + (Sprite.getSpriteWidth()/2), Pos.y - (Sprite.getSpriteHeight()/2), 0);
//    }
//
//    public Vector3 getBottomLeft() {
//        return new Vector3(Pos.x - (Sprite.getSpriteWidth()/2), Pos.y + (Sprite.getSpriteHeight()/2), 0);
//    }
//
//    public Vector3 getBottomRight() {
//        return new Vector3(Pos.x + (Sprite.getSpriteWidth()/2), Pos.y + (Sprite.getSpriteHeight()/2), 0);
//    }
}