package com.kzy.mobilesafe.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 12:00
 * desc:
 */
public class ServicePhoneBean {

    private List<String> firstLayerDatas;


    private List<List<ServicePhoneSecondData>> secondLayerDatas;

    public List<String> getFirstLayerDatas() {
        if (firstLayerDatas == null) {
            return new ArrayList<>();
        }
        return firstLayerDatas;

    }

    public void setFirstLayerDatas(List<String> firstLayerDatas) {
        this.firstLayerDatas = firstLayerDatas;
    }


    public List<List<ServicePhoneSecondData>> getSecondLayerDatas() {
        if (secondLayerDatas == null) {
            return new ArrayList<>();
        }
        return secondLayerDatas;

    }

    public void setSecondLayerDatas(List<List<ServicePhoneSecondData>> secondLayerDatas) {
        this.secondLayerDatas = secondLayerDatas;
    }

    @Override
    public String toString() {
        return "ServicePhoneBean{" +
                "firstLayerDatas=" + firstLayerDatas +
                ", secondLayerDatas=" + secondLayerDatas +
                '}';
    }
}
