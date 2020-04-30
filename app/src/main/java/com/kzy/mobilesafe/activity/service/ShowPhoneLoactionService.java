package com.kzy.mobilesafe.activity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.kzy.mobilesafe.dao.TelAddressDao;
import com.kzy.mobilesafe.view.MyToast;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 17:52
 * desc:
 */
public class ShowPhoneLoactionService extends Service {

    private MyToast mMyToast;
    private TelephonyManager mTelephonyManager;
    private PhoneStateListener mPhoneStateListener;
    private OutGoingCallReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //来电监听
        registerCallInListener();

        //呼叫监听
        registerCallOutListener();

        mMyToast = new MyToast(this);

    }

    //呼叫监听
    private void registerCallOutListener() {

        mReceiver = new OutGoingCallReceiver();

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);

        registerReceiver(mReceiver,filter);


    }


    //来电监听和挂断电话监听
    private void registerCallInListener() {

        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        mPhoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {

                switch (state) {

                    case TelephonyManager.CALL_STATE_IDLE://停止
                        mMyToast.hideToast();
                        break;

                    case TelephonyManager.CALL_STATE_RINGING://响铃

                        Toast.makeText(getApplicationContext(), "" + getLoaction(phoneNumber), Toast.LENGTH_SHORT).show();

                        mMyToast.showToast(getLoaction(phoneNumber));

                        break;

                    case TelephonyManager.CALL_STATE_OFFHOOK://通话

                        break;

                }


                super.onCallStateChanged(state, phoneNumber);
            }
        };

        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private String getLoaction(String phoneNumber) {

        return TelAddressDao.getLocation(phoneNumber);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }


    class OutGoingCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String location = TelAddressDao.getLocation(number);
            mMyToast.showToast(location);
        }

    }


}
