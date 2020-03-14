package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.service.FindPhoneService;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.utils.SpUtil;

public class SetupHomeActivity extends Activity implements View.OnClickListener {

    private TextView mTvSafePhoneNumHome;
    private ImageView mIvProtectIsOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_home);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.tv_to_setup_home).setOnClickListener(this);
    }

    private void initData() {
        //判断防盗设置向导是否完成
        boolean isSetUpFinish = SpUtil.getBoolean(this, MyConstants.FINISH_SETUP, false);
        if (isSetUpFinish){
            //停在当前页面

            String safeNum = SpUtil.getString(this, MyConstants.SAFEPHONE, "");
            mTvSafePhoneNumHome.setText(safeNum);

            boolean serivceRunning = ServiceUtil.isSerivceRunning(this, "com.kzy.mobilesafe.activity.service.FindPhoneService");
            if (serivceRunning){
                mIvProtectIsOpen.setImageResource(R.mipmap.lock);
            }else {
                mIvProtectIsOpen.setImageResource(R.mipmap.unlock);
            }

        }else {
            //进入向导页面
            enterGuideSetUpActivity();
        }
    }

    private void enterGuideSetUpActivity() {
        Intent intent = new Intent(this,Setup1Activity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        mTvSafePhoneNumHome = findViewById(R.id.tv_safe_phone_num_home);
        mIvProtectIsOpen = findViewById(R.id.iv_protect_is_open);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_to_setup_home){
            //进入向导设置页面
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();

        }
    }
}
