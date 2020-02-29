package com.kzy.mobilesafe.bean;

/**
 * author: kuangzeyu2019
 * date: 2020/2/29
 * time: 22:59
 * desc:
 */
public class VersionInfo {
    /*
    {"versionName":"奇异版","versionCode":"2",
    "desc":"修复了已知bug，增加了趣味玩毛线功能",
    "url":"http://192.168.2.140:8080/"}
    * */

    private String versionName;
    private int versionCode;
    private String desc;
    private String url;

    public String getVersionName() {
        return versionName == null ? "" : versionName;

    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;

    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDesc() {
        return desc == null ? "" : desc;

    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url == null ? "" : url;

    }

    public void setUrl(String url) {
        this.url = url;
    }
}
