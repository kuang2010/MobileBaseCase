package com.kzy.mobilesafe.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.audiofx.PresetReverb;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.File;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/16
 * time: 22:43
 * desc:
 */
public class PhoneUtil {

    /**
     * 获取总的手机存储空间大小
     * @return byte
     */
    public static  long getTotalPhoneMemory(){
        File dataDirectory = Environment.getDataDirectory();
        long totalSpace = dataDirectory.getTotalSpace();
        return totalSpace;
    }

    /**
     * 获取还可用的手机存储空间大小
     * @return byte
     */
    public static long getFreePhoneMemory(){
        File dataDirectory = Environment.getDataDirectory();
        long freeSpace = dataDirectory.getFreeSpace();
        return freeSpace;
    }

    /**
     * 获取SD卡总存储空间大小
     * @return byte
     */
    public static long getTotalSdcardMemory(){
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        long totalSpace = externalStorageDirectory.getTotalSpace();
        return totalSpace;
    }

    /**
     * 获取sd卡可用存储空间
     * @return byte
     */
    public static long getFreeSdcardMemory(){
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        long freeSpace = externalStorageDirectory.getFreeSpace();
        return freeSpace;
    }


    /**
     * 将字节自动转成适合的单位Byte
     * @param context
     * @param sizeBylte
     * @return
     */
    public static String formatFileSize(Context context ,long sizeBylte){
        return  Formatter.formatFileSize(context,sizeBylte);
    }

    /**
     * 判断设备上是否有某个页面
     * @param context
     * @param intent 页面注册时的intent-filter
     * @return
     */
    public static boolean hasActivity(Context context,Intent intent){
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        ResolveInfo resolveInfo = resolveInfos.get(0);
//        ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
        return resolveInfos.size()>0;
    }
}
