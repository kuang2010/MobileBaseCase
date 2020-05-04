package com.kzy.mobilesafe.activity.gjgj.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.gjgj.activity.LockActivity;
import com.kzy.mobilesafe.adapter.AppLockAdapter;
import com.kzy.mobilesafe.bean.AppInfoBean;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/5/2
 * time: 17:23
 * desc:
 */
public class BaseLockFragment extends Fragment {

    private List<AppInfoBean> mAppInfoBeans;
    private AppLockAdapter mAdapter;
//    private Animation mLock_tranlate;
//    private Animation mUnlock_tranlate;
    private ObjectAnimator mUnLockObjAnima;
    private ObjectAnimator mLockObjAnima;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("tagtag","onAttach:"+this.getClass().getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("tagtag","onCreate:"+this.getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("tagtag","onCreateView:"+this.getClass().getName());
        RecyclerView rootView = (RecyclerView) inflater.inflate(R.layout.fragment_lock, null);
        initView(rootView);
        initAnimation();
        initData();
        initEvent();
        return rootView;
    }

    private void initAnimation() {
//        mLock_tranlate = AnimationUtils.loadAnimation(getContext(), R.anim.lock_tranlate);
//        mUnlock_tranlate = AnimationUtils.loadAnimation(getContext(), R.anim.unlock_tranlate);
//        mLock_tranlate.setFillAfter(true);
//        mUnlock_tranlate.setFillAfter(true);

//        mLock_obj_translate = AnimatorInflater.loadAnimator(getContext(), R.animator.lock_obj_translate);
//        mUnlock_obj_translate = AnimatorInflater.loadAnimator(getContext(), R.animator.unlock_obj_translate);


    }

    private void initEvent() {
        final Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        mAdapter.setOnLockClickListener(new AppLockAdapter.OnLockClickListener() {
            @Override
            public void onLockClick(View view, final AppInfoBean mClickAppInfoBean, final int position) {
                if (mClickAppInfoBean.isLock()){
                    //已加锁,解锁业务
                    if (mUnLockObjAnima==null){
                        mUnLockObjAnima = ObjectAnimator.ofFloat(
                                view,
                                "translationX",
                                0,-view.getWidth()
                        );
                        mUnLockObjAnima.setDuration(500);
                    }
                    mUnLockObjAnima.setTarget(view);
                    mUnLockObjAnima.start();

                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            synchronized (BaseLockFragment.this){
                                SystemClock.sleep(500);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((LockActivity)getActivity()).lock(mClickAppInfoBean, position);
                                    }
                                });
                            }

                        }
                    }.start();

                }else {
                    //未加锁，加锁业务
                    if (mLockObjAnima==null){
                        mLockObjAnima = ObjectAnimator.ofFloat(
                                view,
                                "translationX",
                                0,view.getWidth()
                        );
                        mLockObjAnima.setDuration(500);
                    }
                    mLockObjAnima.setTarget(view);
                    mLockObjAnima.start();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            synchronized (BaseLockFragment.this){
                                SystemClock.sleep(500);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((LockActivity)getActivity()).lock(mClickAppInfoBean, position);
                                    }
                                });
                            }
                        }
                    }.start();
                }
            }

        });


    }

    private void initData() {

    }

    private void initView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        mAdapter = new AppLockAdapter(getActivity());
        mAdapter.setDatas(mAppInfoBeans);
        recyclerView.setAdapter(mAdapter);

    }



    protected void setTitle(TextView rootView) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("tagtag","onActivityCreated:"+this.getClass().getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("tagtag","onStart:"+this.getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("tagtag","onStop:"+this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("tagtag","onDestroyView:"+this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("tagtag","onDestroy:"+this.getClass().getName());
    }

    public void setDatasAndRefreshUi(List<AppInfoBean> appInfoBeans) {
        mAppInfoBeans = appInfoBeans;
        if (mAdapter!=null){
            mAdapter.setDatas(mAppInfoBeans);
        }
    }
}
