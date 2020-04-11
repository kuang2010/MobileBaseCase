package com.kzy.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.service.BlackInterceptService;
import com.kzy.mobilesafe.activity.service.ShowPhoneLoactionService;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.utils.SpUtil;
import com.kzy.mobilesafe.view.ToggleView;

/**
 * 设置中心
 */
public class SettingCenterActivity extends Activity implements ToggleView.OnToggleStateChangeListener {

    private ToggleView mTgvBlackInterceptSetting;
    private ToggleView mTgvShowLocationSetting;
    private ToggleView mTgvLocationStyleSetting;

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if ("FrameLayout".equals(name)) {
            int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                String attributeName = attrs.getAttributeName(i);
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeName.equals("id")) {
                    int id = Integer.parseInt(attributeValue.substring(1));
                    String idVal = getResources().getResourceName(id);
                    if ("android:id/content".equals(idVal)) {
//                        GrayFramLayout grayFrameLayout = new GrayFramLayout(context, attrs);
//                        grayFrameLayout.setBackgroundDrawable(getWindow().getDecorView().getBackground());
                        //实现黑白布局
                        //return grayFrameLayout;
                    }
                }
            }
        }

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_center);
        initView();
        initData();
        initEvent();

        requestPermissions(new String[]{"android.permission.PROCESS_OUTGOING_CALLS"}, 100);

        View decorView = getWindow().getDecorView();
        Log.d("decorView", "" + decorView);
        boolean b = decorView instanceof FrameLayout;
        Log.d("decorView", "b:" + b);
    }

    private void initEvent() {
        mTgvBlackInterceptSetting.setOnToggleStateChangeListener(this);
        mTgvShowLocationSetting.setOnToggleStateChangeListener(this);
        mTgvLocationStyleSetting.setOnToggleStateChangeListener(this);
    }

    private void initData() {
        mTgvBlackInterceptSetting.setToggleState(ServiceUtil.isSerivceRunning(this, "com.kzy.mobilesafe.activity.service.BlackInterceptService"));
        mTgvShowLocationSetting.setToggleState(ServiceUtil.isSerivceRunning(this, "com.kzy.mobilesafe.activity.service.ShowPhoneLoactionService"));

        int index = SpUtil.getInt(SettingCenterActivity.this, MyConstants.LOCATIONSTYLESELECTPOS, MyConstants.LOCATIONDEFOULTINDX);
        mTgvLocationStyleSetting.setToggleText("归宿的样式(" + LocationStyleDialog.locDesc[index] + ")");
    }

    private void initView() {
        mTgvBlackInterceptSetting = findViewById(R.id.tgv_black_intercept_setting);
        mTgvShowLocationSetting = findViewById(R.id.tgv_show_location_setting);
        mTgvLocationStyleSetting = findViewById(R.id.tgv_location_style_setting);
    }

    @Override
    public void onToggleStateChange(View view, boolean open) {
        int id = view.getId();
        if (id == R.id.tgv_black_intercept_setting) {
            if (open) {
                //开启黑名单拦截服务
                Intent intent = new Intent(SettingCenterActivity.this, BlackInterceptService.class);
                startService(intent);
            } else {
                //关闭黑名单拦截服务
                Intent intent = new Intent(SettingCenterActivity.this, BlackInterceptService.class);
                stopService(intent);
            }
        } else if (id == R.id.tgv_show_location_setting) {
            if (open) {
                //开启归宿地服务
//                Intent intent = new Intent(SettingCenterActivity.this,ShowPhoneLoactionService.class);
//                startService(intent);
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent, 123);
            } else {
                //关闭归宿地服务
                Intent intent = new Intent(SettingCenterActivity.this, ShowPhoneLoactionService.class);
                stopService(intent);
            }
        } else if (id == R.id.tgv_location_style_setting) {
            //归宿地样式
            LocationStyleDialog dialog = new LocationStyleDialog(this);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    int index = SpUtil.getInt(SettingCenterActivity.this, MyConstants.LOCATIONSTYLESELECTPOS, MyConstants.LOCATIONDEFOULTINDX);
                    mTgvLocationStyleSetting.setToggleText("归宿的样式(" + LocationStyleDialog.locDesc[index] + ")");
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {//开启归宿地服务
            Intent intent = new Intent(SettingCenterActivity.this, ShowPhoneLoactionService.class);
            startService(intent);
        }
    }
}
