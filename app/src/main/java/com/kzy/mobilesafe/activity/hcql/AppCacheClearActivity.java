package com.kzy.mobilesafe.activity.hcql;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.AppCacheAdapter;
import com.kzy.mobilesafe.bean.AppInfoBean;
import com.kzy.mobilesafe.utils.AppInfoUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 缓存清理
 */
public class AppCacheClearActivity extends Activity {

    private ImageView mIv_scan_animation_app_cache;
    private TextView mTv_scanning_app_cache;
    private ProgressBar mPb_scanning_app_cache;
    private RecyclerView mRcv_app_cache;
    private TextView mTv_scan_result_app_cache;

    public static final int STARTSCAN = 1;
    public static final int SCANNING = 2;
    public static final int SCANFINISH = 3;
    private RotateAnimation mAnimation;

    private int progress;
    private PackageManager mPm;
    private StorageStatsManager mSsm;
    private StorageManager mSm;

    private List<AppInfoBean> mAppInfoBeans = new ArrayList<>();
    private AppCacheAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_cache_clear);
        initView();
        initAnimation();
        int myUid = Process.myUid();
        int callingUid = Binder.getCallingUid();
        //获取其他APP的缓存大小需要开启了android.permission.PACKAGE_USAGE_STATS 权限
        boolean granted = AppInfoUtil.isPermissionGranted(this,43);//OP_GET_USAGE_STATS
        if (!granted){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent,100);
        }else {
            startScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==100){
            boolean granted = AppInfoUtil.isPermissionGranted(this,43);//OP_GET_USAGE_STATS
            if (granted){
                startScan();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAnimation() {
        mAnimation = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        mAnimation.setDuration(2000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {

                return 2*x;
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STARTSCAN:
                    mIv_scan_animation_app_cache.startAnimation(mAnimation);
                    progress = 0;
                    mPb_scanning_app_cache.setProgress(progress);
                    mAppInfoBeans.clear();
                    break;
                case SCANNING:
                    AppInfoBean appInfoBean = (AppInfoBean) msg.obj;
                    mTv_scanning_app_cache.setText("正在扫描:"+appInfoBean.getAppName());
                    mPb_scanning_app_cache.setProgress(progress);
                    mAppInfoBeans.add(0,appInfoBean);
                    mAdapter.setData(mAppInfoBeans);
                    break;
                case SCANFINISH:
                    mTv_scanning_app_cache.setText("扫描完成");
                    mIv_scan_animation_app_cache.clearAnimation();
                    break;

            }
        }
    };
    private void startScan() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                List<AppInfoBean> allAppsInfos = AppInfoUtil.getAllAppsInfos(AppCacheClearActivity.this);
                mHandler.obtainMessage(STARTSCAN).sendToTarget();
                int count = 0;
                for (AppInfoBean appInfoBean:allAppsInfos){
                    count++;
                    progress = Math.round( count*100.0f/allAppsInfos.size());
                    getAppCacheSize(appInfoBean);
                    Message message = mHandler.obtainMessage();
                    message.what = SCANNING;
                    message.obj = appInfoBean;
                    message.sendToTarget();
                    SystemClock.sleep(200);
                }
                mHandler.obtainMessage(SCANFINISH).sendToTarget();
            }
        }.start();
    }

    private void getAppCacheSize(AppInfoBean appInfoBean) {
        //android.permission.PACKAGE_USAGE_STATS 权限
        try {

            if (mPm==null||mSsm==null||mSm==null){
                mPm = getPackageManager();
                mSsm = (StorageStatsManager) getSystemService(STORAGE_STATS_SERVICE);
                mSm = (StorageManager) getSystemService(STORAGE_SERVICE);
            }

            UUID uuid = StorageManager.UUID_DEFAULT;//mSm.getUuidForPath(getCacheDir());

            //通过包名获取uid
            int uid = getUid(appInfoBean.getPackageName());

            StorageStats storageStats = mSsm.queryStatsForUid(uuid, uid);

            long cacheBytes = storageStats.getCacheBytes();//缓存
            long dataBytes = storageStats.getDataBytes();//数据
            long appBytes = storageStats.getAppBytes();//应用

            Log.d("cacheBytes",""+cacheBytes);

            appInfoBean.setCacheSize(cacheBytes);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tagtag",e.getMessage());
        }
        catch (NoClassDefFoundError error){
            //不应该被捕获的严重错误。
            Log.e("tagtag",error.getMessage());
            //NoClassDefFoundError: Failed resolution of: Landroid/app/usage/StorageStatsManager;
        }

    }

    private int getUid(String packageName) {
        try {
            ApplicationInfo applicationInfo = mPm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private boolean isGrantPermissed(String permission,String op) {
        boolean granted = false;
        AppOpsManager opsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsManager.checkOpNoThrow(op, Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            granted = mode == AppOpsManager.MODE_ALLOWED;
        }
        return granted;
    }

    private void initView() {
        mIv_scan_animation_app_cache = findViewById(R.id.iv_scan_animation_app_cache);
        mTv_scanning_app_cache = findViewById(R.id.tv_scanning_app_cache);
        mPb_scanning_app_cache = findViewById(R.id.pb_scanning_app_cache);
        mRcv_app_cache = findViewById(R.id.rcv_app_cache);
        mTv_scan_result_app_cache = findViewById(R.id.tv_scan_result_app_cache);
        mRcv_app_cache.setLayoutManager(new LinearLayoutManager(this));
        mRcv_app_cache.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mAdapter = new AppCacheAdapter(this);
        mRcv_app_cache.setAdapter(mAdapter);
    }
}
