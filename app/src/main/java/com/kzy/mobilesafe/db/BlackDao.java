package com.kzy.mobilesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kzy.mobilesafe.bean.BlackBean;

/**
 * author: kuangzeyu2019
 * date: 2020/3/21
 * time: 1:37
 * desc:
 */
public class BlackDao {

    private final BlackDb mBlackDb;

    public BlackDao(Context context){

        mBlackDb = new BlackDb(context);
    }

    public void insert(BlackBean blackBean){

        SQLiteDatabase database = mBlackDb.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BlackDb.BLACK_PHONE,blackBean.getPhone());

        values.put(BlackDb.BLACK_MODE,blackBean.getMode());

        database.insert(BlackDb.TBNAME,null,values);
    }
}
