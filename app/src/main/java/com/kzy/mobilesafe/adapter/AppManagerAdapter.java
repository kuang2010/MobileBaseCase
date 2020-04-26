package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.AppInfoBean;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/17
 * time: 10:15
 * desc:
 */
public class AppManagerAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppInfoBean> mInstallSystemAppBeans;
    private List<AppInfoBean> mInstallUserAppBeans;
    private View.OnClickListener onItemClickListener;
    public AppManagerAdapter(Context context){
        mContext = context;
    }
    public void setSystemAppBeans(List<AppInfoBean> installSystemAppBeans){
        mInstallSystemAppBeans = installSystemAppBeans;
        notifyDataSetChanged();
    }
    public void setUserAppBeans(List<AppInfoBean> installUserAppBeans){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position==0){
            //系统软件标签
            TextView tv_system = new TextView(mContext);
            tv_system.setText("系统软件("+mInstallSystemAppBeans.size()+")");
            tv_system.setTextSize(20);
            tv_system.setBackgroundColor(Color.GRAY);
            return tv_system;//为啥不用设置layoutParam,也能填充宽度??? listview添加itemview使用的layoutparam是ViewGroup.layoutparam
        }else if (position==mInstallSystemAppBeans.size()+1){
            //应用软件标签
            TextView tv_user = new TextView(mContext);
            tv_user.setText("应用软件("+mInstallUserAppBeans.size()+")");
            tv_user.setTextSize(20);
            tv_user.setBackgroundColor(Color.GRAY);
            return tv_user;//为啥不用设置layoutParam,也能填充宽度??? listview添加itemview使用的layoutparam是ViewGroup.layoutparam
        }else {

            if (convertView==null||convertView instanceof TextView || position==mInstallSystemAppBeans.size()+2){//position==mInstallSystemAppBeans.size()+2使该位置能被点击
                convertView = View.inflate(mContext, R.layout.item_app_manager,null);//root 为null，布局的最外层属性会部分失效，如layout_width="200dp"。即使有root,由于listview使用的是ViewGroup的LayoutParam来添加item，所以最外层的marginLeft等属性也会失效
            }
            ImageView iv_icon_app = convertView.findViewById(R.id.iv_icon_app);
            TextView tv_name_app = convertView.findViewById(R.id.tv_name_app);
            TextView tv_path_app = convertView.findViewById(R.id.tv_path_app);
            TextView tv_size_app = convertView.findViewById(R.id.tv_size_app);
            AppInfoBean appInfoBean;
            if (position<=mInstallSystemAppBeans.size()){
                appInfoBean = mInstallSystemAppBeans.get(position-1);
            }else {
                appInfoBean = mInstallUserAppBeans.get(position-mInstallSystemAppBeans.size()-2);
            }
            iv_icon_app.setImageDrawable(appInfoBean.getIcon());
            tv_name_app.setText(appInfoBean.getAppName());
            tv_path_app.setText(appInfoBean.getInstallPath());
            tv_size_app.setText(appInfoBean.getMemSize());
            return convertView;
        }
    }

}
