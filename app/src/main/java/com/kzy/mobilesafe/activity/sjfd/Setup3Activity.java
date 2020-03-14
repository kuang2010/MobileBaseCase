package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.utils.SpUtil;

public class Setup3Activity extends BaseSetupActivity {

    private EditText mEtInputPhoneSetup3;
    private Button mBtnSelectPhoneSetup3;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setup3;
    }

    @Override
    protected void initView() {
        super.initView();
        findViewById(R.id.btn_pre_setup3).setOnClickListener(this);
        findViewById(R.id.btn_next_setup3).setOnClickListener(this);
        mEtInputPhoneSetup3 = findViewById(R.id.et_input_phone_setup3);
        mBtnSelectPhoneSetup3 = findViewById(R.id.btn_select_phone_setup3);

    }

    @Override
    protected void initData() {
        super.initData();
        String safePhone = SpUtil.getString(this, MyConstants.SAFEPHONE, null);
        if (!TextUtils.isEmpty(safePhone)){
            mEtInputPhoneSetup3.setText(safePhone);
            mEtInputPhoneSetup3.setSelection(safePhone.length());
        }

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
        mBtnSelectPhoneSetup3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_pre_setup3){
            goPrePage(getPreClass());
        }else if (v.getId() == R.id.btn_next_setup3){
            goNextPage(getNextClass());
        }else if (v.getId() == R.id.btn_select_phone_setup3){
            //获取通讯录里的电话号码
            //访问通讯录数据库
            requestPermissions(new String[]{"android.permission.READ_CONTACTS","android.permission.SEND_SMS"},10);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==10){
            if (permissions[0].equals("android.permission.READ_CONTACTS")){
                if (grantResults[0] == 0){
                    Intent intent = new Intent(Setup3Activity.this,ContactListActivity.class);
                    startActivityForResult(intent,123);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123){
            if (resultCode==RESULT_OK && data!=null){
                String name = data.getStringExtra(MyConstants.CONTACTERNAME);
                String phone = data.getStringExtra(MyConstants.CONTACTERPHONE);
                if (phone.contains(",")){
                    String[] split = phone.split(",");
                    mEtInputPhoneSetup3.setText(split[0]);
                }else {
                    mEtInputPhoneSetup3.setText(phone);
                }

                mEtInputPhoneSetup3.setSelection(mEtInputPhoneSetup3.getText().toString().length());

            }
        }
    }

    @Override
    protected boolean vaildAgree() {
        if (mEtInputPhoneSetup3.getText().toString().length()==0){
            Toast.makeText(this,"请填写安全号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.vaildAgree();
    }

    @Override
    public void goNextPage(Class nextClass) {
        //保存安全号码
        SpUtil.putString(this,MyConstants.SAFEPHONE,mEtInputPhoneSetup3.getText().toString());
        super.goNextPage(nextClass);
    }
}
