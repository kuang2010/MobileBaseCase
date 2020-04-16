package com.kzy.mobilesafe.activity.rjgj;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.utils.PhoneUtil;
import com.kzy.mobilesafe.view.MemoryProgressBar;

public class AppManageActivity extends Activity {

    private MemoryProgressBar mPb_memory_phone;
    private MemoryProgressBar mPb_memory_sdcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {

    }

    private void initData() {
        long totalPhoneMemory = PhoneUtil.getTotalPhoneMemory();
        long freePhoneMemory = PhoneUtil.getFreePhoneMemory();
        long totalSdcardMemory = PhoneUtil.getTotalSdcardMemory();
        long freeSdcardMemory = PhoneUtil.getFreeSdcardMemory();
        mPb_memory_phone.setProgress(totalPhoneMemory-freePhoneMemory,totalPhoneMemory);
        mPb_memory_phone.setText("手机可用内存:"+PhoneUtil.formatFileSize(this,freePhoneMemory));
        mPb_memory_sdcard.setProgress(totalSdcardMemory-freeSdcardMemory,totalSdcardMemory);
        mPb_memory_sdcard.setText("sd卡可用内存:"+PhoneUtil.formatFileSize(this,freeSdcardMemory));

    }

    private void initView() {
        mPb_memory_phone = findViewById(R.id.pb_memory_phone);
        mPb_memory_sdcard = findViewById(R.id.pb_memory_sdcard);
    }
}
