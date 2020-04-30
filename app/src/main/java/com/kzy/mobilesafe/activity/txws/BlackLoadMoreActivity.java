package com.kzy.mobilesafe.activity.txws;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.BlackAdapter;
import com.kzy.mobilesafe.bean.BlackBean;
import com.kzy.mobilesafe.dao.BlackDao;
import com.kzy.mobilesafe.db.BlackDb;

import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单 分批 加载页面
 */
public class BlackLoadMoreActivity extends Activity implements View.OnClickListener {

    private static final int LOADING = 1;
    private static final int  FINISH = 2;
    private static final int NOMOREDATA = 3;//没有数据了
    private ImageView mIvAddBlack;
    private ImageView mIvBlackUiNoData;
    private ListView mLvBlackUiHaveData;
    private BlackAdapter mAdapter;
    private BlackDao mBlackDao;
    private List<BlackBean> mBlackBeans = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private PopupWindow mPopupWindow;
    private Animation mAnimation;
    private View mPopView;
    private Animator mAnimator;
    private int countPerPage = 3;//每页加载20条数据
    private int pageNum = 1;//或加载的页码
    private enum LoadState{
        init,
        loading,
        finish,
        nodata
    }
    LoadState mLoadState = LoadState.init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
        initView();
        initData();
        initEvent();

        initLoadingDialog();

        initPopuWindow();

        requestPermissions(new String[]{"android.permission.RECEIVE_SMS"},100 );


    }


    private void initPopuWindow() {

        mPopView = LayoutInflater.from(this).inflate(R.layout.view_pop_add_black,null);
        mPopView.findViewById(R.id.tv_add_black_shoudong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                showAddBlackDialog();
            }
        });

        mPopupWindow = new PopupWindow(mPopView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置背景 1. 外部点击生效
        //mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);//设置触摸区域外可隐藏PopupWindow

        mPopupWindow.setFocusable(true);//获取焦点
        mPopupWindow.setTouchable(true);//设置可触摸

        //初始化个动画给pop弹出用
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_add_black);

        //属性动画
        mAnimator = AnimatorInflater.loadAnimator(this, R.animator.scale_add_black);
        mPopView.setPivotY(0);
        mAnimator.setTarget(mPopView);

    }

    private void showAddBlackDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view_add_black = LayoutInflater.from(this).inflate(R.layout.dialog_view_add_black, null);
        final EditText et_black_phone_dialog = view_add_black.findViewById(R.id.et_black_phone_dialog);
        final CheckBox cb_mode_phone_dialog = view_add_black.findViewById(R.id.cb_mode_phone_dialog);
        final CheckBox cb_mode_sms_dialog = view_add_black.findViewById(R.id.cb_mode_sms_dialog);
        Button btn_add_dialog = view_add_black.findViewById(R.id.btn_add_dialog);
        Button btn_cancel_dialog = view_add_black.findViewById(R.id.btn_cancel_dialog);
        ab.setView(view_add_black);
        final AlertDialog mDialogAddBlack = ab.create();
        mDialogAddBlack.show();
        btn_add_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_black_phone_dialog.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(getApplicationContext(),"请输入要拦截的号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!cb_mode_phone_dialog.isChecked() && !cb_mode_sms_dialog.isChecked()){
                    Toast.makeText(getApplicationContext(),"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }

                //添加，不用做添加后再分页那么麻烦了，直接添加就好
                BlackBean blackBean = new BlackBean();
                blackBean.setPhone(et_black_phone_dialog.getText().toString().trim());
                int mode = 0;
                if (cb_mode_phone_dialog.isChecked()) {
                    mode |= BlackDb.MODE_PHONE;
                }

                if (cb_mode_sms_dialog.isChecked()){
                    mode |= BlackDb.MODE_SMS;
                }
                blackBean.setMode(mode);

                mBlackBeans.remove(blackBean);
                mBlackBeans.add(0,blackBean);
                mAdapter.setDatas(mBlackBeans);

                //最好在子线程里处理
                mBlackDao.update(blackBean);

                mIvBlackUiNoData.setVisibility(View.GONE);
                mLvBlackUiHaveData.setVisibility(View.VISIBLE);
                mDialogAddBlack.dismiss();
            }
        });
        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogAddBlack.dismiss();
            }
        });
    }

    private void initLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载..");
    }

    private void initEvent() {

        mAdapter.setOnBlackItemClickListener(new BlackAdapter.OnBlackItemClickListener() {
            @Override
            public void onDelete(int position) {
                //删除
                if (mBlackBeans!=null){

                    if (mBlackBeans.size()>=position){
                        BlackBean blackBean = mBlackBeans.get(position);
                        mBlackBeans.remove(blackBean);
                        mAdapter.setDatas(mBlackBeans);
                        mBlackDao.delete(blackBean.getPhone());

                        //由于是分页加载，所以要补充一个进来
//                        BlackBean blackBean1 = mBlackDao.queryOneAfterDelete(mBlackBeans.size());
                        List<BlackBean> blackBeans = mBlackDao.queryPartData2(mBlackBeans.size(),1);
                        if (blackBeans!=null&&blackBeans.size()>0){
                            BlackBean blackBean1 = blackBeans.get(0);
                            if (blackBean1!=null){
                                mBlackBeans.add(blackBean1);
                                mAdapter.setDatas(mBlackBeans);
                            }
                        }

                        if (mBlackBeans.size()==0){
                            mIvBlackUiNoData.setVisibility(View.VISIBLE);
                            mLvBlackUiHaveData.setVisibility(View.GONE);
                        }

                        //由于是分批加载数据，为了不造成数据混乱，需要先清除下集合数据,从头开始
//                        mBlackBeans.clear();
//                        getDataAndRefreshUI();
                    }

                }

            }
        });

        mIvAddBlack.setOnClickListener(this);

        mLvBlackUiHaveData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    //静止状态
                    int lastVisiblePosition = mLvBlackUiHaveData.getLastVisiblePosition();//listview上最后一条数据的位置

                    if (lastVisiblePosition!=-1 && lastVisiblePosition >= (mBlackBeans.size()-1)){
                        //滑到了最后
                        if (mLoadState==LoadState.init || mLoadState==LoadState.finish){
                            //可以去加载更多数据
                            getDataAndRefreshUI();

                        }


                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void initData() {
        mBlackDao = new BlackDao(this);
        mAdapter = new BlackAdapter(this,mBlackDao);
        mLvBlackUiHaveData.setAdapter(mAdapter);
        getDataAndRefreshUI();
    }

    private void getDataAndRefreshUI() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mHandler.obtainMessage(LOADING).sendToTarget();

                //mBlackBeans = mBlackDao.queryAll();

                //分批加载
                //List<BlackBean> blackBeans = mBlackDao.queryPartData1(pageNum, countPerPage);
                List<BlackBean> blackBeans = mBlackDao.queryPartData2(mBlackBeans.size(), countPerPage);
                SystemClock.sleep(200);

                if (blackBeans!=null && blackBeans.size()>0){

                    mBlackBeans.addAll(blackBeans);

                    mHandler.obtainMessage(FINISH).sendToTarget();

                }else {

                    mHandler.obtainMessage(NOMOREDATA).sendToTarget();
                }
            }
        }.start();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING:
                    mLoadState = LoadState.loading;
                    mProgressDialog.show();
                    break;
                case FINISH:
                    mLoadState = LoadState.finish;
                    pageNum++;
                    //有数据
                    mIvBlackUiNoData.setVisibility(View.GONE);
                    mLvBlackUiHaveData.setVisibility(View.VISIBLE);
                    mAdapter.setDatas(mBlackBeans);
                    mAdapter.notifyDataSetChanged();

                    mProgressDialog.dismiss();
                    break;
                case NOMOREDATA://没有数据了
                    mLoadState = LoadState.nodata;
                    if (mBlackBeans==null||mBlackBeans.size()==0){
                        //没有数据
                        mIvBlackUiNoData.setVisibility(View.VISIBLE);
                        mLvBlackUiHaveData.setVisibility(View.GONE);
                    }else {
                        //没有更多数据了
                        mIvBlackUiNoData.setVisibility(View.GONE);
                        mLvBlackUiHaveData.setVisibility(View.VISIBLE);
                        Toast.makeText(BlackLoadMoreActivity.this,"没有更多数据了",Toast.LENGTH_SHORT).show();
                    }
                    mProgressDialog.dismiss();
                    break;
                default:
                    break;

            }

        }
    };

    private void initView() {
        mIvAddBlack = findViewById(R.id.iv_add_black);
        mIvBlackUiNoData = findViewById(R.id.iv_black_ui_no_data);
        mLvBlackUiHaveData = findViewById(R.id.lv_black_ui_have_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_black:
                Log.d("tagtag",""+mPopupWindow.isShowing());
                if (mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }else {
                    mPopupWindow.showAsDropDown(mIvAddBlack);
//                    mPopView.startAnimation(mAnimation);
                    mAnimator.start();
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
        super.onDestroy();
    }
}
