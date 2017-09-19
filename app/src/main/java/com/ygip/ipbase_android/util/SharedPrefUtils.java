package com.ygip.ipbase_android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ygip.ipbase_android.App;

/**
 * Created by LockyLuo on 2017/9/17.
 */

public class SharedPrefUtils {
    public static void save(String name,String data){
        SharedPreferences sharedPreferences= App.getInstance().getSharedPreferences("ipbase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(name,data);
        editor.commit();
    }

    public static String load(String name){
        String data="";
        SharedPreferences sharedPreferences= App.getInstance().getSharedPreferences("ipbase", Context.MODE_PRIVATE);
        data=sharedPreferences.getString(name,data);
        return data;
    }

    public  static void Clear(){
        SharedPreferences sharedPreferences= App.getInstance().getSharedPreferences("ipbase", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
