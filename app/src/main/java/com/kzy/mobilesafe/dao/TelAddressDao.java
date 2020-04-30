package com.kzy.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: kuangzeyu2019
 * date: 2020/4/4
 * time: 18:24
 * desc: 电话归宿地查询
 */
public class TelAddressDao {

    private static final String ADDRESSDBPATH = "/data/data/com.kzy.mobilesafe/files/address.db";

    public static String getLocation(String telPhone){

        String location = "未知";

        SQLiteDatabase database = SQLiteDatabase.openDatabase(ADDRESSDBPATH, null, SQLiteDatabase.OPEN_READONLY);

        //判断是否是手机号
        if (isMobilePhone(telPhone)){
            //只要前面7位
            String tel = telPhone.substring(0, 7);

            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{tel});

            if (cursor.moveToNext()){

                location = cursor.getString(0);
            }

            cursor.close();

            database.close();

        }else {
            //是固定电话

            String area = "";
            if (telPhone.charAt(1) == '1' || telPhone.charAt(1) == '2') {
                //2位的区号
                area = telPhone.substring(1,3);
            } else {
                //3位的区号
                area = telPhone.substring(1,4);
            }

            if (area.length()>0){

                Cursor cursor = database.rawQuery("select location from data2 where area=?", new String[]{area});

                if (cursor.moveToNext()){

                    location = cursor.getString(0);
                }

            }
        }

        return location.replace("电信","").replace("移动","").replace("联通","");

    }


    public static boolean isMobilePhone(String telPhone) {
        String reg = "1[345678][0-9]{9}";//中括号是字符集选择，大括号是次数
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(telPhone);
        boolean matches = matcher.matches();
        return matches;
    }

}
