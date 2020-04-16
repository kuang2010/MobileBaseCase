package com.kzy.mobilesafe.utils;

import android.content.Context;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.File;

/**
 * author: kuangzeyu2019
 * date: 2020/4/16
 * time: 22:43
 * desc:
 */
public class PhoneUtil {

    /**
     * 获取总的手机内存大小
     * @return byte
     */
    public static  long getTotalPhoneMemory(){
        File dataDirectory = Environment.getDataDirectory();
        long totalSpace = dataDirectory.getTotalSpace();
        return totalSpace;
    }

    /**
     * 获取还可用的手机内存大小
     * @return byte
     */
    public static long getFreePhoneMemory(){
        File dataDirectory = Environment.getDataDirectory();
        long freeSpace = dataDirectory.getFreeSpace();
        return freeSpace;
    }

    /**
     * 获取SD卡总内存大小
     * @return byte
     */
    public static long getTotalSdcardMemory(){
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        long totalSpace = externalStorageDirectory.getTotalSpace();
        return totalSpace;
    }

    /**
     * 获取sd卡可用内存
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
}
