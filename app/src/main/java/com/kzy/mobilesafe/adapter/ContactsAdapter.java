package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.ContactorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/3/12
 * time: 23:06
 * desc:
 */
public class ContactsAdapter extends BaseAdapter {

    private Context mContext;

    List<ContactorBean> mContactorBeans = new ArrayList<>();

    public ContactsAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ContactorBean> contactorBeans) {
        mContactorBeans.clear();
        notifyDataSetChanged();
        if (contactorBeans!=null){
            mContactorBeans.addAll(contactorBeans);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mContactorBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactorBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contactor,null);
            viewHold = new ViewHold();
            convertView.setTag(viewHold);

            viewHold.tv_name_contact = convertView.findViewById(R.id.tv_name_contact);
            viewHold.tv_phone_contact = convertView.findViewById(R.id.tv_phone_contact);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        ContactorBean contactorBean = (ContactorBean) getItem(position);

        viewHold.tv_name_contact.setText(contactorBean.getName());
        viewHold.tv_phone_contact.setText(contactorBean.getPhone());

        return convertView;
    }


    class ViewHold{
        TextView tv_name_contact;
        TextView tv_phone_contact;
    }
}
