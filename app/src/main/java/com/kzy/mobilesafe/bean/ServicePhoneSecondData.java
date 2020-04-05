package com.kzy.mobilesafe.bean;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 12:17
 * desc:
 */
public class ServicePhoneSecondData {
    private String name;
    private String phone;

    public String getName() {
        return name == null ? "" : name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone == null ? "" : phone;

    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ServicePhoneSecondData{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
