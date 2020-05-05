package com.kzy.mobilesafe.activity.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.kzy.mobilesafe.Constant.MyConstants;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.activity.gjgj.AToolHomeActivity;
import com.kzy.mobilesafe.activity.gjgj.activity.LockEntryPassWordActivity;
import com.kzy.mobilesafe.dao.AppLockDao;
import com.kzy.mobilesafe.db.AppLockDb;
import com.kzy.mobilesafe.utils.AppInfoUtil;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * author: kuangzeyu2019
 * date: 2020/5/5
 * time: 10:19
 * desc: 看门狗服务
 */
public class WatchDog2Service extends Service {

    private Timer mTimer;
    private ActivityManager mActivityManager;
    private List<ActivityManager.RunningTaskInfo> runningTasks;
    private ActivityManager.RunningTaskInfo mRunningTaskInfo;
    private ComponentName mTopActivity;
    private UsageStatsManager mUsageStatsManager;
    private SortedMap<Long, UsageStats> mySortedMap;
    public static String WHITEPACKENAM = null;
    private List<String> mList;
    private AppLockDao mAppLockDao;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //开启前台服务通知,提高服务级别
    private void startPrority() {

        Intent intent = new Intent();
        intent.setAction("com.kzy.mobilesafe.action.home");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon));
        builder.setContentTitle("您有一条新通知");
        builder.setContentText("这是一条逗你玩的消息");
        builder.setAutoCancel(true);
        builder.setContentIntent(pintent);

// 【适配Android8.0】设置Notification的Channel_ID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }

// 【适配Android8.0】给NotificationManager对象设置NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }

        // 启动前台服务通知
        startForeground(1, builder.build());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("tagtag","WatchDog2Service:onCreate>>>>>>>>");

        boolean b = checkUseStage();
        if (!b){
            stopSelf();
            return;
        }

        startPrority();

        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        mySortedMap = new TreeMap<Long, UsageStats>();

        mAppLockDao = new AppLockDao(this);
        mList = mAppLockDao.queryAllLockPckName();
        observeUri();

        //定时监测堆栈的变化
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                //获取堆栈信息
                String topPackageName = "";

                if (Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP){

                    long time = System.currentTimeMillis();
                    //time - 1000 * 10, time 开始时间和结束时间的设置，在这个时间范围内 获取栈顶Activity 有效
                    List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);//获取10s前到现在期间的堆栈信息
                    // Sort the stats by the last time used
                    if (stats != null) {
                        mySortedMap.clear();
                        for (UsageStats us : stats) {
                            mySortedMap.put(us.getLastTimeUsed(), us);
                        }
                        if (mySortedMap != null && !mySortedMap.isEmpty()) {

                            topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();

                        }
                    }


                }else {

                    //以下方法在高版本中是可以获取到本包的堆栈信息的，但获取不到第三方APP的堆栈信息
                    runningTasks = mActivityManager.getRunningTasks(5);//获取前面5个运行的堆栈信息
                    //获取最前面运行的那个堆栈
                    mRunningTaskInfo = runningTasks.get(0);
                    //获取到顶部的activity
                    mTopActivity = mRunningTaskInfo.topActivity;
                    String className = mTopActivity.getClassName();
                    Log.d("tagtag","className:"+className);
                    topPackageName = mTopActivity.getPackageName();
                }

                if (!TextUtils.isEmpty(WHITEPACKENAM) && !WHITEPACKENAM.equals(topPackageName)){
                    WHITEPACKENAM = null;
                }

                Log.d("tagtag","topPackageName:"+topPackageName);
                //判断包名是不是在加锁数据库列表里
                if (mList !=null&& mList.contains(topPackageName)){
                    //加锁了
                    //弹出密码输入页面
                    if (topPackageName.equals(WHITEPACKENAM)){
                        //密码正确，放行
                        return;
                    }
                    Intent intent = new Intent(WatchDog2Service.this, LockEntryPassWordActivity.class);
                    intent.putExtra(MyConstants.PACKAGENAME,topPackageName);
                    startActivity(intent);

                }

            }

        };
        mTimer.schedule(task,0,400);
    }

    private void observeUri() {
        ContentObserver unLockObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Toast.makeText(WatchDog2Service.this,"unLockObserver",Toast.LENGTH_SHORT).show();
                mList = mAppLockDao.queryAllLockPckName();
            }
        };
        getContentResolver().registerContentObserver(AppLockDb.URI_UNLOCK,true,unLockObserver);

        ContentObserver lockObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                Toast.makeText(WatchDog2Service.this,"lockObserver",Toast.LENGTH_SHORT).show();
                mList = mAppLockDao.queryAllLockPckName();
            }
        };
        getContentResolver().registerContentObserver(AppLockDb.URI_LOCK,true,lockObserver);

    }

    private boolean checkUseStage() {
        if (!AppInfoUtil.isUsagestatsGranted(this)){
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        Log.d("tagtag","WatchDog2Service:onDestroy>>>>>>>>");
        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
        WHITEPACKENAM = null;
        super.onDestroy();
    }
}
