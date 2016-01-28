package com.ss.mobileframework.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sweeseng789 on 19/1/2016.
 */
public class Data
{
    //============== VARIABLES ==============//
    public enum DATANAME
    {
        s_HIGHSCORE,
        s_LATESTSCORE,
        s_TOTAL
    }

    String databaseNaming[];
    SharedPreferences sharedDatabase;
    SharedPreferences.Editor editor;

    //============== CONSTRUCTOR ==============//
    public Data(Context context, String DatabaseName)
    {
        sharedDatabase = context.getSharedPreferences(DatabaseName, Context.MODE_PRIVATE);
        editor = sharedDatabase.edit();
        databaseNaming = new String[DATANAME.s_TOTAL.ordinal()];
    }

    //============== GETTER ==============//
    public SharedPreferences getSharedDatabase()
    {
        return sharedDatabase;
    }

    public String[] getDatabaseNaming()
    {
        return databaseNaming;
    }

    public String getDatabaseNaming(int dataName)
    {
        if(dataName < DATANAME.s_TOTAL.ordinal())
        {
            return databaseNaming[dataName];
        }

        return "";
    }

    public SharedPreferences.Editor getEditor()
    {
        return editor;
    }
}
