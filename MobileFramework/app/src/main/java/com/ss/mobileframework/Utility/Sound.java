package com.ss.mobileframework.Utility;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore;

/**
 * Created by sweeseng789 on 18/1/2016.
 */
public class Sound
{
    //============== VARIABLES ==============//
    MediaPlayer backgroundMusic;
    SoundPool soundPool;


    //============== CONSTRUCTOR ==============//
    public Sound()
    {
        backgroundMusic = null;
        soundPool = null;
    }

    //============== SETTER ==============//
    public void setBackgroundMusic(Context context, int music)
    {
        backgroundMusic = MediaPlayer.create(context, music);
    }

    public void setUpSoundPool(int maxSound)
    {
        soundPool = new SoundPool(maxSound, AudioManager.STREAM_MUSIC, 0);
    }


    public void endMusic(int soundList[])
    {
        backgroundMusic.stop();
        backgroundMusic.release();

        for(int a = 0; a < soundList.length; ++a)
        {
            soundPool.unload(a);
        }
        soundPool.release();
    }

    //============== GETTER ==============//
    public MediaPlayer getBackgroundMusic()
    {
        return backgroundMusic;
    }

    public SoundPool getSoundPool()
    {
        return soundPool;
    }

}
