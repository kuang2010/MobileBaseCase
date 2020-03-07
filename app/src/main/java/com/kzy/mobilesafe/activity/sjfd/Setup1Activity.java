package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.kzy.mobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setup1;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.btn_next_setup1).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected Class getNextClass() {
        return Setup2Activity.class;
    }

    @Override
    protected Class getPreClass() {
        return null;
    }

    @Override
    protected void initEvent() {
        super.initEvent();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.btn_next_setup1){
            goNextPage(getNextClass());
        }
    }

}
