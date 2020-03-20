package com.kzy.mobilesafe.bean;

/**
 * author: kuangzeyu2019
 * date: 2020/3/21
 * time: 1:38
 * desc:
 */
public class BlackBean {
    private String phone;
    private int mode;

    public BlackBean(String phone, int mode) {
        this.phone = phone;
        this.mode = mode;
    }

    public BlackBean(){};


    public String getPhone() {
        return phone == null ? "" : phone;

    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMode() {
        return mode;

    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackBean{" +
                "phone='" + phone + '\'' +
                ", mode=" + mode +
                '}';
    }
}
