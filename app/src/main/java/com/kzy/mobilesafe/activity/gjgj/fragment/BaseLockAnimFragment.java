package com.kzy.mobilesafe.activity.gjgj.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.gjgj.activity.LockActivity;
import com.kzy.mobilesafe.activity.gjgj.animator.BaseItemAnimator;
import com.kzy.mobilesafe.activity.gjgj.animator.BaseItemAnimator2;
import com.kzy.mobilesafe.activity.gjgj.animator.LockItemAnimator;
import com.kzy.mobilesafe.activity.gjgj.animator.LockItemAnimator2;
import com.kzy.mobilesafe.activity.gjgj.animator.UnLockItemAnimator;
import com.kzy.mobilesafe.activity.gjgj.animator.UnLockItemAnimator2;
import com.kzy.mobilesafe.adapter.AppLockAdapter;
import com.kzy.mobilesafe.bean.AppInfoBean;

import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/5/2
 * time: 17:23
 * desc: recycleview 实现了item动画
 */
public class BaseLockAnimFragment extends Fragment {

    private List<AppInfoBean> mAppInfoBeans;
    private AppLockAdapter mAdapter;

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
        initData();
        initEvent();
        return rootView;
    }


    private void initEvent() {

        mAdapter.setOnLockClickListener(new AppLockAdapter.OnLockClickListener() {
            @Override
            public void onLockClick(View view, final AppInfoBean mClickAppInfoBean,int position) {
                Log.d("onLockClickposition",""+position);
                try {
                    ((LockActivity)getActivity()).lock(mClickAppInfoBean,position);
                }catch (ArrayIndexOutOfBoundsException e){
                    //避免在动画过程中又点了该条目位置
                    e.printStackTrace();
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

//        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();//默认动画，只有alpha效果

        //由于没有能完全copy到 DefaultItemAnimator导致有点小问题
//        BaseItemAnimator defaultItemAnimator = new BaseItemAnimator();
//        if (this instanceof UnLockFragment){
//            defaultItemAnimator = new UnLockItemAnimator();
//        }else if (this instanceof LockFragment){
//            defaultItemAnimator = new LockItemAnimator();
//        }

        BaseItemAnimator2 defaultItemAnimator = new BaseItemAnimator2();
        if (this instanceof UnLockFragment){
            defaultItemAnimator = new UnLockItemAnimator2();
        }else if (this instanceof LockFragment){
            defaultItemAnimator = new LockItemAnimator2();
        }

        defaultItemAnimator.setAddDuration(500);
        defaultItemAnimator.setRemoveDuration(500);
        //给recyclerview的item添加删除和增加的动画。要配合notifyItemXxx使用才有效
        recyclerView.setItemAnimator(defaultItemAnimator);//要配合notifyItemXxx使用才有效！！！



//        获取recycleview item坐标位置对应的position
//        int position = recyclerView.getChildAdapterPosition(recyclerView.findChildViewUnder(x,y))

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


    public void changeDatasAndRefreshUi(int position) {
        if (mAdapter!=null){
            mAdapter.deleteData(position);
        }
    }
}
