package com.kzy.mobilesafe.activity.gjgj;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.gjgj.activity.LockActivity;
import com.kzy.mobilesafe.activity.gjgj.activity.ServicePhoneQueryActivity;
import com.kzy.mobilesafe.activity.gjgj.activity.TelepAddressQueryActivity;
import com.kzy.mobilesafe.activity.service.WatchDog2Service;
import com.kzy.mobilesafe.dao.TelAddressDao;
import com.kzy.mobilesafe.db.AppLockDb;
import com.kzy.mobilesafe.utils.AppInfoUtil;
import com.kzy.mobilesafe.utils.EncodeUtils;
import com.kzy.mobilesafe.utils.ServiceUtil;
import com.kzy.mobilesafe.view.ToggleView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

/**
 * 高级工具首页
 */
public class AToolHomeActivity extends Activity implements View.OnClickListener {

    private ToggleView mTgvQueryAddressAtool;
    private ToggleView mTgvQueryServicePhoneAtool;
    private ToggleView mTgvSmsBackupAtool;
    private ToggleView mTgvSmsRestoreAtool;
    private ToggleView mTgv_lock_atool;
    private ToggleView mTgv_watch_dog1_atool;
    private ToggleView mTgv_watch_dog2_atool;

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
        mTgvSmsBackupAtool.setOnClickListener(this);
        mTgvSmsRestoreAtool.setOnClickListener(this);
        mTgv_lock_atool.setOnClickListener(this);
        ToggleView.OnToggleStateChangeListener onToggleStateChangeListener = new ToggleView.OnToggleStateChangeListener() {
            @Override
            public void onToggleStateChange(View view, boolean open) {
                if (view == mTgv_watch_dog1_atool){
                    Toast.makeText(AToolHomeActivity.this,"使用手机辅助功能无障碍",Toast.LENGTH_SHORT).show();
                }else if (view == mTgv_watch_dog2_atool){
                    if (open){
                        if (checkUseStage()){
                            Intent openService = new Intent(AToolHomeActivity.this, WatchDog2Service.class);
                            startService(openService);
                        }else {
                            mTgv_watch_dog2_atool.setToggleState(false);
                        }
                    }else {
                        Intent closeService = new Intent(AToolHomeActivity.this,WatchDog2Service.class);
                        stopService(closeService);
                    }
                }
            }
        };
        mTgv_watch_dog1_atool.setOnToggleStateChangeListener(onToggleStateChangeListener);
        mTgv_watch_dog2_atool.setOnToggleStateChangeListener(onToggleStateChangeListener);
    }

    private boolean checkUseStage() {
        if (!AppInfoUtil.isUsagestatsGranted(this)){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void initData() {

        String location = TelAddressDao.getLocation("07467375387");

        Log.d("location","location:"+location);

        mTgv_watch_dog2_atool.setToggleState(ServiceUtil.isSerivceRunning(this,"com.kzy.mobilesafe.activity.service.WatchDog2Service"));
    }

    private void initView() {
        mTgvQueryAddressAtool = findViewById(R.id.tgv_query_address_atool);
        mTgvQueryServicePhoneAtool = findViewById(R.id.tgv_query_service_phone_atool);
        mTgvSmsBackupAtool = findViewById(R.id.tgv_sms_backup_atool);
        mTgvSmsRestoreAtool = findViewById(R.id.tgv_sms_restore_atool);
        mTgv_lock_atool = findViewById(R.id.tgv_lock_atool);
        mTgv_watch_dog1_atool = findViewById(R.id.tgv_watch_dog1_atool);
        mTgv_watch_dog2_atool = findViewById(R.id.tgv_watch_dog2_atool);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.tgv_query_address_atool){
            Intent intent_addr = new Intent(this, TelepAddressQueryActivity.class);
            startActivity(intent_addr);
        }else if (id == R.id.tgv_query_service_phone_atool){
            Intent intent_addr = new Intent(this, ServicePhoneQueryActivity.class);
            startActivity(intent_addr);
        }else if (id == R.id.tgv_sms_backup_atool){
            //短信的备份
            File file = new File(getFilesDir(),"sms.json");
            try {
                PrintWriter writer = new PrintWriter(file);

                String sms_src = "{sms:[{'body':'你好啊','number':'13485865895'},{'body':'哈哈哈囍','number':'13485895685'}]}";

                String enCodeStr = EncodeUtils.en_de_codeStr(sms_src, MyConstants.ximing);

                writer.print(enCodeStr);

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else if (id == R.id.tgv_sms_restore_atool){
            //短信的还原
            try{
                StringBuilder sb = new StringBuilder();
                File file = new File(getFilesDir(),"sms.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line = reader.readLine();
                sb.append(line);
                if (TextUtils.isEmpty(line)){
                    line = reader.readLine();
                    sb.append(line);
                }
                String json = sb.toString();
                json = EncodeUtils.en_de_codeStr(json,MyConstants.ximing);
                Gson gson = new Gson();
                SmsBean smsBean = gson.fromJson(json, SmsBean.class);
                Log.d("tagtag",""+smsBean.toString());
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if (id == R.id.tgv_lock_atool){
            //程序锁
            Intent intent = new Intent(this, LockActivity.class);
            startActivity(intent);
        }
    }

    class SmsBean{
        private List<Sms> sms;
        class Sms{
            private String body;
            private String number;

            @Override
            public String toString() {
                return "Sms{" +
                        "body='" + body + '\'' +
                        ", number='" + number + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "SmsBean{" +
                    "sms=" + sms +
                    '}';
        }
    }
}
