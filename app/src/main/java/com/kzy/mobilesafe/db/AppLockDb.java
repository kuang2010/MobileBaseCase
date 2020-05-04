package com.kzy.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * author: kuangzeyu2019
 * date: 2020/5/2
 * time: 18:32
 * desc: 记录加了锁的APP包名的数据库
 */
public class AppLockDb extends SQLiteOpenHelper {
    public static final String DBNAME = "app_lock.db";
    public static final int VERSION = 1;
    public static final String TABLENAME = "lockData";
    public static final String COLUM_PACKAGENAME = "packageName";
    public static final Uri URI = Uri.parse("content://delete");

    public AppLockDb(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLENAME+" (" +
                "_id integer primary key autoincrement," +
                ""+COLUM_PACKAGENAME+" varchar)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+TABLENAME);
        onCreate(db);
    }
}
