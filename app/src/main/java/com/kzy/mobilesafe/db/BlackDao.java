package com.kzy.mobilesafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kzy.mobilesafe.bean.BlackBean;

import java.util.ArrayList;
import java.util.List;

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

    public void update(BlackBean blackBean){

        //删掉再插入能使同一个号码的旧bean位置发生变化即末尾位置，从而用户可以很容易观察得到是否添加成功
        delete(blackBean.getPhone());

        insert(blackBean);
    }

    public void insert(BlackBean blackBean){

        SQLiteDatabase database = mBlackDb.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BlackDb.BLACK_PHONE,blackBean.getPhone());

        values.put(BlackDb.BLACK_MODE,blackBean.getMode());

        database.insert(BlackDb.TBNAME,null,values);

        database.close();
    }

    /**
     * 查询所有的数据
     * @return
     */
    public List<BlackBean> queryAll(){

        List<BlackBean> list = new ArrayList<>();

        SQLiteDatabase database = mBlackDb.getReadableDatabase();

        Cursor cursor = database.query(BlackDb.TBNAME, new String[]{"" + BlackDb.BLACK_PHONE, "" + BlackDb.BLACK_MODE}, null, null, null, null, "_id desc");

        BlackBean blackBean = null;

        while (cursor.moveToNext()){

            String phone = cursor.getString(0);

            int mode = cursor.getInt(1);

            blackBean = new BlackBean(phone,mode);

            list.add(blackBean);

        }

        cursor.close();

        database.close();

        return list;
    }


    public boolean delete(String phone){

        boolean success = false;

        SQLiteDatabase database = mBlackDb.getWritableDatabase();

        int count = database.delete(BlackDb.TBNAME, "" + BlackDb.BLACK_PHONE+"=?", new String[]{phone});

        if (count>0){
            success = true;
        }

        database.close();

        return success;
    }


    /**
     * 通过要查询的 页码 来分批查询数据
     * @param pageNum 页码，从1开始
     * @param countPerPage 每次查多少条数据
     * @return
     */
    public List<BlackBean> queryPartData1(int pageNum,int countPerPage){

        List<BlackBean> blackBeans = new ArrayList<>();

        int startIndex = (pageNum-1)*countPerPage;//转换到要查询的起始位置，从0开始

        SQLiteDatabase database = mBlackDb.getReadableDatabase();

        Cursor cursor = database.query(BlackDb.TBNAME, new String[]{BlackDb.BLACK_PHONE, BlackDb.BLACK_MODE}, null, null, null, null, "_id desc", "" + startIndex + "," + countPerPage);

        BlackBean blackBean = null;

        while (cursor.moveToNext()){

            String phone = cursor.getString(0);

            int mode = cursor.getInt(1);

            blackBean = new BlackBean(phone,mode);

            blackBeans.add(blackBean);
        }

        return blackBeans;
    }


    /**通过要查询的 起始位置 来分批查询数据
     * @param startIndex 起始位置 从0开始
     * @param countPerPage 每次查多少条数据
     * @return
     */
    public List<BlackBean> queryPartData2(int startIndex,int countPerPage){

        List<BlackBean> blackBeans = new ArrayList<>();

        SQLiteDatabase database = mBlackDb.getReadableDatabase();

        Cursor cursor = database.query(BlackDb.TBNAME, new String[]{BlackDb.BLACK_PHONE, BlackDb.BLACK_MODE}, null, null, null, null, "_id desc", "" + startIndex + "," + countPerPage);

        BlackBean blackBean = null;

        while (cursor.moveToNext()){

            String phone = cursor.getString(0);

            int mode = cursor.getInt(1);

            blackBean = new BlackBean(phone,mode);

            blackBeans.add(blackBean);
        }

        return blackBeans;
    }
}
