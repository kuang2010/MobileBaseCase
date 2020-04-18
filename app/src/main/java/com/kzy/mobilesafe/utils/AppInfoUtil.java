package com.kzy.mobilesafe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.Formatter;

import com.kzy.mobilesafe.bean.InstallAppBean;

import java.io.File;
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
    public static List<InstallAppBean> getInstallAppsInfo(Context context){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        List<InstallAppBean> installAppBeans = new ArrayList<>();
        for(PackageInfo packageInfo:packageInfos){
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            String packageName = appInfo.packageName;
            if (context.getPackageName().equals(packageName)){
                //是自己
                continue;
            }
            InstallAppBean installAppBean = new InstallAppBean();
            installAppBeans.add(installAppBean);
            installAppBean.setAppName(appInfo.loadLabel(pm) + "");
            installAppBean.setIcon(appInfo.loadIcon(pm));
            installAppBean.setPackageName(packageName);
            installAppBean.setInstallPath(appInfo.sourceDir);
            installAppBean.setMemSize(Formatter.formatFileSize(context,new File(appInfo.sourceDir).length()));
            installAppBean.setSystemApp((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        }
        return installAppBeans;
    }
}
