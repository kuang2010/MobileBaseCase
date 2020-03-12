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
import com.kzy.mobilesafe.activity.HomeActivity;
import com.kzy.mobilesafe.bean.FuntionBean;

import java.util.ArrayList;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 16:21
 * desc:
 */
public class HomeFunctionAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<FuntionBean> mFuntionBeans = new ArrayList<>();
    public HomeFunctionAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(ArrayList<FuntionBean> funtionBeans){
        mFuntionBeans.clear();
        notifyDataSetChanged();
        if (funtionBeans!=null){
            mFuntionBeans.addAll(funtionBeans);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mFuntionBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mFuntionBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView==null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_function_home,null);
        }else {
            view = convertView;
        }

        ImageView iv_icon_funtion_item = view.findViewById(R.id.iv_icon_funtion_item);
        TextView tv_title_item_home = view.findViewById(R.id.tv_title_item_home);
        TextView tv_desc_item_home = view.findViewById(R.id.tv_desc_item_home);

        FuntionBean funtionBean = mFuntionBeans.get(position);
        iv_icon_funtion_item.setImageResource(funtionBean.getIcon());
        tv_desc_item_home.setText(funtionBean.getDesc());
        tv_title_item_home.setText(funtionBean.getTitle());
        return view;
    }
}
