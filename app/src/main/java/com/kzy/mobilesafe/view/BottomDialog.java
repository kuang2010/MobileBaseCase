package com.kzy.mobilesafe.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.kzy.mobilesafe.R;

/**
 * author: kuangzeyu2019
 * date: 2020/4/8
 * time: 22:55
 * desc: gravity在屏幕底部的dialog
 */
public class BottomDialog extends Dialog {


    public BottomDialog(@NonNull Context context) {
        this(context, R.style.dialog_style);
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public BottomDialog setView(int layoutResID){
        View mView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
        setContentView(mView);
        return this;
    }

    public BottomDialog setView(View view){
        setContentView(view);
        return this;
    }



    /**
     * 该方法会在dialog.show()的时候被调用。调用它之前应该先调用setContentView(.)完成设置内容view，否则对params的设置不生效
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        getWindow().setWindowAnimations(R.style.dialog_anim_style);
    }

}
