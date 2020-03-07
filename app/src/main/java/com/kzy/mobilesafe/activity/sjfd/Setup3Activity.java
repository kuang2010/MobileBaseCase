package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.kzy.mobilesafe.R;

public class Setup3Activity extends BaseSetupActivity {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setup3;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.btn_pre_setup3).setOnClickListener(this);
        findViewById(R.id.btn_next_setup3).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected Class getNextClass() {
        return Setup4Activity.class;
    }

    @Override
    protected Class getPreClass() {
        return Setup2Activity.class;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_pre_setup3){
            goPrePage(getPreClass());
        }else if (v.getId() == R.id.btn_next_setup3){
            goNextPage(getNextClass());
        }
    }
}
