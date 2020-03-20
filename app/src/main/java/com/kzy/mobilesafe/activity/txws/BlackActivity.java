package com.kzy.mobilesafe.activity.txws;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.kzy.mobilesafe.R;

public class BlackActivity extends Activity implements View.OnClickListener {

    private ImageView mIvAddBlack;
    private ImageView mIvBlackUiNoData;
    private ListView mLvBlackUiHaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mIvAddBlack.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initView() {
        mIvAddBlack = findViewById(R.id.iv_add_black);
        mIvBlackUiNoData = findViewById(R.id.iv_black_ui_no_data);
        mLvBlackUiHaveData = findViewById(R.id.lv_black_ui_have_data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_black:
                break;
            default:
                break;
        }
    }
}
