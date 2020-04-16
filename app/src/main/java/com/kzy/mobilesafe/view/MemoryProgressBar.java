package com.kzy.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzy.mobilesafe.R;

/**
 * author: kuangzeyu2019
 * date: 2020/4/15
 * time: 22:39
 * desc:
 */
public class MemoryProgressBar extends RelativeLayout {

    private TextView mTv_available_space;
    private ProgressBar mPb_use_space;

    public MemoryProgressBar(Context context) {
        super(context);
        init(context);
    }

    public MemoryProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        View rootView = View.inflate(context, R.layout.view_memory_pb, this);
        mPb_use_space = rootView.findViewById(R.id.pb_use_space);
        mTv_available_space = rootView.findViewById(R.id.tv_available_space);
    }

    public void setProgress(float progress,float maxProgress){

        mPb_use_space.setProgress((int) (progress/maxProgress * 100));
    }

    public void setText(String text){
        mTv_available_space.setText(text);
    }
}
