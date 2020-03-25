package com.kzy.mobilesafe.activity.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kzy.mobilesafe.receiver.SmsReceiver;

/**
 * author: kuangzeyu2019
 * date: 2020/3/24
 * time: 23:56
 * desc: 黑名单拦截服务
 */
public class BlackInterceptService extends Service {

    private SmsReceiver mSmsReceiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerSmsListener();
        registerPhoneListener();
    }

    private void registerPhoneListener() {

    }

    private void registerSmsListener() {
        mSmsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mSmsReceiver,intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSmsReceiver!=null){
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver=null;
        }
    }
}
