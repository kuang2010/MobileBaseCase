package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.BlackBean;
import com.kzy.mobilesafe.db.BlackDao;
import com.kzy.mobilesafe.db.BlackDb;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/3/21
 * time: 14:28
 * desc:
 */
public class BlackAdapter extends BaseAdapter {

    private Context mContext;

    private BlackDao mBlackDao;

    private List<BlackBean> mBlackBeans = new ArrayList<>();

    public BlackAdapter(Context context, BlackDao blackDao) {
        mContext = context;
        mBlackDao = blackDao;
    }

    public void setDatas(List<BlackBean> blackBeans){
        mBlackBeans.clear();
        notifyDataSetChanged();
        if (blackBeans!=null){
            mBlackBeans.addAll(blackBeans);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mBlackBeans.size();
    }

    @Override
    public BlackBean getItem(int position) {
        return mBlackBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_black_phone,null);
            viewHold = new ViewHold();
            viewHold.tv_phone_black = convertView.findViewById(R.id.tv_phone_black);
            viewHold.tv_mode_black = convertView.findViewById(R.id.tv_mode_black);
            viewHold.iv_delete_black = convertView.findViewById(R.id.iv_delete_black);
            viewHold.tv_black_position = convertView.findViewById(R.id.tv_black_position);
            convertView.setTag(viewHold);
        }else {
            viewHold = (ViewHold) convertView.getTag();
        }

        BlackBean blackBean = mBlackBeans.get(position);

        viewHold.tv_phone_black.setText(blackBean.getPhone());

        final int mode = blackBean.getMode();

        if (mode == BlackDb.MODE_ALL){

            viewHold.tv_mode_black.setText("全部拦截");

        }else if (mode == BlackDb.MODE_PHONE){

            viewHold.tv_mode_black.setText("电话拦截");

        }else {

            viewHold.tv_mode_black.setText("短信拦截");
        }

        viewHold.tv_black_position.setText(""+position);

        viewHold.iv_delete_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mBlackBeans.remove(position);
//
//                notifyDataSetChanged();
//
//                mBlackDao.delete(mBlackBeans.get(position).getPhone());//错误，前面remove了，导致mBlackBeans里元素的位置已经发生了变化，结果删掉的不是对应的bean


                /*不能处理分批加载。*/
//                BlackBean blackBean1 = mBlackBeans.get(position);

                /*不要在adapter里处理集合的增删改操作，因为要保持跟源集合一致*/
//                mBlackBeans.remove(blackBean1);

//                notifyDataSetChanged();

//                mBlackDao.delete(blackBean1.getPhone());



                //用接口暴露出去
                if (mOnBlackItemClickListener!=null){
                    mOnBlackItemClickListener.onDelete(position);
                }

            }
        });

        return convertView;
    }

    class ViewHold{

        TextView tv_phone_black;

        TextView tv_mode_black;

        ImageView iv_delete_black;

        TextView tv_black_position;

    }

    public interface OnBlackItemClickListener{
        void onDelete(int position);
    }

    private OnBlackItemClickListener mOnBlackItemClickListener;

    public void setOnBlackItemClickListener(OnBlackItemClickListener onBlackItemClickListener) {
        mOnBlackItemClickListener = onBlackItemClickListener;
    }
}
