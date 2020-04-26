package com.kzy.mobilesafe.viewhold;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.AppInfoBean;

/**
 * author: kuangzeyu2019
 * date: 2020/4/25
 * time: 18:06
 * desc:
 */
public class ProcessAppMarkViewHold extends BaseViewHold{

    private final TextView mTv_mark_process;

    public ProcessAppMarkViewHold(@NonNull View itemView) {
        super(itemView);
        mTv_mark_process = itemView.findViewById(R.id.tv_mark_process);
    }

    @Override
    public void setData(AppInfoBean appInfoBean) {
        mTv_mark_process.setText(appInfoBean.isSystemApp()?"系统软件":"应用软件");
    }
}
