package org.example.xwalkembedded;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mac on 2017/7/26.
 */

public class SharedPreferencesHelper {
    private String PREFERENCE = "preference";
    private Context context;
    public SharedPreferencesHelper(Context context){
        this.context = context;
    }
    public void saveInteger(String key,int value){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveString(String key,String value){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void saveBoolean(String key,boolean value){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public int getInteger(String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return  preferences.getInt(key,0);
    }

    public String getString(String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return  preferences.getString(key,null);
    }
    public boolean getBoolean(String key){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return  preferences.getBoolean(key,true);
    }
}
