package com.kzy.mobilesafe.activity.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.kzy.mobilesafe.BuildConfig;
import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.utils.SpUtil;

/**
 * author: kuangzeyu2019
 * date: 2020/3/13
 * time: 22:46
 * desc:
 */
public class FindPhoneService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("tagtag","FindPhoneService_onCreate..");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tagtag","FindPhoneService_onDestroy..");
    }
}
