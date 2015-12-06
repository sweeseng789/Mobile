package com.ss.mobileframework;

import com.ss.mobileframework.Utility.Vector3;

/**
 * Created by ShingLiya on 6/12/2015.
 */
public class GameObject {
    public enum GO_TYPE{
        Unknown,
        Player,
        Enemy
    };

    private GO_TYPE Type;
    public Vector3 Pos;
    private SpriteAnimation Sprite;

    public GameObject()
    {
        Pos = new Vector3();
        Type = GO_TYPE.Unknown;
    }

    public GameObject(GO_TYPE type, Vector3 pos, SpriteAnimation sprite)
    {
        Type = type;
        Pos = pos;
        Sprite = sprite;
    }

    public GO_TYPE getType() {
        return Type;
    }

    public void setType(GO_TYPE type) {
        this.Type = type;
    }

    public Vector3 getPos() {
        return Pos;
    }

    public void setPos(Vector3 pos) {
        this.Pos = pos;
    }

    public SpriteAnimation getSprite() {
        return Sprite;
    }

    public void setSpriteAnimation(SpriteAnimation sprite) {
        this.Sprite = sprite;
    }

    public Vector3 getTopLeft() {
        return new Vector3(Pos.x - (Sprite.getSpriteWidth()/2), Pos.y - (Sprite.getSpriteHeight()/2), 0);
    }

    public Vector3 getTopRight() {
        return new Vector3(Pos.x + (Sprite.getSpriteWidth()/2), Pos.y - (Sprite.getSpriteHeight()/2), 0);
    }

    public Vector3 getBottomLeft() {
        return new Vector3(Pos.x - (Sprite.getSpriteWidth()/2), Pos.y + (Sprite.getSpriteHeight()/2), 0);
    }

    public Vector3 getBottomRight() {
        return new Vector3(Pos.x + (Sprite.getSpriteWidth()/2), Pos.y + (Sprite.getSpriteHeight()/2), 0);
    }
}