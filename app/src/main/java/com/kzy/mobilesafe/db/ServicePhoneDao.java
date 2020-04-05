package com.kzy.mobilesafe.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kzy.mobilesafe.bean.ServicePhoneBean;
import com.kzy.mobilesafe.bean.ServicePhoneSecondData;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 11:28
 * desc: 常用服务号码查询
 */
public class ServicePhoneDao {

    private static final String DBPATH = "/data/data/com.kzy.mobilesafe/files/commonnum.db";

    public static ServicePhoneBean getServicePhoneDatas(){

        ServicePhoneBean servicePhoneBean = new ServicePhoneBean();

        SQLiteDatabase database = SQLiteDatabase.openDatabase(DBPATH, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = database.rawQuery("select name,idx from classlist", null);

        List<String> firstLayerDatas = new ArrayList<>();

        servicePhoneBean.setFirstLayerDatas(firstLayerDatas);

        List<List<ServicePhoneSecondData>> secondLayerDatas = new ArrayList<>();

        servicePhoneBean.setSecondLayerDatas(secondLayerDatas);

        while (cursor.moveToNext()){

            String firstName = cursor.getString(0);

            String idx = cursor.getString(1);

            firstLayerDatas.add(firstName);

            Cursor cursor1 = database.rawQuery("select name,number from table" + idx, null);

            List<ServicePhoneSecondData> servicePhoneSecondDatas = new ArrayList<>();

            secondLayerDatas.add(servicePhoneSecondDatas);

            while (cursor1.moveToNext()){

                String secondName = cursor1.getString(0);

                String secondPhoneNum = cursor1.getString(1);

                ServicePhoneSecondData secondData = new ServicePhoneSecondData();

                secondData.setName(secondName);

                secondData.setPhone(secondPhoneNum);

                servicePhoneSecondDatas.add(secondData);

            }

            cursor1.close();

        }


        cursor.close();

        database.close();


        return servicePhoneBean;

    }
}
