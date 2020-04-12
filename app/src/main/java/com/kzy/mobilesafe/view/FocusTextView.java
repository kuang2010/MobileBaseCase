package com.kzy.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 11:26
 * desc: 永远获得焦点的textview，用于实现文字滚动
 */
public class FocusTextView extends TextView {


    /**
     * 代码中实例化调用
     * @param context
     */
    public FocusTextView(Context context) {
        super(context);
    }

    /**
     * 配置文件中 实例化调用
     * @param context
     * @param attrs
     */
    public FocusTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
