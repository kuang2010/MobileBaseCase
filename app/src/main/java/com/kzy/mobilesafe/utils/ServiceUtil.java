package com.kzy.mobilesafe.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/3/13
 * time: 23:37
 * desc:
 */
public class ServiceUtil {

    /**
     * 判断服务是否正在运行
     * @param context
     * @param serviceName 服务名称
     * @return
     */
    public static boolean isSerivceRunning(Context context,String serviceName){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices){
            if (runningServiceInfo.service.getClassName().equals(serviceName)){
                return true;
            }
        }
        return false;
    }
}
