package com.kzy.mobilesafe.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

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
    private String memSize;//安装apk占用存储空间或运行时app占用内存大小 byte
    private boolean isSystemApp;//是否是系统APP
    private boolean isMark;//是否是标签
    private boolean isCheck;//进程管理中是否被选上了
    private boolean isVirus;//是否是病毒
    private long cacheSize;//产生的文件资源占用缓存空间大小 byte
    private boolean isLock;//是否加锁

    public AppInfoBean(){};

    //重写equals和hashcode方便造假删除某个对象
    public AppInfoBean(String packageName){
        this.packageName = packageName;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof AppInfoBean){
            AppInfoBean o = (AppInfoBean) obj;
            if (o.getPackageName().equals(this.getPackageName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return packageName.hashCode();
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
                ", isVirus=" + isVirus +
                ", cacheSize=" + cacheSize +
                ", isLock=" + isLock +
                '}';
    }

    public boolean isLock() {
        return isLock;

    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public long getCacheSize() {
        return cacheSize;

    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public boolean isVirus() {
        return isVirus;

    }

    public void setVirus(boolean virus) {
        isVirus = virus;
    }

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

}
