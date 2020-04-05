package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.kzy.mobilesafe.bean.ServicePhoneBean;
import com.kzy.mobilesafe.bean.ServicePhoneSecondData;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 14:11
 * desc:
 */
public class MyExpendAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private ServicePhoneBean mServicePhoneBean;

    public MyExpendAdapter(Context context) {

        mContext = context;
    }


    @Override
    public int getGroupCount() {
        if (mServicePhoneBean!=null&&mServicePhoneBean.getFirstLayerDatas().size()>0){
            return mServicePhoneBean.getFirstLayerDatas().size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mServicePhoneBean!=null && mServicePhoneBean.getSecondLayerDatas().get(groupPosition).size()>0){
            return mServicePhoneBean.getSecondLayerDatas().get(groupPosition).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mServicePhoneBean.getFirstLayerDatas().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mServicePhoneBean.getSecondLayerDatas().get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.parseLong(""+groupPosition+""+childPosition);
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        TextView textView = null;

        if (convertView==null){

            textView = new TextView(mContext);

            textView.setTextSize(40);

            textView.setTextColor(Color.parseColor("#ff00ff"));

        }else {

            textView = (TextView) convertView;

        }

        textView.setText(mServicePhoneBean.getFirstLayerDatas().get(groupPosition));

        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        TextView textView = null;

        if (convertView==null){

            textView = new TextView(mContext);

            textView.setTextSize(20);

            textView.setTextColor(Color.parseColor("#333333"));

        }else {

            textView = (TextView) convertView;

        }


        ServicePhoneSecondData servicePhoneSecondData = mServicePhoneBean.getSecondLayerDatas().get(groupPosition).get(childPosition);

        textView.setText(servicePhoneSecondData.getName()+"   "+servicePhoneSecondData.getPhone());

        return textView;
    }




    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    public void setDatas(ServicePhoneBean servicePhoneDatas) {

        mServicePhoneBean = servicePhoneDatas;
    }
}
