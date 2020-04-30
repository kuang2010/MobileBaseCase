package com.kzy.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kzy.mobilesafe.dao.TelAddressDao;
import com.kzy.mobilesafe.view.MyToast;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 22:59
 * desc: 外拨电话监听
 */
public class OutGoingCallReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        String location = TelAddressDao.getLocation(number);
        new MyToast(context).showToast(location);
    }

}
