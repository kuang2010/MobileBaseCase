package com.kzy.mobilesafe.activity.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.kzy.mobilesafe.db.BlackDao;
import com.kzy.mobilesafe.db.BlackDb;
import com.kzy.mobilesafe.receiver.SmsReceiver;

import java.lang.reflect.Method;

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

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener(){
            // 监听电话状态的改变
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {

                if (state == TelephonyManager.CALL_STATE_RINGING){
                    //响铃状态
                    interceptPhone(phoneNumber);
                }

                super.onCallStateChanged(state, phoneNumber);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private void interceptPhone(String phoneNumber) {
        Toast.makeText(getApplicationContext(),"拦截来电:"+phoneNumber,Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(phoneNumber)){
            BlackDao blackDao = new BlackDao(this);
            int mode = blackDao.queryMode(phoneNumber);
            if ((mode & BlackDb.MODE_PHONE) != 0){
                //是电话拦截的号码
                //则挂断电话
                endCall();
            }
        }
    }

    private void endCall() {
        Toast.makeText(getApplicationContext(),"挂断电话了",Toast.LENGTH_SHORT).show();

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //挂断被Google屏蔽了
        try {
            //1. class
            Class clazz = Class.forName("android.os.ServiceManager");
            //2. method
            Method method = clazz.getDeclaredMethod("getService", String.class);
            //3. call
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);

            //4. aidl 将ibinder 转成可调用的对象
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();// 挂断电话
            //删除电话日志


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
