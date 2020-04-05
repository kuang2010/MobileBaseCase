package com.kzy.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.service.BlackInterceptService;
import com.kzy.mobilesafe.activity.service.ShowPhoneLoactionService;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.view.ToggleView;

/**
 * 设置中心
 */
public class SettingCenterActivity extends Activity implements ToggleView.OnToggleStateChangeListener {

    private ToggleView mTgvBlackInterceptSetting;
    private ToggleView mTgvShowLocationSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);
        initView();
        initData();
        initEvent();

        requestPermissions(new String[]{"android.permission.PROCESS_OUTGOING_CALLS"},100);

    }

    private void initEvent() {
        mTgvBlackInterceptSetting.setOnToggleStateChangeListener(this);
        mTgvShowLocationSetting.setOnToggleStateChangeListener(this);
    }

    private void initData() {
        mTgvBlackInterceptSetting.setToggleState(ServiceUtil.isSerivceRunning(this,"com.kzy.mobilesafe.activity.service.BlackInterceptService"));
        mTgvShowLocationSetting.setToggleState(ServiceUtil.isSerivceRunning(this,"com.kzy.mobilesafe.activity.service.ShowPhoneLoactionService"));
    }

    private void initView() {
        mTgvBlackInterceptSetting = findViewById(R.id.tgv_black_intercept_setting);
        mTgvShowLocationSetting = findViewById(R.id.tgv_show_location_setting);
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
        }else if (id == R.id.tgv_show_location_setting){
            if (open){
                //开启归宿地服务
                Intent intent = new Intent(SettingCenterActivity.this,ShowPhoneLoactionService.class);
                startService(intent);
            }else {
                //关闭归宿地服务
                Intent intent = new Intent(SettingCenterActivity.this,ShowPhoneLoactionService.class);
                stopService(intent);
            }
        }
    }
}
