package com.kzy.mobilesafe.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * author: kuangzeyu2019
 * date: 2020/4/29
 * time: 10:47
 * desc: 病毒查杀dao
 */
public class VirusDao {

    public static final String path = "/data/data/com.kzy.mobilesafe/files/antivirus.db";

    /**
     * 查询文件的MD5值是不是病毒
     * @param md5
     */
    public static boolean isVirus(String md5){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select 1 from datable where md5=?", new String[]{md5});
        if (cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }


    /**
     * 增加新的病毒
     * @param md5  病毒文件的md5
     */
    public static void insertNewVirus(String md5,int type,String name,String desc){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("md5",md5);
        values.put("type",type);
        values.put("name",name);
        values.put("desc",desc);
        db.insert("datable",null,values);
        db.close();
    }

    /**
     * 获取当前病毒库版本
     */
    public static int getVirusVersion(){
        int version = 0;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select subcnt from version", null);
        if (cursor.moveToNext()){
            version = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return version;
    }

    /**
     * 更新病毒库版本
     * @param newVersion
     */
    public static void updateVirusVersion(int newVersion){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("subcnt",newVersion);
        db.update("version",values,null,null);
        db.close();
    }
}
