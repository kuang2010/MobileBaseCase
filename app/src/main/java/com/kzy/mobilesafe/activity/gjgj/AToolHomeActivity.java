package com.kzy.mobilesafe.activity.gjgj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.db.TelAddressDao;
import com.kzy.mobilesafe.view.ToggleView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
    }

    private void initData() {

        String location = TelAddressDao.getLocation("07467375387");

        Log.d("location","location:"+location);

    }

    private void initView() {
        mTgvQueryAddressAtool = findViewById(R.id.tgv_query_address_atool);
        mTgvQueryServicePhoneAtool = findViewById(R.id.tgv_query_service_phone_atool);
        mTgvSmsBackupAtool = findViewById(R.id.tgv_sms_backup_atool);
        mTgvSmsRestoreAtool = findViewById(R.id.tgv_sms_restore_atool);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.tgv_query_address_atool){
            Intent intent_addr = new Intent(this,TelepAddressQueryActivity.class);
            startActivity(intent_addr);
        }else if (id == R.id.tgv_query_service_phone_atool){
            Intent intent_addr = new Intent(this,ServicePhoneQueryActivity.class);
            startActivity(intent_addr);
        }else if (id == R.id.tgv_sms_backup_atool){
            //短信的备份
            File file = new File(getFilesDir(),"sms.json");
            try {
                PrintWriter writer = new PrintWriter(file);

                writer.print("{sms:[{'body':'你好啊','number':'13485865895'},{'body':'哈哈哈囍','number':'13485895685'}]}");

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
                Gson gson = new Gson();
                SmsBean smsBean = gson.fromJson(json, SmsBean.class);
                Log.d("tagtag",""+smsBean.toString());
            }catch (Exception e){
                e.printStackTrace();
            }

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
