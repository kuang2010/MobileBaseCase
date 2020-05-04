package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/5/2
 * time: 19:10
 * desc:
 */
public class AppLockAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<AppInfoBean> mAppInfoBeans = new ArrayList<>();

    public AppLockAdapter(Context context){
        mContext = context;
    }

    public void setDatas(List<AppInfoBean> appInfoBeans) {
        mAppInfoBeans.clear();
        notifyDataSetChanged();
        if (appInfoBeans!=null && appInfoBeans.size()>0){
            mAppInfoBeans.addAll(appInfoBeans);
            notifyDataSetChanged();
        }
    }

    //带有动画效果的删除item
    public void deleteData(int position) {
        mAppInfoBeans.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        MyViewHold myViewHold = new MyViewHold(LayoutInflater.from(mContext).inflate(R.layout.item_app_lock, viewGroup, false));
//        myViewHold.setIsRecyclable(false);

        return myViewHold;
    }

    /*
     * ！！！不要给int position 加上final，否则在处理notifyItemXxx的时候会位置错乱
     * */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,  int position) {
        ((MyViewHold)viewHolder).setData(mAppInfoBeans.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLockClickListener!=null){
                    try {
                        mOnLockClickListener.onLockClick(viewHolder.itemView,mAppInfoBeans.get(viewHolder.getAdapterPosition()),viewHolder.getAdapterPosition());
                    }catch (ArrayIndexOutOfBoundsException e){
                        //避免在动画过程中又点了该条目位置
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mAppInfoBeans.size();
    }


    class MyViewHold extends RecyclerView.ViewHolder{

        private final ImageView mIv_icon_app_lock;
        private final TextView mTv_name_app_lock;
        private final ImageView mIv_is_lock;

        public MyViewHold(@NonNull View itemView) {
            super(itemView);
            mIv_icon_app_lock = itemView.findViewById(R.id.iv_icon_app_lock);
            mTv_name_app_lock = itemView.findViewById(R.id.tv_name_app_lock);
            mIv_is_lock = itemView.findViewById(R.id.iv_is_lock);
        }

        public void setData(AppInfoBean appInfoBean) {
            mIv_icon_app_lock.setImageDrawable(appInfoBean.getIcon());
            mTv_name_app_lock.setText(appInfoBean.getAppName());
            if (appInfoBean.isLock()){
                mIv_is_lock.setImageResource(R.mipmap.unlock);
            }else {
                mIv_is_lock.setImageResource(R.mipmap.lock);
            }
        }
    }

    public interface OnLockClickListener{
        void onLockClick(View view, AppInfoBean mClickAppInfoBean,int position);
    }
    private OnLockClickListener mOnLockClickListener;
    public void setOnLockClickListener(OnLockClickListener onLockClickListener){
        mOnLockClickListener = onLockClickListener;
    }
}
