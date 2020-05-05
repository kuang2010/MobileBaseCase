package com.kzy.mobilesafe.activity.jcgl;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.RuningAppManagerAdapter;
import com.kzy.mobilesafe.bean.AppInfoBean;
import com.kzy.mobilesafe.utils.AppInfoUtil;
import com.kzy.mobilesafe.utils.PhoneUtil;
import com.kzy.mobilesafe.view.MemoryProgressBar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class ProcessTaskManagerAcitivity extends Activity implements View.OnClickListener {

    private static final int LOADING = 1;
    private static final int FINISH = 2;
    private MemoryProgressBar mPb_process_num;
    private MemoryProgressBar mPb_process_memory;
    private ProgressBar mPb_load_process;
    private RecyclerView mRv_data_process;
    private TextView mTv_mark_process;
    private long mTotalMemorySpace;
    private long mAvailMemorySpace;
    private List<AppInfoBean> mRunningApp;
    private RuningAppManagerAdapter mAdapter;
    private View mLayout_mark_process;
    private int mLastSystemRunAppIndex;
    private ImageView mIv_clean_run_app;
    private Button mBtn_select_all_runapp;
    private Button mBtn_unselect_all_runapp;
    private ActivityManager mAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_task_manager_acitivity);
        /*
        <!--有权查看使用情况的应用程序-->
        <uses-permission
        android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
        * */
        if (isNoOption()){
            if (!AppInfoUtil.isUsagestatsGranted(this)){
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
                finish();
            }
        }

        initView();
        initData();
        initEnvent();
    }

    /**
     * 判断当前设备中是否有"有权查看使用情况的应用程序"这个选项
     * @return
     */
    private boolean isNoOption() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        return PhoneUtil.hasActivity(this,intent);
    }



    private void initEnvent() {
        mIv_clean_run_app.setOnClickListener(this);
        mBtn_select_all_runapp.setOnClickListener(this);
        mBtn_unselect_all_runapp.setOnClickListener(this);

        mAdapter.setOnItemclickListener(new RuningAppManagerAdapter.OnItemclickListener() {
            @Override
            public void onClick(View view, int position) {
                AppInfoBean appInfoBean = mRunningApp.get(position);
                appInfoBean.setCheck(!appInfoBean.isCheck());
                mAdapter.setDatas(mRunningApp);
            }
        });


        mRv_data_process.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition < mLastSystemRunAppIndex+1){
                    mTv_mark_process.setText("系统软件");
                }else {
                    mTv_mark_process.setText("应用软件");
                }


                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING:
                    mPb_load_process.setVisibility(View.VISIBLE);
                    mRv_data_process.setVisibility(View.GONE);
                    mLayout_mark_process.setVisibility(View.GONE);
                    break;
                case FINISH:
                    mPb_load_process.setVisibility(View.GONE);
                    mRv_data_process.setVisibility(View.VISIBLE);
                    mLayout_mark_process.setVisibility(View.VISIBLE);
                    mTv_mark_process.setText("系统软件");
                    mAdapter.setDatas(mRunningApp);
                    mPb_process_num.setText("运行中的进程个数"+(mRunningApp.size()-2));
                    mPb_process_num.setProgress(mRunningApp.size()-2,AppInfoUtil.getAllAppsInfos(getApplicationContext()).size());
                    mPb_process_memory.setText("可用内存/总内存"+ Formatter.formatFileSize(getApplicationContext(),AppInfoUtil.getAvailMemorySpace(getApplicationContext()))+"/"+Formatter.formatFileSize(getApplicationContext(),AppInfoUtil.getTotalMemorySpace1()));
                    mPb_process_memory.setProgress(AppInfoUtil.getTotalMemorySpace1()-AppInfoUtil.getAvailMemorySpace(getApplicationContext()),AppInfoUtil.getTotalMemorySpace1());
                    break;
            }

        }
    };


    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mHandler.obtainMessage(LOADING).sendToTarget();

                mTotalMemorySpace = AppInfoUtil.getTotalMemorySpace1();
                mAvailMemorySpace = AppInfoUtil.getAvailMemorySpace(ProcessTaskManagerAcitivity.this);
                mRunningApp = AppInfoUtil.getRunningAppProcesses2(ProcessTaskManagerAcitivity.this);
                Collections.sort(mRunningApp, new Comparator<AppInfoBean>() {
                    @Override
                    public int compare(AppInfoBean o1, AppInfoBean o2) {
                        int o1Num = convertNum(o1.isSystemApp());
                        int o2Num = convertNum(o2.isSystemApp());
                        return o2Num-o1Num;
                    }
                });
                mLastSystemRunAppIndex = findFirstIndexUserApp(mRunningApp);
                AppInfoBean appInfoBeant = new AppInfoBean();
                appInfoBeant.setMark(true);
                mRunningApp.add(mLastSystemRunAppIndex,appInfoBeant);//添加用户软件标签
                AppInfoBean appInfoBeano = new AppInfoBean();
                appInfoBeano.setMark(true);
                mRunningApp.add(0,appInfoBeano);//添加系统软件标签
                Log.d("tagtag",""+ mRunningApp.toString());

                mHandler.obtainMessage(FINISH).sendToTarget();
            }

        }.start();

        mAdapter = new RuningAppManagerAdapter(getApplicationContext());
        mAdapter.setDatas(mRunningApp);
        mRv_data_process.setLayoutManager(new LinearLayoutManager(this));
        mRv_data_process.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRv_data_process.setAdapter(mAdapter);

        mAM = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

    }


    /**
     * 获取到第一个运行的用户APP的位置，以便在其位置加一个标签
     * @param runningApp
     * @return
     */
    private int findFirstIndexUserApp(List<AppInfoBean> runningApp) {
        int index = 0;
        for (int i=0;i<runningApp.size();i++){
            if (!runningApp.get(i).isSystemApp()){
                index = i;
                break;
            }
        }
        return index;
    }


    /**
     * 把布尔值转成数字 true对应2，false对应1
     * @param systemApp
     * @return
     */
    private int convertNum(boolean systemApp) {
        return systemApp?2:1;
    }

    private void initView() {
        mPb_process_num = findViewById(R.id.pb_process_num);
        mPb_process_memory = findViewById(R.id.pb_process_memory);
        mPb_load_process = findViewById(R.id.pb_load_process);
        mRv_data_process = findViewById(R.id.rv_data_process);
        mLayout_mark_process = findViewById(R.id.layout_mark_process);
        mTv_mark_process = mLayout_mark_process.findViewById(R.id.tv_mark_process);
        mIv_clean_run_app = mLayout_mark_process.findViewById(R.id.iv_clean_run_app);
        mIv_clean_run_app.setVisibility(View.VISIBLE);
        mBtn_select_all_runapp = findViewById(R.id.btn_select_all_runapp);
        mBtn_unselect_all_runapp = findViewById(R.id.btn_unselect_all_runapp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_all_runapp://全选
                if (mRunningApp!=null && mRunningApp.size()>0){
                    for (AppInfoBean appInfoBean:mRunningApp){
                        appInfoBean.setCheck(true);
                    }
                    mAdapter.setDatas(mRunningApp);
                }
                break;
            case R.id.btn_unselect_all_runapp://反选
                if (mRunningApp!=null && mRunningApp.size()>0){
                    for (AppInfoBean appInfoBean:mRunningApp){
                        appInfoBean.setCheck(!appInfoBean.isCheck());
                    }
                    mAdapter.setDatas(mRunningApp);
                }
                break;
            case R.id.iv_clean_run_app://清理进程(假的)
                for (int i=0;i<mRunningApp.size();i++){
                    if (!mRunningApp.get(i).isMark()){
                        if (mRunningApp.get(i).isCheck()){
                            mAM.killBackgroundProcesses(mRunningApp.get(i).getPackageName());
                            mRunningApp.remove(i);
                            i--;

                        }

                    }
                }
                mAdapter.setDatas(mRunningApp);
                break;
        }
    }
}
