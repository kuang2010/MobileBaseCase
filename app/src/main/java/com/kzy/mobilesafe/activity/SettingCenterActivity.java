package com.kzy.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.service.BlackInterceptService;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.view.ToggleView;

/**
 * 设置中心
 */
public class SettingCenterActivity extends Activity implements ToggleView.OnToggleStateChangeListener {

    private ToggleView mTgvBlackInterceptSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mTgvBlackInterceptSetting.setOnToggleStateChangeListener(this);
    }

    private void initData() {
        mTgvBlackInterceptSetting.setToggleState(ServiceUtil.isSerivceRunning(this,"com.kzy.mobilesafe.activity.service.BlackInterceptService"));
    }

    private void initView() {
        mTgvBlackInterceptSetting = findViewById(R.id.tgv_black_intercept_setting);
    }

    @Override
    public void onToggleStateChange(View view, boolean open) {
        int id = view.getId();
        if (id == R.id.tgv_black_intercept_setting){
            if (open){
                //开启黑名单拦截服务
                Intent intent = new Intent(SettingCenterActivity.this,BlackInterceptService.class);
                startService(intent);
            }else {
                //关闭黑名单拦截服务
                Intent intent = new Intent(SettingCenterActivity.this,BlackInterceptService.class);
                stopService(intent);
            }
        }
    }
}
