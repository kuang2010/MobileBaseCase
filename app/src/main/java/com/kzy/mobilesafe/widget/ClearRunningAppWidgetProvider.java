package com.kzy.mobilesafe.widget;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * author: kuangzeyu2019
 * date: 2020/4/26
 * time: 17:03
 * desc: widget小部件。它跟APP在不同的进程
 */
public class ClearRunningAppWidgetProvider extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("tagtag","ClearRunningAppWidgetProvider_onReceive");
    }

    // 第一次创建执行
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("tagtag","ClearRunningAppWidgetProvider_onEnabled");
        Intent intent = new Intent(context,ClearRunningAppWidgeterService.class);
        context.startService(intent);

    }

    //删除最后一个执行
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d("tagtag","ClearRunningAppWidgetProvider_onDisabled");
        Intent intent = new Intent(context,ClearRunningAppWidgeterService.class);
        context.stopService(intent);
    }
}
