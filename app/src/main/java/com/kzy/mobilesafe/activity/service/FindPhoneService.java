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
import com.kzy.mobilesafe.utils.SpUtil;


/**
 * author: kuangzeyu2019
 * date: 2020/3/13
 * time: 22:46
 * desc:
 */
public class FindPhoneService extends Service {

    private SmsReceiver mSmsReceiver;
    private DevicePolicyManager mDm;
    private ComponentName mComponentName;

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

        mDm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this,MyDeviceAdminReceiver.class);

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

    private class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("tagtag","SmsReceiver_onReceive");
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");

            for (Object data: pdus){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) data);
                String smsBody = smsMessage.getDisplayMessageBody();
                Log.d("tagtag","smsBody:"+smsBody);
                if (smsBody!=null){
                    if (smsBody.contains("#*music*#")){
                        //播放音乐
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ahjj);
                        if (!mediaPlayer.isPlaying()){
                            mediaPlayer.start();
                        }
                    }else if (smsBody.contains("#*lockscreen*#")){
                        //锁屏
                        mDm.lockNow();
                    }else if (smsBody.contains("#*wipedata*#")){
                        //清除数据
                        mDm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    }else if (smsBody.contains("#*GPS*#")){
                        //获取GPS位置信息
                        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        Location location = locationManager.getLastKnownLocation("gps");

                        String locMsg = "纬度："+location.getLatitude()+"  经度："+location.getLongitude();
                        Log.d("tagtag","locMsg:"+locMsg);

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(SpUtil.getString(getApplicationContext(),MyConstants.SAFEPHONE,"5556"),null,locMsg,null,null);



                    }


                }


            }
        }
    }
}
