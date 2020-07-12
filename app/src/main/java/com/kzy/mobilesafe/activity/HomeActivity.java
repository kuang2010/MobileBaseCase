package com.kzy.mobilesafe.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.AlertDialog;
import android.app.usage.StorageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.bdcs.AntiVirusActivity;
import com.kzy.mobilesafe.activity.gjgj.AToolHomeActivity;
import com.kzy.mobilesafe.activity.hcql.AppCacheClearActivity;
import com.kzy.mobilesafe.activity.jcgl.ProcessTaskManagerAcitivity;
import com.kzy.mobilesafe.activity.rjgj.AppManageActivity;
import com.kzy.mobilesafe.activity.service.FindPhoneService;
import com.kzy.mobilesafe.activity.sjfd.SetupHomeActivity;
import com.kzy.mobilesafe.activity.txws.BlackActivity;
import com.kzy.mobilesafe.activity.txws.BlackLoadMoreActivity;
import com.kzy.mobilesafe.adapter.HomeFunctionAdapter;
import com.kzy.mobilesafe.bean.FuntionBean;
import com.kzy.mobilesafe.utils.AppInfoUtil;
import com.kzy.mobilesafe.utils.Md5Util;
import com.kzy.mobilesafe.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvHeimaHome;
    private Button mBtnSettingHome;
    private GridView mGvFunction;
    private ArrayList<FuntionBean> mFuntionBeans;
    private HomeFunctionAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        initEvent();
        initAnimator();

    }


    private void initAnimator() {
        /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                mIvHeimaHome,
                "rotationY",
                new float[]{0,90,180,270,360});
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();*/
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.rotate_heima);
        animator.setTarget(mIvHeimaHome);
        animator.start();
    }

    private void initEvent() {
        mBtnSettingHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,SettingCenterActivity.class);
                startActivity(intent);
            }
        });

        mGvFunction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://手机防盗
                        toMobileAntitheft();
                        break;
                    case 1://通讯卫士
//                        Intent intent = new Intent(HomeActivity.this,BlackActivity.class);
//                        startActivity(intent);
                        Intent intent = new Intent(HomeActivity.this,BlackLoadMoreActivity.class);
                        startActivity(intent);
                        break;
                    case 7://高级工具
                        Intent intent_tool = new Intent(HomeActivity.this,AToolHomeActivity.class);
                        startActivity(intent_tool);
                        break;
                    case 2://软件管家
                        Intent intent_soft = new Intent(HomeActivity.this, AppManageActivity.class);
                        startActivity(intent_soft);
                        break;
                    case 3:
                        {//进程管理
                            Intent intent_task = new Intent(HomeActivity.this, ProcessTaskManagerAcitivity.class);
                            startActivity(intent_task);
                        }
                        break;
                    case 5://病毒查杀
                        Intent intent_antivirus = new Intent(HomeActivity.this, AntiVirusActivity.class);
                        startActivity(intent_antivirus);
                        break;
                    case 6://缓存清理
                        Intent intent_cache = new Intent(HomeActivity.this, AppCacheClearActivity.class);
                        startActivity(intent_cache);
                        break;

                    case 4://
                        int a = 30/0;
                        break;
                }
            }
        });
    }

    private void toMobileAntitheft() {
        //判断是否设置过密码
        String pwd_save = SpUtil.getString(this, MyConstants.ENTER_ANTITHELF_PWD, null);
        if (TextUtils.isEmpty(pwd_save)){
            //没设置过密码
            showEnterPwdDialog(true);
        }else {
            //设置过密码
            showEnterPwdDialog(false);
        }
    }

    private void showEnterPwdDialog(final boolean isSetPwd) {
        AlertDialog.Builder ab =new AlertDialog.Builder(this);//AlertDialog时APP主题不能用android:Theme.Light.xxx
        View view_dialog = View.inflate(this, R.layout.dialog_setpwd_home, null);
        ab.setView(view_dialog);
        final AlertDialog dialog = ab.create();
        dialog.show();

        TextView tv_title = view_dialog.findViewById(R.id.tv_title_dialog_pwd_home);
        final EditText et_enter_pwd = view_dialog.findViewById(R.id.et_enter_pwd_dialog);
        final EditText et_confirm_pwd_dialog = view_dialog.findViewById(R.id.et_confirm_pwd_dialog);
        Button btn_cancel_pwd_dialog = view_dialog.findViewById(R.id.btn_cancel_pwd_dialog);
        Button btn_confirm_pwd_dialog = view_dialog.findViewById(R.id.btn_confirm_pwd_dialog);

        if (isSetPwd){
            tv_title.setText("请设置密码");
        }else {
            tv_title.setText("请输入密码");
            et_confirm_pwd_dialog.setVisibility(View.GONE);
        }

        btn_cancel_pwd_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_confirm_pwd_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSetPwd){
                    //是设置密码
                    String pwd = et_enter_pwd.getText().toString();
                    String pwd_confirm = et_confirm_pwd_dialog.getText().toString();
                    if (TextUtils.isEmpty(pwd)){
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!pwd.equals(pwd_confirm)){
                        Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SpUtil.putString(getApplicationContext(),MyConstants.ENTER_ANTITHELF_PWD,Md5Util.md5Encode(pwd));
                    Toast.makeText(getApplicationContext(),"密码设置成功",Toast.LENGTH_SHORT).show();
                }else {
                    String pwd = et_enter_pwd.getText().toString();
                    if (TextUtils.isEmpty(pwd)){
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String pwd_save = SpUtil.getString(getApplicationContext(), MyConstants.ENTER_ANTITHELF_PWD, null);
                    if (!Md5Util.md5Encode(pwd).equals(pwd_save)){
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(HomeActivity.this,SetupHomeActivity.class);
                    startActivity(intent);

                }

                dialog.dismiss();
            }
        });

    }

    private void initData() {
        mFuntionBeans = new ArrayList<>();
        mFuntionBeans.add(new FuntionBean(R.mipmap.sjfd,"手机防盗","手机丢失好找"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.srlj,"通讯卫士","拦截骚扰电信"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.rjgj,"软件管家","软件管理中心"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.jcgl,"进程管理","提升手机性能"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.lltj,"流量统计","注意流量超标"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.lltj,"病毒查杀","手机安全保障"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.hcql,"缓存清理","清理垃圾数据"));
        mFuntionBeans.add(new FuntionBean(R.mipmap.cygj,"高级工具","特性处理更好"));
        mAdapter = new HomeFunctionAdapter(this);
        mGvFunction.setAdapter(mAdapter);

        mAdapter.setDatas(mFuntionBeans);


        boolean startService = SpUtil.getBoolean(this, MyConstants.IS_BOOTCOMPLETE_START_SERVICE, false);
        if (startService){
            Intent intent2 = new Intent(this,FindPhoneService.class);
            this.startService(intent2);
        }

    }

    private void initView() {
        mIvHeimaHome = findViewById(R.id.iv_heima_home);
        mBtnSettingHome = findViewById(R.id.btn_setting_home);
        mGvFunction = findViewById(R.id.gv_function);
    }

    @Override
    public void onClick(View v) {

    }
}
