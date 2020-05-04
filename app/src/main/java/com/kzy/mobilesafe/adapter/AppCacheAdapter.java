package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
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
 * date: 2020/4/30
 * time: 17:39
 * desc:
 */
public class AppCacheAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AppInfoBean> mAppInfoBeans = new ArrayList<>();

    public AppCacheAdapter(Context context){
        mContext = context;
    }

    public void setData(List<AppInfoBean> appInfoBeans){
        mAppInfoBeans.clear();
        notifyDataSetChanged();
        if (appInfoBeans!=null&&appInfoBeans.size()>0){
            mAppInfoBeans.addAll(appInfoBeans);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHold(LayoutInflater.from(mContext).inflate(R.layout.item_clear_cache,viewGroup,false));
    }

    /*
    * ！！！不要给int position 加上final
    * */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder,  int position) {
        ImageView iv_icon_cache = viewHolder.itemView.findViewById(R.id.iv_icon_cache);
        TextView tv_name_cache = viewHolder.itemView.findViewById(R.id.tv_name_cache);
        TextView tv_size_cache = viewHolder.itemView.findViewById(R.id.tv_size_cache);

        AppInfoBean appInfoBean = mAppInfoBeans.get(position);

        iv_icon_cache.setImageDrawable(appInfoBean.getIcon());
        tv_name_cache.setText(appInfoBean.getAppName());
        tv_size_cache.setText(Formatter.formatFileSize(mContext,appInfoBean.getCacheSize()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onclick(v,viewHolder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAppInfoBeans.size();
    }

    class MyViewHold extends RecyclerView.ViewHolder{

        public MyViewHold(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onclick(View view,int position);
    }
    OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }
}
