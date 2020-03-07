package com.kzy.mobilesafe.bean;

import java.security.PublicKey;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 16:03
 * desc:
 */
public class FuntionBean {
    private int icon;
    private String title;
    private String desc;

    public FuntionBean(int icon, String title, String desc) {
        this.icon = icon;
        this.title = title;
        this.desc = desc;
    }

    public int getIcon() {
        return icon;

    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title == null ? "" : title;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc == null ? "" : desc;

    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
