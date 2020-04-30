package com.kzy.mobilesafe.activity.gjgj;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.dao.TelAddressDao;

/**
 * 电话归属地查询
 */
public class TelepAddressQueryActivity extends Activity implements View.OnClickListener {

    private EditText mEtPhoneNumAddrTool;
    private Button mBtnQueryAddrTool;
    private TextView mTvTelAddressTool;
    private Vibrator mVibrator;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telep_address_query);

        initView();
        initData();
        initEvent();

    }

    @Nullable
    @Override
    public View onCreateView(String name,  Context context,  AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void initEvent() {
        mBtnQueryAddrTool.setOnClickListener(this);
    }

    private void initData() {

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

    }

    private void initView() {
        FrameLayout content = findViewById(android.R.id.content);

        mEtPhoneNumAddrTool = findViewById(R.id.et_phone_num_addr_tool);
        mBtnQueryAddrTool = content.findViewById(R.id.btn_query_addr_tool);
        mTvTelAddressTool = findViewById(R.id.tv_tel_address_tool);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_query_addr_tool){
            queryAddr();
        }
    }

    /**
     * 查询归属地数据库，最好是子线程
     */
    private void queryAddr() {
        String phoneNum = mEtPhoneNumAddrTool.getText().toString();
        if (TextUtils.isEmpty(phoneNum)){
            zhengdong();
            return;
        }
        String location = TelAddressDao.getLocation(phoneNum);
        mTvTelAddressTool.setText(location);

    }

    /**
     * 震动和抖动动画
     */
    private void zhengdong() {

        mVibrator.vibrate(new long[] { 300, 100, 200, 300, 200, 200, 400,
                100, 500, 200 }, -1);//震动300ms，停100ms，震动200ms,停300ms...不重复

        mEtPhoneNumAddrTool.startAnimation(mAnimation);
    }
}
