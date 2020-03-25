package com.kzy.mobilesafe.receiver;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.db.BlackDao;
import com.kzy.mobilesafe.db.BlackDb;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.utils.SpUtil;

/**
 * author: kuangzeyu2019
 * date: 2020/3/25
 * time: 21:30
 * desc:
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("tagtag","SmsReceiver_onReceive");
        DevicePolicyManager mDm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        Log.d("tagtag","pdus.length:"+pdus.length);//1

        for (Object data: pdus){

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) data);

            if (ServiceUtil.isSerivceRunning(context,"com.kzy.mobilesafe.activity.service.BlackInterceptService")){

                String address = smsMessage.getOriginatingAddress();//号码

                BlackDao blackDao = new BlackDao(context);

                int mode = blackDao.queryMode(address);

                if ((mode & BlackDb.MODE_SMS) !=0){
                    //sms 拦截
                    Toast.makeText(context,"拦截了短信："+address,Toast.LENGTH_SHORT).show();

                    abortBroadcast();
                }

            }


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
                    if (PackageManager.PERMISSION_DENIED==context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION")){
                        Log.d("tagtag","没定位权限");
                        return;
                    }
                    final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation("gps");

                    if(location!=null){
                        String locMsg = "纬度："+location.getLatitude()+"  经度："+location.getLongitude();
                        Log.d("tagtag","locMsg:"+locMsg);

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(SpUtil.getString(context,MyConstants.SAFEPHONE,"5556"),null,locMsg,null,null);
                    }else {
                        //移动位置监听
                        locationManager.requestLocationUpdates("gps", 0, 0, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                locationManager.removeUpdates(this);
                                String locMsg = "纬度："+location.getLatitude()+"  经度："+location.getLongitude();
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(SpUtil.getString(context,MyConstants.SAFEPHONE,"5556"),null,locMsg,null,null);

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
                    }

                }


            }


        }
    }
}
