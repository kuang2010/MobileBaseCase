package com.kzy.mobilesafe.viewhold;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.AppInfoBean;

/**
 * author: kuangzeyu2019
 * date: 2020/4/25
 * time: 18:11
 * desc:
 */
public class ProcessAppRuningViewHold extends BaseViewHold {

    private final ImageView mIv_icon_app;
    private final TextView mTv_name_app;
    private final TextView mTv_path_app;
    private final CheckBox mCb_clear_process_app;

    public ProcessAppRuningViewHold(@NonNull View itemView) {
        super(itemView);
        mIv_icon_app = itemView.findViewById(R.id.iv_icon_app);
        mTv_name_app = itemView.findViewById(R.id.tv_name_app);
        mTv_path_app = itemView.findViewById(R.id.tv_path_app);
        itemView.findViewById(R.id.tv_size_app).setVisibility(View.GONE);
        mCb_clear_process_app = itemView.findViewById(R.id.cb_clear_process_app);
        mCb_clear_process_app.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(AppInfoBean appInfoBean) {
        mIv_icon_app.setImageDrawable(appInfoBean.getIcon());
        mTv_name_app.setText(appInfoBean.getAppName());
        mTv_path_app.setText(appInfoBean.getInstallPath());
        mCb_clear_process_app.setChecked(appInfoBean.isCheck());
    }
}
