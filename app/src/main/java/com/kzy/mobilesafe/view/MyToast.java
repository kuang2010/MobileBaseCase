package com.kzy.mobilesafe.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kzy.mobilesafe.R;

/**
 * author: kuangzeyu2019
 * date: 2020/4/5
 * time: 18:16
 * desc:  自定义toast
 */
public class MyToast implements View.OnTouchListener {

    private Context mContext;
    private WindowManager mWM;
    private  View mView;
    private  WindowManager.LayoutParams mParams;
    private  TextView mTvToastMess;
    private float mDownRawX;
    private float mDownRawY;


    public MyToast(Context context) {
        mContext = context;
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        //去掉toast原来显示时的动画效果
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;//TYPE_TOAST  TYPE_PRIORITY_PHONE
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mParams.gravity = Gravity.LEFT|Gravity.TOP;//设置toast view的原点在左上角的位置,以便后面的拖动

        int mOriginalX = mParams.x;
        int mOriginalY = mParams.y;
        Log.d("tagtag","mOriginalX:"+mOriginalX+",mOriginalY:"+mOriginalY);

        mView = View.inflate(mContext, R.layout.item_toast, null);

        mTvToastMess = mView.findViewById(R.id.tv_toast_mess);

        mView.setOnTouchListener(this);
    }

    public void showToast(String mess){

        mTvToastMess.setText(mess);

        mWM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);

        if (mView.getParent() != null) {

            mWM.removeView(mView);
        }

        try {

            mWM.addView(mView, mParams);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void hideToast(){
        if (mView != null ) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWM.removeViewImmediate(mView);
            }

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                mDownRawX = event.getRawX();
                mDownRawY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveRawX = event.getRawX();
                float moveRawY = event.getRawY();

                float v1 = moveRawX - mDownRawX;
                float v2 = moveRawY - mDownRawY;

                mParams.x = (int) (mParams.x + v1);
                mParams.y = (int) (mParams.y + v2);

                //超出位置
                if (mParams.x<0){
                    mParams.x = 0;
                }else if (mParams.x > (mWM.getDefaultDisplay().getWidth() - mView.getWidth())){
                    mParams.x = mWM.getDefaultDisplay().getWidth() - mView.getWidth();
                }

                if (mParams.y < 0){
                    mParams.y = 0;
                }else if (mParams.y>(mWM.getDefaultDisplay().getHeight()-mView.getHeight())){
                    mParams.y = mWM.getDefaultDisplay().getHeight()-mView.getHeight();
                }


                //改变view的位置
                mWM.updateViewLayout(mView, mParams);

                mDownRawX = moveRawX;
                mDownRawY = moveRawY;

                break;

            case MotionEvent.ACTION_UP://自己消费掉
                break;


        }

        return true;
    }
}
