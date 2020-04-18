package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.InstallAppBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/17
 * time: 10:15
 * desc:
 */
public class AppManagerAdapter extends BaseAdapter {

    private Context mContext;
    private List<InstallAppBean> mInstallSystemAppBeans;
    private List<InstallAppBean> mInstallUserAppBeans;
    public AppManagerAdapter(Context context){
        mContext = context;
    }
    public void setSystemAppBeans(List<InstallAppBean> installSystemAppBeans){
        mInstallSystemAppBeans = installSystemAppBeans;
        notifyDataSetChanged();
    }
    public void setUserAppBeans(List<InstallAppBean> installUserAppBeans){
        mInstallUserAppBeans = installUserAppBeans;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mInstallSystemAppBeans!=null && mInstallUserAppBeans!=null){
            return mInstallSystemAppBeans.size()+mInstallUserAppBeans.size()+2;
        }else if (mInstallSystemAppBeans==null||mInstallSystemAppBeans.size()==0){
            if (mInstallUserAppBeans!=null && mInstallUserAppBeans.size()>0){
                return mInstallUserAppBeans.size()+2;
            }
        }else if (mInstallUserAppBeans==null||mInstallUserAppBeans.size()==0){
            if (mInstallSystemAppBeans!=null && mInstallSystemAppBeans.size()>0){
                return mInstallSystemAppBeans.size()+2;
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position==0){
            //系统软件标签
            TextView tv_system = new TextView(mContext);
            tv_system.setText("系统软件("+mInstallSystemAppBeans.size()+")");
            tv_system.setTextSize(20);
            tv_system.setBackgroundColor(Color.GRAY);
            return tv_system;
        }else if (position==mInstallSystemAppBeans.size()+1){
            //应用软件标签
            TextView tv_user = new TextView(mContext);
            tv_user.setText("应用软件("+mInstallUserAppBeans.size()+")");
            tv_user.setTextSize(20);
            tv_user.setBackgroundColor(Color.GRAY);
            return tv_user;
        }else {

            if (convertView==null||convertView instanceof TextView){
                convertView = View.inflate(mContext, R.layout.item_app_manager,null);
            }
            ImageView iv_icon_app = convertView.findViewById(R.id.iv_icon_app);
            TextView tv_name_app = convertView.findViewById(R.id.tv_name_app);
            TextView tv_path_app = convertView.findViewById(R.id.tv_path_app);
            TextView tv_size_app = convertView.findViewById(R.id.tv_size_app);
            InstallAppBean installAppBean;
            if (position<=mInstallSystemAppBeans.size()){
                installAppBean = mInstallSystemAppBeans.get(position-1);
            }else {
                installAppBean = mInstallUserAppBeans.get(position-mInstallSystemAppBeans.size()-2);
            }
            iv_icon_app.setImageDrawable(installAppBean.getIcon());
            tv_name_app.setText(installAppBean.getAppName());
            tv_path_app.setText(installAppBean.getInstallPath());
            tv_size_app.setText(installAppBean.getMemSize());
            return convertView;
        }
    }
}
