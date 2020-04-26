package com.kzy.mobilesafe.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.jcgl.ProcessTaskManagerAcitivity;
import com.kzy.mobilesafe.utils.AppInfoUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author: kuangzeyu2019
 * date: 2020/4/26
 * time: 18:48
 * desc: 清理进程的widget的服务(管理widget小部件)
 */
public class ClearRunningAppWidgeterService extends Service {
    private AppWidgetManager mAWM;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("tagtag","ClearRunningAppWidgeterService>>>>>>>>>onCreate");

        mAWM = AppWidgetManager.getInstance(getApplicationContext());

        System.out.println("widget  service create");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateWidgetMessage();

            }
        };
        timer.schedule(task, 0 , 1000 * 2);//每隔2s
    }

    private void updateWidgetMessage() {

        ComponentName provider = new ComponentName(getApplicationContext(), ClearRunningAppWidgetProvider.class);
        //远程view。因为widget小部件不跟APP在同一个进程
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
        views.setTextViewText(R.id.tv_process_count, "运行中的软件:" + AppInfoUtil.getRunningAppProcesses2(getApplicationContext()).size());
        views.setTextViewText(R.id.tv_process_memory, "可用内存:" + Formatter.formatFileSize(getApplicationContext(),
                AppInfoUtil.getAvailMemorySpace(getApplicationContext())));

        Intent intent = new Intent();
        intent.setAction("widget.clear.task");//PendingIntent.getBroadcast获取的广播一定要在清单文件里注册
        PendingIntent pendingIntent_broadcast = PendingIntent.getBroadcast(getApplicationContext(), 0, intent , 0);

        Intent intent2 = new Intent(this, ProcessTaskManagerAcitivity.class);
        PendingIntent pendingIntent_activity = PendingIntent.getActivity(getApplicationContext(), 0, intent2 , 0);
        views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent_activity );

        // 更新widget界面
        mAWM.updateAppWidget(provider, views);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
