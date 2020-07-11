package com.kzy.mobilesafe.activity.gjgj.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.service.WatchDog2Service;

/**
 * 程序锁拦截密码输入界面
 * singleInstance
 */
public class LockEntryPassWordActivity extends Activity {

    private ImageView mIv_lock_app_icon;
    private EditText mEt_pwd_lock_app;
    private Button mBtn_entry_lock_app;
    private String mPackageName;
    private HomeBackReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_entry_pass_word);
        initView();
        initData();
        initEvent();
        registerHomeKeyReciver();
    }


    private void initEvent() {
        mBtn_entry_lock_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = mEt_pwd_lock_app.getText().toString();
                if ("123".equals(pwd)){
                    //密码正确，放行
                    WatchDog2Service.WHITEPACKENAM = mPackageName;
                    finish();

                }else {
                    Toast.makeText(LockEntryPassWordActivity.this,"密码不正确",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        mPackageName = intent.getStringExtra(MyConstants.PACKAGENAME);
        PackageManager packageManager = getPackageManager();
        try {
            Drawable applicationIcon = packageManager.getApplicationIcon(mPackageName);
            mIv_lock_app_icon.setImageDrawable(applicationIcon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mIv_lock_app_icon = findViewById(R.id.iv_lock_app_icon);
        mEt_pwd_lock_app = findViewById(R.id.et_pwd_lock_app);
        mBtn_entry_lock_app = findViewById(R.id.btn_entry_lock_app);
    }


    //处理返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            //返回按钮，回到桌面
            backHome();
        }
        return super.onKeyDown(keyCode, event);
    }

    //处理home按钮
    private void registerHomeKeyReciver() {
        mReceiver = new HomeBackReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mReceiver,filter);

    }
    class HomeBackReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                Toast.makeText(context,"按了Home键",Toast.LENGTH_SHORT).show();
                //关闭自己
                finish();
            }
        }
    }

    private void backHome() {
        // 手机的主界面
		/*
		 * <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.MONKEY"/>
            </intent-filter>
		 */
        Intent main = new Intent("android.intent.action.MAIN");
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addCategory("android.intent.category.HOME");
        main.addCategory("android.intent.category.DEFAULT");
        main.addCategory("android.intent.category.MONKEY");
        startActivity(main);//进入手机主界面

        //关闭自己
        finish();
    }


    @Override
    protected void onDestroy() {
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }
}
