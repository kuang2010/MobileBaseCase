package com.kzy.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kzy.mobilesafe.Constant.MyConstants;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 17:04
 * desc:
 */
public class SpUtil {

    public static void putString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPNAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context,String key,String defaultValue){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPNAME, Context.MODE_PRIVATE);
        return sp.getString(key,defaultValue);
    }

    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPNAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defaultValue){
        SharedPreferences sp = context.getSharedPreferences(MyConstants.SPNAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defaultValue);
    }
}
