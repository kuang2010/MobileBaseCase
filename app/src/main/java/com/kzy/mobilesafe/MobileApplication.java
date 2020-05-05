package com.kzy.mobilesafe;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.kzy.mobilesafe.utils.AppInfoUtil;
import com.kzy.mobilesafe.utils.PhoneUtil;

import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;


/**
 * author: kuangzeyu2019
 * date: 2020/3/1
 * time: 11:42
 * desc: 一个apk 对应唯一一个application，在所有功能执行之前先运行application
 */
public class MobileApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //监控任务异常的状态
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                //未捕获的异常
                StringBuilder sb = PhoneUtil.getDeviceTypeInfo();
                sb.append(e.toString());//缺点这个错误信息里没有包含具体哪行代码出错了!!!
                boolean permissionGranted = PackageManager.PERMISSION_GRANTED==ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionGranted){
                    try {
//                        PrintWriter writer = new PrintWriter(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+new Date().getTime()+"_errorlog.txt"));
                        PrintWriter writer = new PrintWriter(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"errorlog.txt"));
                        writer.print(sb.toString());
                        writer.flush();
                        writer.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }

                //重生
//                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getPackageName());
//                launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(launchIntentForPackage);
                //挂掉
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });



        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        for (int i=0;i<50;i++){
            //模拟延时启动白屏
            SystemClock.sleep(100);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
