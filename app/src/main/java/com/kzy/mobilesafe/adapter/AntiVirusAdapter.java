package com.kzy.mobilesafe.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * time: 8:51
 * desc:
 */
public class AntiVirusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AppInfoBean> mScanAppInfo = new ArrayList<>();

    public void setData(List<AppInfoBean> scanAppInfo){
        mScanAppInfo.clear();
        notifyDataSetChanged();
        if (scanAppInfo!=null&&scanAppInfo.size()>0){
            mScanAppInfo.addAll(scanAppInfo);
            notifyDataSetChanged();
        }

    }

    public AntiVirusAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHold(LayoutInflater.from(mContext).inflate(R.layout.item_antivirus,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ImageView iv_icon_antivirus = viewHolder.itemView.findViewById(R.id.iv_icon_antivirus);
        TextView tv_name_antivirus = viewHolder.itemView.findViewById(R.id.tv_name_antivirus);
        ImageView iv_is_virus = viewHolder.itemView.findViewById(R.id.iv_is_virus);

        iv_icon_antivirus.setImageDrawable(mScanAppInfo.get(position).getIcon());
        tv_name_antivirus.setText(mScanAppInfo.get(position).getAppName());
        iv_is_virus.setImageResource(mScanAppInfo.get(position).isVirus()?R.mipmap.check_status_red:R.mipmap.check_status_green);
        Log.d("AntiVirusAdapter","onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return mScanAppInfo.size();
    }

    class MyViewHold extends RecyclerView.ViewHolder{

        public MyViewHold(@NonNull View itemView) {
            super(itemView);
            Log.d("AntiVirusAdapter","MyViewHold");
        }
    }
}
