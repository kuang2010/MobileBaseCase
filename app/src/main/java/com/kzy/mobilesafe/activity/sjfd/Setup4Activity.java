package com.kzy.mobilesafe.activity.sjfd;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.service.FindPhoneService;
import com.kzy.mobilesafe.receiver.MyDeviceAdminReceiver;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.utils.SpUtil;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox mCbStartAntithelfSetup4;
    private TextView mTvDescFandaoService;
    private DevicePolicyManager mDPM;
    private ComponentName mComponentName;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setup4;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.btn_pre_setup4).setOnClickListener(this);
        findViewById(R.id.btn_complement_setup4).setOnClickListener(this);
        mCbStartAntithelfSetup4 = findViewById(R.id.cb_start_antithelf_setup4);
        mTvDescFandaoService = findViewById(R.id.tv_desc_fandao_service);
    }

    @Override
    protected void initData() {
        super.initData();
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this,MyDeviceAdminReceiver.class);

        boolean serivceRunning = ServiceUtil.isSerivceRunning(this, "com.kzy.mobilesafe.activity.service.FindPhoneService");
        if (serivceRunning){
            mCbStartAntithelfSetup4.setChecked(true);
            mTvDescFandaoService.setText("防盗保护服务已开启");
        }else {
            mCbStartAntithelfSetup4.setChecked(false);
            mTvDescFandaoService.setText("防盗保护服务已关闭");
        }
    }

    @Override
    protected Class getNextClass() {
        return null;
    }

    @Override
    protected Class getPreClass() {
        return Setup3Activity.class;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mCbStartAntithelfSetup4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //判断设备管理器是否激活
                    if (!mDPM.isAdminActive(mComponentName)){
                        //没激活，去激活
                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,getResources().getString(R.string.sample_device_admin_description));
                        startActivityForResult(intent, 100);
                    }else {
                        //激活了。开启服务
                        startFindPhoneService();
                    }

                }else {
                    if (mDPM.isAdminActive(mComponentName)){
                        mDPM.removeActiveAdmin(mComponentName);
                    }
                    Intent intent = new Intent(Setup4Activity.this,FindPhoneService.class);
                    stopService(intent);
                    mTvDescFandaoService.setText("防盗保护服务已关闭");
                    SpUtil.putBoolean(Setup4Activity.this,MyConstants.IS_BOOTCOMPLETE_START_SERVICE,false);

                }
            }
        });
    }

    private void startFindPhoneService() {
        Intent intent = new Intent(this,FindPhoneService.class);
        startService(intent);
        mTvDescFandaoService.setText("防盗保护服务已开启");
        SpUtil.putBoolean(this,MyConstants.IS_BOOTCOMPLETE_START_SERVICE,true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_pre_setup4){
            goPrePage(getPreClass());
        }else if (v.getId() == R.id.btn_complement_setup4){
            if (vaildAgree()){
                requestPermissions(new String[]{"android.permission.RECEIVE_SMS","android.permission.RECEIVE_BOOT_COMPLETED","android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"},125 );
            }
        }
    }

    @Override
    protected boolean vaildAgree() {
        if (!mCbStartAntithelfSetup4.isChecked()){
            Toast.makeText(this,"请先勾选开启防盗保护",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (mDPM.isAdminActive(mComponentName)){
                startFindPhoneService();
            }else {
                mCbStartAntithelfSetup4.setChecked(false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 125){
            if (grantResults[0] == 0 && grantResults[1] == 0 && grantResults[3] == 0){
                SpUtil.putBoolean(this,MyConstants.FINISH_SETUP,true);
                Intent intent = new Intent(this,SetupHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
