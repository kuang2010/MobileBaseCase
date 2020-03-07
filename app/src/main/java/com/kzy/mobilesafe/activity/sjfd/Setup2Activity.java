package com.kzy.mobilesafe.activity.sjfd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.utils.SpUtil;

public class Setup2Activity extends BaseSetupActivity {

    private ImageView mIvLockUnlockSetup2;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setup2;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.btn_pre_setup2).setOnClickListener(this);
        findViewById(R.id.btn_next_setup2).setOnClickListener(this);
        findViewById(R.id.btn_bind_unbind_sim_setup2).setOnClickListener(this);
        mIvLockUnlockSetup2 = findViewById(R.id.iv_lock_unlock_setup2);
    }

    @Override
    protected void initData() {
        super.initData();
        requestPermissions(new String[]{"android.permission.READ_PHONE_STATE"},100 );
        String sim_num = getSimNumFromSp();
        if (TextUtils.isEmpty(sim_num)){
            mIvLockUnlockSetup2.setImageResource(R.mipmap.unlock);
        }else {
            mIvLockUnlockSetup2.setImageResource(R.mipmap.lock);

        }
    }

    private String getSimNumFromSp() {
        return SpUtil.getString(getApplicationContext(), MyConstants.SIM_NUM, null);
    }

    @Override
    protected Class getNextClass() {
        return Setup3Activity.class;
    }

    @Override
    protected Class getPreClass() {
        return Setup1Activity.class;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_pre_setup2){
            goPrePage(getPreClass());
        }else if (v.getId() == R.id.btn_next_setup2){
            goNextPage(getNextClass());
        }else if (v.getId()==R.id.btn_bind_unbind_sim_setup2){
            //判断是否绑定sim
            String sim_num = getSimNumFromSp();
            if (TextUtils.isEmpty(sim_num)){
                //还没绑定SIM，则去绑定
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String simSerialNumber = telephonyManager.getSimSerialNumber();
                if (!TextUtils.isEmpty(simSerialNumber)){
                    SpUtil.putString(getApplicationContext(),MyConstants.SIM_NUM,simSerialNumber);
                    mIvLockUnlockSetup2.setImageResource(R.mipmap.lock);
                }else {
                    Toast.makeText(getApplicationContext(),"没读取手机状态信息的权限",Toast.LENGTH_SHORT).show();
                }
            }else {
                //已绑定，则清除绑定
                SpUtil.putString(getApplicationContext(),MyConstants.SIM_NUM,"");
                mIvLockUnlockSetup2.setImageResource(R.mipmap.unlock);
            }
        }
    }

    @Override
    public void goNextPage(Class nextClass) {
        if (nextClass==null){
            return;
        }
        String sim_num = getSimNumFromSp();
        if (TextUtils.isEmpty(sim_num)){
            Toast.makeText(getApplicationContext(),"请先绑定SIM卡",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this,nextClass);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_entry_fromright_anim,R.anim.activity_exit_toleft_anim);
        finish();
    }
}
