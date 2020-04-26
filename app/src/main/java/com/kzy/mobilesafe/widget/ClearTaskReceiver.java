package com.kzy.mobilesafe.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kzy.mobilesafe.activity.jcgl.ProcessTaskManagerAcitivity;

/**
 * author: kuangzeyu2019
 * date: 2020/4/26
 * time: 18:55
 * desc: ClearRunningAppWidgeterService  widget.clear.task
 */
public class ClearTaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"widget.clear.task",Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, ProcessTaskManagerAcitivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
