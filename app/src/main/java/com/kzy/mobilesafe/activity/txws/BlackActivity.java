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

import java.util.List;

/**
 * 黑名单页面
 */
public class BlackActivity extends Activity implements View.OnClickListener {

    private static final int LOADING = 1;
    private static final int  FINISH = 2;
    private ImageView mIvAddBlack;
    private ImageView mIvBlackUiNoData;
    private ListView mLvBlackUiHaveData;
    private BlackAdapter mAdapter;
    private BlackDao mBlackDao;
    private List<BlackBean> mBlackBeans;
    private ProgressDialog mProgressDialog;
    private PopupWindow mPopupWindow;
    private Animation mAnimation;
    private View mPopView;
    private Animator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
        initView();
        initData();
        initEvent();

        initLoadingDialog();

        initPopuWindow();

        requestPermissions(new String[]{"android.permission.RECEIVE_SMS","android.permission.READ_PHONE_STATE","android.permission.CALL_PHONE"},100 );

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

                //添加
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

                //最好在子线程里处理
                mBlackDao.update(blackBean);

                getDataAndRefreshUI();

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
        mIvAddBlack.setOnClickListener(this);
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

                mBlackBeans = mBlackDao.queryAll();

                SystemClock.sleep(200);

                mHandler.obtainMessage(FINISH).sendToTarget();
            }
        }.start();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage( Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADING:
                    mProgressDialog.show();
                    break;
                case FINISH:
                    if (mBlackBeans==null||mBlackBeans.size()==0){
                        //没有数据
                        mIvBlackUiNoData.setVisibility(View.VISIBLE);
                        mLvBlackUiHaveData.setVisibility(View.GONE);
                    }else {
                        //有数据
                        mIvBlackUiNoData.setVisibility(View.GONE);
                        mLvBlackUiHaveData.setVisibility(View.VISIBLE);

                        mAdapter.setDatas(mBlackBeans);
                        mAdapter.notifyDataSetChanged();
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
