package com.kzy.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * author: kuangzeyu2019
 * date: 2020/4/17
 * time: 9:18
 * desc:
 */
public class AppInfoBean {
    private String appName;
    private String packageName;
    private String installPath;
    private Drawable icon;
    private String memSize;//占用存储或内存大小 byte
    private boolean isSystemApp;//是否是系统APP
    private boolean isMark;//是否是标签
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;

    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isMark() {
        return isMark;

    }

    public void setMark(boolean mark) {
        isMark = mark;
    }

    public boolean isSystemApp() {
        return isSystemApp;

    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public String getAppName() {
        return appName == null ? "" : appName;

    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName == null ? "" : packageName;

    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getInstallPath() {
        return installPath == null ? "" : installPath;

    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    public Drawable getIcon() {
        return icon;

    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getMemSize() {
        return memSize == null ? "" : memSize;

    }

    public void setMemSize(String memSize) {
        this.memSize = memSize;
    }

    @Override
    public String toString() {
        return "AppInfoBean{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", installPath='" + installPath + '\'' +
                ", icon=" + icon +
                ", memSize='" + memSize + '\'' +
                ", isSystemApp=" + isSystemApp +
                ", isMark=" + isMark +
                ", isCheck=" + isCheck +
                '}';
    }
}
