package com.kzy.mobilesafe.activity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * author: kuangzeyu2019
 * date: 2020/3/24
 * time: 23:56
 * desc: 黑名单拦截服务
 */
public class BlackInterceptService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
