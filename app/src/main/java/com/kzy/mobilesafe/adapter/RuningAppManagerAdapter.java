package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.AppInfoBean;
import com.kzy.mobilesafe.viewhold.BaseViewHold;
import com.kzy.mobilesafe.viewhold.ProcessAppMarkViewHold;
import com.kzy.mobilesafe.viewhold.ProcessAppRuningViewHold;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/4/25
 * time: 17:32
 * desc:
 */
public class RuningAppManagerAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    List<AppInfoBean> mAppInfoBeans = new ArrayList<>();
    private int TYPE_MARK = 0;
    private int TYPE_APP_INFO = 1;

    public RuningAppManagerAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == TYPE_MARK){
            vh = new ProcessAppMarkViewHold(LayoutInflater.from(mContext).inflate(R.layout.view_item_mark_process,viewGroup,false));//attachToRoot false即不将布局itemview添加到recycleView,仅仅是使用root的LinearLayout.LayoutParam，从而可用不用重新创建layoutparam，避免导致布局itemview的最外层布局的一些属性失效
        }else {
            vh = new ProcessAppRuningViewHold(LayoutInflater.from(mContext).inflate(R.layout.item_app_manager,viewGroup,false));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((BaseViewHold) viewHolder).setData(mAppInfoBeans.get(position));
        if (getItemViewType(position)!=TYPE_MARK){
            viewHolder.itemView.setOnClickListener(this);
            viewHolder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mAppInfoBeans.get(position).isMark()){
            return TYPE_MARK;
        }else {
            return TYPE_APP_INFO;
        }

    }

    @Override
    public int getItemCount() {
        return mAppInfoBeans.size();
    }

    public void setDatas(List<AppInfoBean> runningApp) {
        if (runningApp==null || runningApp.size()==0){
            mAppInfoBeans.clear();
            notifyDataSetChanged();
        }else {
            mAppInfoBeans.clear();
            notifyDataSetChanged();
            mAppInfoBeans.addAll(runningApp);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (mOnItemclickListener!=null){
            mOnItemclickListener.onClick(v,position);
        }
    }

    public interface OnItemclickListener{
        void onClick(View view,int position);
    }
    private OnItemclickListener mOnItemclickListener;
    public void setOnItemclickListener(OnItemclickListener onItemclickListener){
        mOnItemclickListener = onItemclickListener;
    }
}
