package com.kzy.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private ImageView mIvBgSplash;
    private TextView mTvVersionName;
    private TextView mTvVersionCode;
    private static final int GETCODESUCCESS=200;
    private static final int DOWNSUCCESS = 202;
    private static final int ERROR404=404;
    private static final int ERROR500=500;
    private static final int URLERROR=10089;
    private static final int IOERROR=10090;
    private static final int JSONERROR=10091;
    private int mVersionCode;
    private long mStartTime;
    private Timer mInstallTimer;
    private static final int FILENOTFOUNDERROR = 10092;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashThemeNoBg);
        setContentView(R.layout.activity_splash);
        initView();
        initAnimation();
        initData();
        initEvent();
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE"},100);
        }

        copyDbToDirectory("address.db");
        copyDbToDirectory("commonnum.db");
        copyDbToDirectory("antivirus.db");
    }

    /**
     * 拷贝assets目录下的数据库文件到私有文件夹下，以便后面程序的访问
     * @param dbName 数据库名称
     */
    private void copyDbToDirectory(final String dbName) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    AssetManager assets = getAssets();
                    InputStream in = assets.open(dbName);
                    File filesDir = getFilesDir();
                    File file = new File(filesDir, dbName);
                    if (file.exists()){
                        return;
                    }
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len=in.read(buf))>0){
                        out.write(buf,0,len);
                        out.flush();
                    }
                    out.close();
                    in.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
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
                case DOWNSUCCESS:
                    String  path = (String) msg.obj;
                    Log.d("tagtag","下载成功:"+path);
                    Toast.makeText(SplashActivity.this,"下载成功:"+path,Toast.LENGTH_SHORT).show();
                    installApk(path);
                    break;
                case GETCODESUCCESS:
                    VersionInfo versionInfo = (VersionInfo) msg.obj;
                    int versionCode = versionInfo.getVersionCode();
                    if (versionCode>mVersionCode){
                        //有新版本，提示去下载
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
                case FILENOTFOUNDERROR:
                    Toast.makeText(SplashActivity.this,"文件找不到或没文件读写权限",Toast.LENGTH_SHORT).show();
                    goHome();
                    break;
                default:
                    goHome();
                    break;
            }
        }
    };


    /**
     * 安装apk
     * @param path 文件路径，不能是应用的私有文件里，否则其他应用不能访问就安装不了了
     */
    private void installApk(String path) {
        /*
            PackageInstallerActivity
           <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="content" />
                <data android:scheme="file" />
                <data android:mimeType="application/vnd.android.package-archive" />
            </intent-filter>
         */
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(this, "com.kzy.mobilesafe.fileProvider", apkFile);//清单文件内容提供者主机名com.kzy.mobilesafe.fileProvider
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
        startInstallTimer();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tagtag","onResume");
        mActivityState=ActivityState.onResume;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tagtag","onPause");
        mActivityState=ActivityState.onPause;
    }

    enum ActivityState{
        onResume,onPause;
    }
    ActivityState mActivityState;

    private void startInstallTimer(){
        mInstallTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //判断当前APP是否在前台
                if (ActivityState.onResume==mActivityState){
                    Log.d("tagtag","currentThread:"+Thread.currentThread().getName());
                    goHome();
                }
            }
        };
        mInstallTimer.schedule(timerTask,1000,200);
    }

    private void stopInstallTimer() {
        if (mInstallTimer != null) {
            mInstallTimer.cancel();
            mInstallTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopInstallTimer();
    }

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

    private void downLoadNewApp(final String path) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                File saveFile = new File(Environment.getExternalStorageDirectory().getPath()+"/new.apk");
                if (saveFile.exists()){
                    saveFile.delete();
                }
                Message message = mHandler.obtainMessage();
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code==200){
                        InputStream in = conn.getInputStream();
                        FileOutputStream out = new FileOutputStream(saveFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len=in.read(buf))>0){
                            out.write(buf,0,len);
                        }
                        out.close();
                        in.close();
                        message.what = DOWNSUCCESS;
                        message.obj = saveFile.getAbsolutePath();
                    }else {
                        message.what = code;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = URLERROR;
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    message.what = FILENOTFOUNDERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = IOERROR;
                }finally {
                    mHandler.sendMessage(message);
                }
            }
        }.start();

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
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                    message.what = FILENOTFOUNDERROR;
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
