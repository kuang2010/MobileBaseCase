package com.kzy.mobilesafe.activity.gjgj.activity;

import android.app.ProgressDialog;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.gjgj.fragment.BaseLockAnimFragment;
import com.kzy.mobilesafe.activity.gjgj.fragment.LockFragment;
import com.kzy.mobilesafe.activity.gjgj.fragment.UnLockFragment;
import com.kzy.mobilesafe.bean.AppInfoBean;
import com.kzy.mobilesafe.dao.AppLockDao;
import com.kzy.mobilesafe.db.AppLockDb;
import com.kzy.mobilesafe.utils.AppInfoUtil;

import java.util.ArrayList;
import java.util.List;

//程序锁
public class LockActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LOADING = 1;
    private static final int FINISH = 2;
    private CheckBox mCb_lock_btn_head;
    private CheckBox mCb_unlock_btn_head;
    private LockFragment mLockFragment;
    private UnLockFragment mUnLockFragment;
    private FragmentManager mSupportFragmentManager;
    private List<AppInfoBean> lockAppInfoBeans = new ArrayList<>();
    private List<AppInfoBean> unLockAppInfoBeans = new ArrayList<>();
    private AppLockDao mAppLockDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        mLockFragment = new LockFragment();
        mUnLockFragment = new UnLockFragment();
        mSupportFragmentManager = getSupportFragmentManager();
        mAppLockDao = new AppLockDao(this);
        //由于fragment的生命周期方法会多出重复调用，所以把获取数据放到activity里提供性能
        getDatas();
    }

    private Handler mHandler = new Handler(){
        ProgressDialog dialog;
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING:
                    dialog = new ProgressDialog(LockActivity.this);
                    dialog.setMessage("正在加载数据");
                    dialog.show();
                    break;
                case FINISH:
                    if (dialog!=null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    mLockFragment.setDatasAndRefreshUi(lockAppInfoBeans);//fragment不建议用setData方法
                    mUnLockFragment.setDatasAndRefreshUi(unLockAppInfoBeans);
                    mCb_lock_btn_head.setChecked(false);
                    mCb_unlock_btn_head.setChecked(true);
                    selectFragment(mUnLockFragment);
                    break;
            }
        }
    };
    private void getDatas() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mHandler.obtainMessage(LOADING).sendToTarget();
                List<AppInfoBean> allAppsInfos = AppInfoUtil.getAllAppsInfos(LockActivity.this);

                //造假删除自己
                allAppsInfos.remove(new AppInfoBean(getPackageName()));

                lockAppInfoBeans.clear();
                for (AppInfoBean appInfoBean:allAppsInfos){
                    String packageName = appInfoBean.getPackageName();
                    Log.d("tagtag","packageName:"+packageName);
                    if (mAppLockDao.queryIsLock(packageName)){
                        //加锁的
                        appInfoBean.setLock(true);
                        lockAppInfoBeans.add(appInfoBean);
                    }else {
                        //未加锁的
                        appInfoBean.setLock(false);
                        unLockAppInfoBeans.add(appInfoBean);
                    }
                }
                SystemClock.sleep(10000);
                mHandler.obtainMessage(FINISH).sendToTarget();
            }
        }.start();
    }

    private void initEvent() {
        mCb_lock_btn_head.setOnClickListener(this);
        mCb_unlock_btn_head.setOnClickListener(this);
    }

    private void initView() {
        mCb_lock_btn_head = findViewById(R.id.cb_lock_btn_head);
        mCb_unlock_btn_head = findViewById(R.id.cb_unlock_btn_head);
        mCb_lock_btn_head.post(new Runnable() {
            @Override
            public void run() {
                int width = mCb_lock_btn_head.getWidth();
                Log.d("xxxxx","width:"+width);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cb_lock_btn_head://加锁
                mCb_lock_btn_head.setChecked(true);
                mCb_unlock_btn_head.setChecked(false);
                selectFragment(mLockFragment);
                break;
            case R.id.cb_unlock_btn_head://未加锁
                mCb_lock_btn_head.setChecked(false);
                mCb_unlock_btn_head.setChecked(true);
                selectFragment(mUnLockFragment);
                break;
            default:
                break;
        }
    }

    private BaseLockAnimFragment lastShowFragment;
    private void selectFragment(BaseLockAnimFragment lockFragment) {
        FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fl_content_lock,lockFragment,lockFragment.getClass().getName());
//        fragmentTransaction.add(R.id.fl_content_lock,lockFragment,lockFragment.getClass().getName());
//        fragmentTransaction.commit();

        if (lastShowFragment!=null){
            fragmentTransaction.hide(lastShowFragment);
        }
        Fragment fragmentByTag = mSupportFragmentManager.findFragmentByTag(lockFragment.getClass().getName());

        if (fragmentByTag==null){

            fragmentTransaction.add(R.id.fl_content_lock,lockFragment,lockFragment.getClass().getName());

        }else {

            fragmentTransaction.show(fragmentByTag);
        }

        lastShowFragment = lockFragment;

        fragmentTransaction.commitAllowingStateLoss();
        mSupportFragmentManager.executePendingTransactions();
    }

    public void lock(AppInfoBean clickAppInfoBean, int position) {
        boolean lock = clickAppInfoBean.isLock();
        if (lock){
            //解锁业务
            clickAppInfoBean.setLock(false);
            mAppLockDao.delete(clickAppInfoBean.getPackageName());
            lockAppInfoBeans.remove(clickAppInfoBean);
            unLockAppInfoBeans.add(clickAppInfoBean);
            mLockFragment.changeDatasAndRefreshUi(position);
            mUnLockFragment.setDatasAndRefreshUi(unLockAppInfoBeans);
        }else {
            //加锁业务
            clickAppInfoBean.setLock(true);
            mAppLockDao.insert(clickAppInfoBean.getPackageName());
            unLockAppInfoBeans.remove(clickAppInfoBean);
            lockAppInfoBeans.add(clickAppInfoBean);
            mUnLockFragment.changeDatasAndRefreshUi(position);
            mLockFragment.setDatasAndRefreshUi(lockAppInfoBeans);
        }
    }
}
