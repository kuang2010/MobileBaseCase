package com.kzy.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.text.format.Formatter;

import com.kzy.mobilesafe.bean.AppInfoBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/17
 * time: 9:34
 * desc:
 */
public class AppInfoUtil {

    /**
     * 获取所有安装的APP信息
     * @param context
     * @return
     */
    public static List<AppInfoBean> getAllAppsInfos(Context context){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        List<AppInfoBean> appInfoBeans = new ArrayList<>();
        for(PackageInfo packageInfo:packageInfos){
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            String packageName = appInfo.packageName;
            if (context.getPackageName().equals(packageName)){
                //是自己
                continue;
            }
            AppInfoBean appInfoBean = new AppInfoBean();
            appInfoBeans.add(appInfoBean);
            appInfoBean.setAppName(appInfo.loadLabel(pm) + "");
            appInfoBean.setIcon(appInfo.loadIcon(pm));
            appInfoBean.setPackageName(packageName);
            appInfoBean.setInstallPath(appInfo.sourceDir);
            appInfoBean.setMemSize(Formatter.formatFileSize(context,new File(appInfo.sourceDir).length()));
            appInfoBean.setSystemApp((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        }
        return appInfoBeans;
    }

    /**
     * 获取运行中的app
     * ！！！note Android 5.0+ killed getRunningTasks(int) and getRunningAppProcesses()。need use UsageStatsManager
     * @param context
     * @return
     * @deprecated please use UsageStatsManager
     */
    public static List<AppInfoBean> getRunningAppProcesses(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        List<AppInfoBean> appInfoBeans = new ArrayList<>();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo:runningAppProcesses){
            String processName = runningAppProcessInfo.processName;
            try {
                AppInfoBean appInfoBean = new AppInfoBean();
                appInfoBeans.add(appInfoBean);
                PackageManager pm = context.getPackageManager();
                ApplicationInfo appInfo = pm.getApplicationInfo(processName, 0);
                appInfoBean.setAppName(appInfo.loadLabel(pm) + "");
                appInfoBean.setIcon(appInfo.loadIcon(pm));
                appInfoBean.setPackageName(processName);
                appInfoBean.setInstallPath(appInfo.sourceDir);
                //占用内存
                android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                appInfoBean.setMemSize(Formatter.formatFileSize(context,processMemoryInfo[0].getTotalPrivateDirty()*1024));
                appInfoBean.setSystemApp((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return appInfoBeans;
    }

    /**
     * UsageStatsManager 获取近段时间打开过的APP
     * @param context
     * @return
     */
    public static List<AppInfoBean> getRunningAppProcesses2(Context context){
        List<AppInfoBean> appInfoBeans = new ArrayList<>();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        UsageStatsManager um = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStats = um.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000, System.currentTimeMillis());
        for (UsageStats us: usageStats){
            String packageName = us.getPackageName();
            try {
                AppInfoBean appInfoBean = new AppInfoBean();
                PackageManager pm = context.getPackageManager();
                ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
                appInfoBean.setAppName(appInfo.loadLabel(pm) + "");
                appInfoBean.setIcon(appInfo.loadIcon(pm));
                appInfoBean.setPackageName(packageName);
                appInfoBean.setInstallPath(appInfo.sourceDir);

                //占用运行内存
                android.os.Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{appInfo.uid});
                appInfoBean.setMemSize(Formatter.formatFileSize(context,processMemoryInfo[0].getTotalPrivateDirty()*1024));

                appInfoBean.setSystemApp((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
                appInfoBeans.add(appInfoBean);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return appInfoBeans;
    }

    /**
     * 低版本
     * 获取总内存大小
     * @return  byte
     */
    public static long getTotalMemorySpace1(){
        long space = 0;
        String meminfoPath = "/proc/meminfo";
        File file = new File(meminfoPath);
        if (file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String firstLine = reader.readLine();
                String kB = firstLine.substring(firstLine.indexOf(":") + 1, firstLine.indexOf("kB"));
                long mem = Long.parseLong(kB.trim());
                return mem*1024;//byte

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return space;
    }

    /**
     * 高版本
     * 获取总内存大小
     * @return  byte
     */
    public static long getTotalMemorySpace2(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        long totalMem = outInfo.totalMem;
        return totalMem;
    }

    /**
     * 获取可用内存大小
     * @param context
     * @return byte
     */
    public static long getAvailMemorySpace(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        long availMem = outInfo.availMem;
        return availMem;
    }


    /**
     * 判断APP是否有某种权限，适于所有版本的安卓
     * 当 API>=23时可以用 ContextCompat:checkSelfPermission()
     * @param context
     * @param permissionCode AppOpsManager里对应权限的常量值
     * @return
     */
    public static boolean isPermissionGranted(Context context,int permissionCode) {
        try {
            AppOpsManager object = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);

            if (method == null) {
                return false;
            }
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = Integer.valueOf(permissionCode);
            //arrayOfObject[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject[1] = Integer.valueOf(android.os.Process.myUid());
            arrayOfObject[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
