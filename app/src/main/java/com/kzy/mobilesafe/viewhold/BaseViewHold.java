package com.kzy.mobilesafe.viewhold;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kzy.mobilesafe.bean.AppInfoBean;

/**
 * author: kuangzeyu2019
 * date: 2020/4/25
 * time: 22:55
 * desc:
 */
public abstract class BaseViewHold extends RecyclerView.ViewHolder {
    public BaseViewHold(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void setData(AppInfoBean appInfoBean);
}
