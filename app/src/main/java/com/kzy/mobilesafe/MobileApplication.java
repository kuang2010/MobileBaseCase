package com.kzy.mobilesafe;

import android.app.Application;

import org.xutils.x;


/**
 * author: kuangzeyu2019
 * date: 2020/3/1
 * time: 11:42
 * desc:
 */
public class MobileApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }
}
