package com.kzy.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * author: kuangzeyu2019
 * date: 2020/3/21
 * time: 0:12
 * desc:
 */
public class BlackDb extends SQLiteOpenHelper {

    public static final String DBNAME = "black";//数据库名字
    public static final int VERSION = 1;//数据库版本
    public static final String TBNAME = "blackData";//表的名字
    public static final String BLACK_PHONE = "phone";//表的字段phone
    public static final String BLACK_MODE = "mode";//表的字段mode
    public static final int MODE_PHONE = 1<<0;//1 电话拦截
    public static final int MODE_SMS = 1<<1;//10 短信拦截
    public static final int MODE_ALL = MODE_PHONE | MODE_SMS;//11 全部拦截

    public BlackDb(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+TBNAME+"(_id integer primary key autoincrement,"+BLACK_PHONE+" varchar," +
                BLACK_MODE+" integer)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table "+TBNAME);
        onCreate(db);

    }
}
