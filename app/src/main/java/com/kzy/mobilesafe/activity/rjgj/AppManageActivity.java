package com.kzy.mobilesafe.activity.rjgj;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.AppManagerAdapter;
import com.kzy.mobilesafe.bean.AppInfoBean;
import com.kzy.mobilesafe.utils.AppInfoUtil;
import com.kzy.mobilesafe.utils.DensityUtil;
import com.kzy.mobilesafe.utils.PhoneUtil;
import com.kzy.mobilesafe.view.MemoryProgressBar;

import java.util.ArrayList;
import java.util.List;


public class AppManageActivity extends Activity implements View.OnClickListener {

    private static final int LOADING = 1;
    private static final int LOADFINISH = 2;
    private MemoryProgressBar mPb_memory_phone;
    private MemoryProgressBar mPb_memory_sdcard;
    private ListView mLv_app_data;
    private View mLayout_load;
    private List<AppInfoBean> mInstallAppsInfo;
    private List<AppInfoBean> mInstallSystemAppsInfo=new ArrayList<>();//系统APP
    private List<AppInfoBean> mInstallUserAppsInfo=new ArrayList<>();//应用APP
    private AppManagerAdapter mManagerAdapter;
    private RelativeLayout mRl_app_data;
    private TextView mTv_ticket;
    private PopupWindow mPopupWindow;
    private AppInfoBean mClickAppInfoBean;
    private AppUnInstallReceiver mAppUnInstallReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage);
        initView();
        initData();
        initEvent();
        initPopuWindow();
        registerReceiver();
    }

    private void registerReceiver() {
        //注册卸载完成的广播接收
        mAppUnInstallReceiver = new AppUnInstallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(mAppUnInstallReceiver,filter);

    }

    //卸载完成的广播接收者
    class AppUnInstallReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //卸载app完成
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAppUnInstallReceiver!=null){
            unregisterReceiver(mAppUnInstallReceiver);
            mAppUnInstallReceiver=null;
        }
        super.onDestroy();
    }

    private void initPopuWindow() {
        View contentView = View.inflate(this, R.layout.view_popwindow_appmanager, null);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);

        contentView.findViewById(R.id.ll_uninstall_appmanager).setOnClickListener(this);
        contentView.findViewById(R.id.ll_launch_appmanager).setOnClickListener(this);
        contentView.findViewById(R.id.ll_share_appmanager).setOnClickListener(this);
        contentView.findViewById(R.id.ll_setting_appmanager).setOnClickListener(this);


    }

    private void initEvent() {
        mLv_app_data.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem<mInstallSystemAppsInfo.size()+1){
                    mTv_ticket.setText("系统软件("+mInstallSystemAppsInfo.size()+")");
                }else{
                    mTv_ticket.setText("应用软件("+mInstallUserAppsInfo.size()+")");
                }
            }
        });

        mLv_app_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0 ||position==mInstallSystemAppsInfo.size()+1){
                    //是标签
                    return;
                }
                mPopupWindow.showAsDropDown(view, DensityUtil.dip2px(AppManageActivity.this,65),-(view.getHeight()));
                if (position<=mInstallSystemAppsInfo.size()){
                    mClickAppInfoBean = mInstallSystemAppsInfo.get(position-1);
                }else {
                    mClickAppInfoBean = mInstallUserAppsInfo.get(position-mInstallSystemAppsInfo.size()-2);
                }

            }
        });
    }

    private void initData() {
        long totalPhoneMemory = PhoneUtil.getTotalPhoneMemory();
        long freePhoneMemory = PhoneUtil.getFreePhoneMemory();
        long totalSdcardMemory = PhoneUtil.getTotalSdcardMemory();
        long freeSdcardMemory = PhoneUtil.getFreeSdcardMemory();
        mPb_memory_phone.setProgress(totalPhoneMemory-freePhoneMemory,totalPhoneMemory);
        mPb_memory_phone.setText("手机可用内存:"+PhoneUtil.formatFileSize(this,freePhoneMemory));
        mPb_memory_sdcard.setProgress(totalSdcardMemory-freeSdcardMemory,totalSdcardMemory);
        mPb_memory_sdcard.setText("sd卡可用内存:"+PhoneUtil.formatFileSize(this,freeSdcardMemory));

        mManagerAdapter = new AppManagerAdapter(this);
        mLv_app_data.setAdapter(mManagerAdapter);

        getInstallAppInfos();
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING:
                    mLayout_load.setVisibility(View.VISIBLE);
                    mLv_app_data.setVisibility(View.GONE);
                    mTv_ticket.setVisibility(View.GONE);
                    break;
                case LOADFINISH:
                    mLayout_load.setVisibility(View.GONE);
                    mLv_app_data.setVisibility(View.VISIBLE);
                    mTv_ticket.setVisibility(View.VISIBLE);
                    mManagerAdapter.setSystemAppBeans(mInstallSystemAppsInfo);
                    mManagerAdapter.setUserAppBeans(mInstallUserAppsInfo);
                    mTv_ticket.setText("系统软件("+mInstallSystemAppsInfo.size()+")");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mRl_app_data.removeView(mTv_ticket);
                    mRl_app_data.addView(mTv_ticket,layoutParams);
                    break;
            }

        }
    };
    private void getInstallAppInfos() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mHandler.obtainMessage(LOADING).sendToTarget();
                mInstallAppsInfo = AppInfoUtil.getAllAppsInfos(getApplicationContext());
                //造假删除自己应用
                mInstallAppsInfo.remove(new AppInfoBean(getPackageName()));
                //分类
                mInstallSystemAppsInfo.clear();
                mInstallUserAppsInfo.clear();
                for (AppInfoBean appInfoBean :mInstallAppsInfo){
                    if (appInfoBean.isSystemApp()){
                        mInstallSystemAppsInfo.add(appInfoBean);
                    }else {
                        mInstallUserAppsInfo.add(appInfoBean);
                    }
                }
                SystemClock.sleep(1000);
                mHandler.obtainMessage(LOADFINISH).sendToTarget();
            }
        }.start();
    }

    private void initView() {
        mPb_memory_phone = findViewById(R.id.pb_memory_phone);
        mPb_memory_sdcard = findViewById(R.id.pb_memory_sdcard);
        mLv_app_data = findViewById(R.id.lv_app_data);
        mLayout_load = findViewById(R.id.layout_load);
        mRl_app_data = findViewById(R.id.rl_app_data);
        mTv_ticket = new TextView(this);
        mTv_ticket.setTextSize(20);
        mTv_ticket.setBackgroundColor(Color.GRAY);
        mTv_ticket.setClickable(true);//抢占点击事件，不让用户点到真实的标签
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_uninstall_appmanager:
                //卸载
                uninstallApp(mClickAppInfoBean.getPackageName());
                break;
            case R.id.ll_launch_appmanager:
                //启动
                startApp(mClickAppInfoBean.getPackageName());
                break;
            case R.id.ll_share_appmanager:
                //分享
//                share();
                break;
            case R.id.ll_setting_appmanager:
                //设置
                toAppInfoSetting(mClickAppInfoBean.getPackageName());
                break;
        }
        mPopupWindow.dismiss();
    }

    /**
     * 分享
     */
    /*private void share() {
//java
        OnekeyShare oks = new OnekeyShare();
// title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享的标题");
// titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
// imagePath是图片的本地路径，确保SDcard下面存在此张图片
        oks.setImagePath("/sdcard/tp2.jpg");
// url在微信、Facebook等平台中使用
        oks.setUrl("https://www.baidu.com");
// 启动分享GUI
        oks.show(MobSDK.getContext());
    }*/

    /**
     * 跳转到APP详情信息设置页面
     * @param packageName
     */
    private void toAppInfoSetting(String packageName) {
       Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
       intent.setData(Uri.parse("package:"+packageName));
       startActivity(intent);
    }

    /**
     * 启动APP
     * @param packageName
     */
    private void startApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent!=null){
            startActivity(intent);
        }else {
            Toast.makeText(this,"没有界面",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 卸载应用APP
     * @param packageName
     */
    private void uninstallApp(String packageName) {
        if (mClickAppInfoBean.isSystemApp()){

        }else {
            Intent intent = new Intent("android.intent.action.DELETE");
            intent.setData(Uri.parse("package:"+packageName));
            startActivity(intent);
        }

    }
}
