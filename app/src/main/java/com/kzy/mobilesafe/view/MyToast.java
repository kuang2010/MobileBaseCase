package com.kzy.mobilesafe.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.LocationStyleDialog;
import com.kzy.mobilesafe.utils.SpUtil;

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
    private final Rect mRect;
    private final LinearLayout mLlBgToast;


    public MyToast(Context context) {
        mContext = context;
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        //去掉toast原来显示时的动画效果
        //params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        //mParams.type = WindowManager.LayoutParams.TYPE_TOAST;//TYPE_TOAST  TYPE_PRIORITY_PHONE
        //对版本有要求
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            //Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        }else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mParams.gravity = Gravity.LEFT|Gravity.TOP;//设置toast view的中心在屏幕左上角的位置,以便后面的拖动

        int mOriginalX = mParams.x;
        int mOriginalY = mParams.y;
        Log.d("tagtag","mOriginalX:"+mOriginalX+",mOriginalY:"+mOriginalY);

        mView = View.inflate(mContext, R.layout.item_toast, null);

        mTvToastMess = mView.findViewById(R.id.tv_toast_mess);

        mLlBgToast = mView.findViewById(R.id.ll_bg_toast);


        mView.setOnTouchListener(this);


        mRect = new Rect();
    }

    public void showToast(String mess){

        mTvToastMess.setText(mess);
        int index = SpUtil.getInt(mContext, MyConstants.LOCATIONSTYLESELECTPOS, MyConstants.LOCATIONDEFOULTINDX);
        mLlBgToast.setBackgroundResource(LocationStyleDialog.locDrawable[index]);

        mWM = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);

        if (mView.getParent() != null) {

            mWM.removeView(mView);
        }

        try {

            mWM.addView(mView, mParams);


        } catch (Exception e) {
            e.printStackTrace();
        }


        //test
        Looper.myLooper();
        new Handler().getLooper();

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

            case MotionEvent.ACTION_DOWN://按下的位置
                mDownRawX = event.getRawX();
                mDownRawY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE://移动后的位置
                int moveRawX = (int) event.getRawX();
                int moveRawY = (int) event.getRawY();

//                mView.getGlobalVisibleRect(mRect);
//                Log.d("tagtag","mRect2:"+mRect);
//                if (!mRect.contains(moveRawX,moveRawY)){
//                    //触摸点不在控件上
//                    return false;
//                }

//                int x = (int) mView.getX();
//                int y = (int) mView.getY();
//                int width = mView.getWidth();
//                int height = mView.getHeight();
//                mRect.set(x,y,x+width,y+height);
//                if (!mRect.contains(moveRawX,moveRawY)){
//                    //触摸点不在控件上
//                    return false;
//                }



                int[] location = new int[2];
                mView.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + mView.getMeasuredWidth();
                int bottom = top + mView.getMeasuredHeight();


                mView.getGlobalVisibleRect(mRect);//xxxx
                int left1 = mRect.left;
                int top1 = mRect.top;

                Log.d("tagtag1",""+left+","+top);
                Log.d("tagtag2",""+left1+","+top1);


                if (moveRawY >= top && moveRawY <= bottom && moveRawX >= left && moveRawX <= right) {
                    Log.d("tagtag","触摸在控件范围内");
                }else {
                    Log.d("tagtag","触摸不在控件范围内");
                    return false;
                }







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

            case MotionEvent.ACTION_UP:
                break;


        }

        return true;//自己消费掉
    }
}
