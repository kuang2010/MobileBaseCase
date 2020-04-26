package com.kzy.mobilesafe.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kzy.mobilesafe.R;

/**
 * author: kuangzeyu2019
 * date: 2020/3/24
 * time: 22:14
 * desc: 自定义控件的布局：抽取组合控件里相同的那一部分UI作为该自定义控件的布局 ， 可变的样式可抽取属性来完成
 */
public class ToggleView extends RelativeLayout {

    private TextView mTvDescFunctionToggleView;
    private ImageView mIvSwitchToggleView;
    private View mRootView;
    private boolean toggleStateIsOpen;

    public ToggleView(Context context) {
        super(context);
        init(null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        initView();
        initData(attrs);
        initEvent();
    }

    private void initEvent() {
        mRootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStateIsOpen = !toggleStateIsOpen;
                if (toggleStateIsOpen){
                    mIvSwitchToggleView.setImageResource(R.drawable.on);
                }else {
                    mIvSwitchToggleView.setImageResource(R.drawable.off);
                }

                if (mOnToggleStateChangeListener!=null){
                    mOnToggleStateChangeListener.onToggleStateChange(ToggleView.this,toggleStateIsOpen);
                }

            }
        });

    }

    private void initData(AttributeSet attrs) {

        String text = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "text");
        String  textColor = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "textColor");
        String bgselector = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "bgselector");
        boolean  togglevisible = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res-auto", "togglevisible",true);

        if (togglevisible){
            mIvSwitchToggleView.setVisibility(VISIBLE);
        }else {
            mIvSwitchToggleView.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(text)){
            mTvDescFunctionToggleView.setText(text);
        }

        if (!TextUtils.isEmpty(textColor)&&textColor.startsWith("#")){
            int c = Color.parseColor(textColor);
            mTvDescFunctionToggleView.setTextColor(c);
        }

        if (!TextUtils.isEmpty(bgselector)){
            int bg = Integer.parseInt(bgselector);
            switch (bg){
                case 1: //first
                    mRootView.setBackgroundResource(R.drawable.selector_toggle_first);
                    break;
                case 2://middle
                    mRootView.setBackgroundResource(R.drawable.selector_toggle_middle);
                    break;
                case 3://last
                    mRootView.setBackgroundResource(R.drawable.selector_toggle_last);
                    break;
            }
        }

    }


    private void initView() {

        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.view_toggle_setting, this);

        mTvDescFunctionToggleView = mRootView.findViewById(R.id.tv_desc_function_toggle_view);

        mIvSwitchToggleView = mRootView.findViewById(R.id.iv_switch_toggle_view);

    }


    public void setToggleState(boolean open){
        toggleStateIsOpen = open;
        if (toggleStateIsOpen){
            mIvSwitchToggleView.setImageResource(R.drawable.on);
        }else {
            mIvSwitchToggleView.setImageResource(R.drawable.off);
        }
    }

    public void setToggleText(String text) {
        mTvDescFunctionToggleView.setText(text);
    }


    public interface OnToggleStateChangeListener{
        void onToggleStateChange(View view,boolean open);
    }

    private OnToggleStateChangeListener mOnToggleStateChangeListener;

    public void setOnToggleStateChangeListener(OnToggleStateChangeListener onToggleStateChangeListener){
        mOnToggleStateChangeListener = onToggleStateChangeListener;
    }
}
