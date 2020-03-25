package com.kzy.mobilesafe.activity.service;


import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.receiver.MyDeviceAdminReceiver;
import com.kzy.mobilesafe.receiver.SmsReceiver;
import com.kzy.mobilesafe.utils.SpUtil;


/**
 * author: kuangzeyu2019
 * date: 2020/3/13
 * time: 22:46
 * desc:
 */
public class FindPhoneService extends Service {

    private SmsReceiver mSmsReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("tagtag","FindPhoneService_onCreate..");
        mSmsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mSmsReceiver,intentFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tagtag","FindPhoneService_onDestroy..");
        if (mSmsReceiver!=null){
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver=null;
        }
    }

}
