package com.kzy.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kzy.mobilesafe.db.AppLockDb;

/**
 * author: kuangzeyu2019
 * date: 2020/5/2
 * time: 18:43
 * desc:
 */
public class AppLockDao {

    private final AppLockDb mAppLockDb;

    public AppLockDao(Context context) {

        mAppLockDb = new AppLockDb(context);
    }

    public void insert(String pckName){
        SQLiteDatabase writableDatabase = mAppLockDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppLockDb.COLUM_PACKAGENAME,pckName);
        writableDatabase.insert(AppLockDb.TABLENAME,null,values);
        writableDatabase.close();
    }

    public void delete(String pckName){
        SQLiteDatabase writableDatabase = mAppLockDb.getWritableDatabase();
        writableDatabase.delete(AppLockDb.TABLENAME,""+AppLockDb.COLUM_PACKAGENAME+"=?",new String[]{pckName});
        writableDatabase.close();
    }

    public boolean queryIsLock(String pckName){
        SQLiteDatabase readableDatabase = mAppLockDb.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select 1 from " + AppLockDb.TABLENAME + " where " + AppLockDb.COLUM_PACKAGENAME + " =?", new String[]{pckName});
        if (cursor.moveToNext()){
            return true;
        }
        return false;
    }
}
