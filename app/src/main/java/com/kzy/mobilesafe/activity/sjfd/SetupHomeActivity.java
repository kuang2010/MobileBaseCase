package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.utils.SpUtil;

public class SetupHomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_home);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

    }

    private void initData() {
        //判断防盗设置向导是否完成
        boolean isSetUpFinish = SpUtil.getBoolean(this, MyConstants.FINISH_SETUP, false);
        if (isSetUpFinish){
            //停在当前页面
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

    }
}
