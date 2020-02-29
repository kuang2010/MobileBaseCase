package com.kzy.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.bean.VersionInfo;
import com.kzy.mobilesafe.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends BaseActivity {

    private ImageView mIvBgSplash;
    private TextView mTvVersionName;
    private TextView mTvVersionCode;
    private static final int GETCODESUCCESS=200;
    private static final int ERROR404=404;
    private static final int ERROR500=500;
    private static final int URLERROR=10089;
    private static final int IOERROR=10090;
    private static final int JSONERROR=10091;
    private int mVersionCode;
    private long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initAnimation();
        initData();
        initEvent();
    }

    private void initAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
        mIvBgSplash.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mStartTime = System.currentTimeMillis();
                checkVersion();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initEvent() {

    }

    private void initData() {
        //获取版本信息
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            mVersionCode = packageInfo.versionCode;
            mTvVersionName.setText(versionName);
            mTvVersionCode.setText(""+ mVersionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETCODESUCCESS:
                    VersionInfo versionInfo = (VersionInfo) msg.obj;
                    int versionCode = versionInfo.getVersionCode();
                    if (versionCode>mVersionCode){
                        //有新版本，提示去下载
                        Toast.makeText(SplashActivity.this,"有新版本，提示去下载",Toast.LENGTH_SHORT).show();
                        showTipUpdateVersion(versionInfo);
                    }else {
                        //goHome
                        goHome();
                    }
                    break;
                case ERROR404:
                    Toast.makeText(SplashActivity.this,"资源不存在",Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                case ERROR500:
                    Toast.makeText(SplashActivity.this,"服务器开小差",Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                case URLERROR:
                    Toast.makeText(SplashActivity.this,"地址错误",Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                case IOERROR:
                    Toast.makeText(SplashActivity.this,"网络出错",Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                case JSONERROR:
                    Toast.makeText(SplashActivity.this,"文件解析异常",Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                default:
                    goHome();
                    break;
            }
        }
    };


    private void showTipUpdateVersion(final VersionInfo versionInfo) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("版本更新提示")
                .setMessage(versionInfo.getDesc())
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downLoadNewApp(versionInfo.getUrl());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goHome();
                    }
                });
        AlertDialog alertDialog = ab.create();
        alertDialog.show();
//        ab.show();

    }

    private void downLoadNewApp(String url) {


    }

    private void checkVersion() {
        //联网获取服务器版本信息
        final String verionUrl = getResources().getString(R.string.check_version_url);
        new Thread(){
            @Override
            public void run() {
                super.run();
                Message message = mHandler.obtainMessage();
                try {
                    //获取版本信息
                    URL url = new URL(verionUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200){
                        InputStream in = conn.getInputStream();
                        String jsonInfo = StreamUtil.convertToString(in);
                        Log.d("tagtag","info:"+jsonInfo);
                        VersionInfo versionInfo = parseJsonInfo(jsonInfo);
                        message.what = GETCODESUCCESS;
                        message.obj = versionInfo;
                    }else {
                        message.what = code;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URLERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = IOERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what = JSONERROR;
                }finally {
                    mHandler.sendMessage(message);
                }

            }
        }.start();

    }

    private VersionInfo parseJsonInfo(String jsonInfo) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonInfo);
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setVersionName(jsonObject.getString("versionName"));
        versionInfo.setVersionCode(jsonObject.getInt("versionCode"));
        versionInfo.setUrl(jsonObject.getString("url"));
        versionInfo.setDesc(jsonObject.getString("desc"));
        return versionInfo;
    }

    private void initView() {
        mIvBgSplash = findViewById(R.id.iv_bg_splash);
        mTvVersionName = findViewById(R.id.tv_version_name);
        mTvVersionCode = findViewById(R.id.tv_version_code);
    }

    private void goHome(){
        long endTime = System.currentTimeMillis();
        long overTime = endTime - mStartTime;
        if (overTime<3000){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000-overTime);
        }else{
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
