package com.kzy.mobilesafe.activity.gjgj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.db.TelAddressDao;
import com.kzy.mobilesafe.view.ToggleView;

/**
 * 高级工具首页
 */
public class AToolHomeActivity extends Activity implements View.OnClickListener {

    private ToggleView mTgvQueryAddressAtool;
    private ToggleView mTgvQueryServicePhoneAtool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool_home);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mTgvQueryAddressAtool.setOnClickListener(this);
        mTgvQueryServicePhoneAtool.setOnClickListener(this);
    }

    private void initData() {

        String location = TelAddressDao.getLocation("07467375387");

        Log.d("location","location:"+location);

    }

    private void initView() {
        mTgvQueryAddressAtool = findViewById(R.id.tgv_query_address_atool);
        mTgvQueryServicePhoneAtool = findViewById(R.id.tgv_query_service_phone_atool);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.tgv_query_address_atool){
            Intent intent_addr = new Intent(this,TelepAddressQueryActivity.class);
            startActivity(intent_addr);
        }
    }
}
