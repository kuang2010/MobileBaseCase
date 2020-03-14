package com.kzy.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.kzy.mobilesafe.BuildConfig;
import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.activity.service.FindPhoneService;
import com.kzy.mobilesafe.utils.SpUtil;

/**
 * author: kuangzeyu2019
 * date: 2020/3/14
 * time: 0:17
 * desc: 开机启动完成的广播接收者
 */
public class MyBootComplementReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tagtag","MyBootComplementReceiver_onReceive");
        boolean startService = SpUtil.getBoolean(context, MyConstants.IS_BOOTCOMPLETE_START_SERVICE, false);
        Log.d("tagtag","startService:"+startService);
        if (startService){
            Intent intent2 = new Intent(context,FindPhoneService.class);
            context.startService(intent2);
        }


        try{
            Log.d("tagtag"," TelephonyManager :");

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSerialNumber = tm.getSimSerialNumber();
            if (BuildConfig.DEBUG || TextUtils.isEmpty(currentSerialNumber)){
                currentSerialNumber = tm.getDeviceId();
                currentSerialNumber = currentSerialNumber+""+System.currentTimeMillis();
            }
            String simNum_save = SpUtil.getString(context, MyConstants.SIM_NUM, "");
            Log.d("tagtag"," simNum_save :"+simNum_save);

            if (!TextUtils.isEmpty(simNum_save)){
                Log.d("tagtag"," currentSerialNumber :"+currentSerialNumber);
                if (!simNum_save.equals(currentSerialNumber)){
                    //SIM不一致，给安全号码发送短信

                    SmsManager smsManager = SmsManager.getDefault();

                    smsManager.sendTextMessage(SpUtil.getString(context,MyConstants.SAFEPHONE,"110"),null,"小偷来了_"+System.currentTimeMillis(),null,null);

                    Log.d("tagtag"," smsManager.sendTextMessage");

                }else {
                    Log.d("tagtag","SIM一致");

                }
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("tagtag","Exception:"+e.getMessage());
        }


    }
}
