package com.kzy.mobilesafe.activity.sjfd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.BaseActivity;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 18:32
 * desc:
 */
public abstract class BaseSetupActivity extends BaseActivity implements View.OnClickListener {

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        initView();
        initData();
        initEvent();

        //初始化手势识别器
        initGuestrueDector();
    }

    private void initGuestrueDector() {
        mGestureDetector = new GestureDetector(new MyGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d("tagtag","onFling..");
                // e1 按下的点
                // e2 松开的点
                // velocityX x方向速度  单位 px/s
                // velocityY y方向的速度
                //判断 是否是x方向滑动
                if (Math.abs(e1.getX()-e2.getX())>Math.abs(e1.getY()-e2.getY())){
                    //横向滑动
                    //判断距离
                    if (Math.abs(e1.getX()-e2.getX())>50){
                        //判断速度
                        if (Math.abs(velocityX)>50){
                            //再判断是向左还是向右滑动
                            if (velocityX>0){
                                //向右滑动
                                Log.d("xxxxx","向右滑动");
                                goPrePage(getPreClass());
                            }else {
                                //向左滑动
                                Log.d("xxxxx","向左滑动");
                                goNextPage(getNextClass());
                            }

                            return true;
                        }
                    }

                }


                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract Class getNextClass();//下一个向导页面的Class

    protected abstract Class getPreClass();//上一个向导页面的Class



    protected void initEvent() {
        
    }

    protected void initData() {
        
    }

    protected void initView() {

    }

    protected abstract int getLayoutResId();

    @Override
    public void onClick(View v) {

    }

    public void goNextPage(Class nextClass) {
        if (nextClass==null){
            return;
        }
        Intent intent = new Intent(this,nextClass);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_entry_fromright_anim,R.anim.activity_exit_toleft_anim);
        finish();
    }

    public void goPrePage(Class preClass){
        if (preClass==null){
            return;
        }
        Intent intent = new Intent(this,preClass);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_entry_fromleft_anim,R.anim.activity_exit_toright_anim);
        finish();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector!=null){
            mGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    class MyGestureListener implements GestureDetector.OnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("tagtag","onDown..");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("tagtag","onShowPress..");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("tagtag","onSingleTapUp..");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("tagtag","onScroll..");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("tagtag","onLongPress..");
        }

        /**
         * @param e1 按下的点
         * @param e2 松开的点
         * @param velocityX x方向的速度 px/s
         * @param velocityY y方向的速度 px/s
         * @return
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
