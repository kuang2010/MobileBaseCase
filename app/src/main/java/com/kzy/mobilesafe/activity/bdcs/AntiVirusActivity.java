package com.kzy.mobilesafe.activity.bdcs;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.AntiVirusAdapter;
import com.kzy.mobilesafe.bean.AppInfoBean;
import com.kzy.mobilesafe.dao.VirusDao;
import com.kzy.mobilesafe.utils.AppInfoUtil;
import com.kzy.mobilesafe.utils.Md5Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class AntiVirusActivity extends Activity {

    private LinearLayout mLl_scanning_progress_antivirus;
    private ArcProgress mCpb_progress_antivirus;
    private TextView mTv_scanning_antivirus;
    private LinearLayout mLl_result_scan_antivirus;
    private TextView mTv_result_scan_antivirus;
    private Button mBtn_result_scan_result;
    private LinearLayout mLl_animation_antivirus;
    private ImageView mIv_left_animation_antivirus;
    private ImageView mIv_right_animation_antivirus;
    private final  int STARTSCAN = 1;
    private final  int SCANNING = 2;
    private final int SCANFINISH = 3;

    private int scanProgress;//扫描进度
    private List<AppInfoBean> mScanAppInfos = new ArrayList<>();
    private boolean haveVirus;
    private AnimatorSet set_open = null;
    private boolean interruptScaning;
    private AnimatorSet mSet_rescan;
    private RecyclerView mRcv_scan_app_antivirus;
    private AntiVirusAdapter mAntiVirusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        initView();
//        startScan();
        checkVersion();
        initEvent();
    }

    private void checkVersion() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载数据");
        dialog.show();
        RequestParams params = new RequestParams(getResources().getString(R.string.virus_version_url));
        params.setConnectTimeout(5000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("tagtag","result:"+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final int version = jsonObject.optInt("version");
                    int virusVersion = VirusDao.getVirusVersion();
                    if (version != virusVersion){
                        //有新病毒
                        Log.d("tagtag","有新病毒:");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AntiVirusActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("病毒库发现新的病毒，是否更新");
                        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startScan();
                            }
                        });
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getNewVirusData(version);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        //没新病毒
                        Log.d("tagtag","没新病毒");
                        startScan();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tagtag","JSONException");
                    startScan();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("tagtag","onError:"+ex.getMessage());
                startScan();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("tagtag","onCancelled");
                startScan();
            }

            @Override
            public void onFinished() {
                Log.d("tagtag","onFinished");
                dialog.dismiss();
            }
        });
    }

    private void getNewVirusData(final int version) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在加载数据");
        dialog.show();
        RequestParams params = new RequestParams(getResources().getString(R.string.virus_update_url));
        params.setConnectTimeout(5000);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("tagtag","result2:"+result);
                try {
                    JSONObject object = new JSONObject(result);
                    String md5 = object.optString("md5");
                    int type = object.optInt("type");
                    String name = object.optString("name");
                    String desc = object.optString("desc");
                    VirusDao.insertNewVirus(md5,type,name,desc);
                    VirusDao.updateVirusVersion(version);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startScan();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                startScan();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                startScan();
            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    private void initEvent() {
        mBtn_result_scan_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新扫描
                reScanAnimation();
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case STARTSCAN://开始扫描
                    mLl_scanning_progress_antivirus.setVisibility(View.VISIBLE);
                    mLl_result_scan_antivirus.setVisibility(View.GONE);
                    mLl_animation_antivirus.setVisibility(View.GONE);
                    mScanAppInfos.clear();
                    break;
                case SCANNING://正在扫描中
                    mCpb_progress_antivirus.setProgress(scanProgress);
                    AppInfoBean obj = (AppInfoBean) msg.obj;
                    String appName = obj.getAppName();
                    mTv_scanning_antivirus.setText("正在扫描:"+appName);
                    mScanAppInfos.add(0,obj);
                    mAntiVirusAdapter.setData(mScanAppInfos);
                    break;
                case SCANFINISH://扫描完成
                    mLl_scanning_progress_antivirus.setVisibility(View.GONE);
                    mLl_animation_antivirus.setVisibility(View.VISIBLE);
                    mLl_result_scan_antivirus.setVisibility(View.VISIBLE);
                    if (haveVirus){
                        mTv_result_scan_antivirus.setText("手机发现病毒");
                        mTv_result_scan_antivirus.setTextColor(Color.RED);
//                        mRcv_scan_app_antivirus.scrollToPosition();
                    }else {
                        mTv_result_scan_antivirus.setText("您的手机没病毒，请放心使用");
                    }
                    startFinishAnimation();
                    break;
            }
        }

    };

    /**
     * 播放扫描完成的动画
     */
    private void startFinishAnimation() {
        if (set_open == null){
            set_open = initFinishAnimator();
        }
        set_open.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mBtn_result_scan_result.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mBtn_result_scan_result.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set_open.start();

    }

    @NonNull
    private AnimatorSet initFinishAnimator() {
        //动画开始前要给iv先附上半边图片
        drawLeftIv(mLl_scanning_progress_antivirus,mIv_left_animation_antivirus);
        drawRightIv(mLl_scanning_progress_antivirus,mIv_right_animation_antivirus);

        Log.d("tagtag","width1:"+mIv_left_animation_antivirus.getWidth());
        mIv_left_animation_antivirus.measure(0,0);
        int width = mIv_left_animation_antivirus.getMeasuredWidth();
        Log.d("tagtag","width2:"+width);
        ObjectAnimator left_trans = ObjectAnimator.ofFloat(
                mIv_left_animation_antivirus,
                "translationX",
                0f,-width
        );

        ObjectAnimator left_alpha = ObjectAnimator.ofFloat(
                mIv_left_animation_antivirus,
                "alpha",
                new float[]{1.0f,0f}
        );

        ObjectAnimator right_trans = ObjectAnimator.ofFloat(
                mIv_right_animation_antivirus,
                "translationX",
                new float[]{0f,width}
        );
        ObjectAnimator right_alpha = ObjectAnimator.ofFloat(
                mIv_right_animation_antivirus,
                "alpha",
                new float[]{1.0f,0f}
        );

        ObjectAnimator result_alpha = ObjectAnimator.ofFloat(
                mLl_result_scan_antivirus,
                "alpha",
                new float[]{0f,1.0f}
        );


        AnimatorSet set_open = new AnimatorSet();
        set_open.setDuration(2000);
        set_open.playTogether(left_trans,left_alpha,right_trans,right_alpha,result_alpha);
        return set_open;
    }


    /**
     * 播放重新扫描的动画
     */
    private void reScanAnimation() {
        if (mSet_rescan==null){
            mSet_rescan = initReScanAnimator();
            mSet_rescan.removeAllListeners();
            mSet_rescan.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startScan();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        mSet_rescan.start();
    }

    private AnimatorSet initReScanAnimator() {
        mIv_left_animation_antivirus.measure(0,0);
        int width = mIv_left_animation_antivirus.getMeasuredWidth();
        ObjectAnimator left_trans = ObjectAnimator.ofFloat(
                mIv_left_animation_antivirus,
                "translationX",
                -width, 0f
        );

        ObjectAnimator left_alpha = ObjectAnimator.ofFloat(
                mIv_left_animation_antivirus,
                "alpha",
                new float[]{0f,1.0f}
        );

        ObjectAnimator right_trans = ObjectAnimator.ofFloat(
                mIv_right_animation_antivirus,
                "translationX",
                new float[]{width,0f}
        );
        ObjectAnimator right_alpha = ObjectAnimator.ofFloat(
                mIv_right_animation_antivirus,
                "alpha",
                new float[]{0f,1.0f}
        );

        ObjectAnimator result_alpha = ObjectAnimator.ofFloat(
                mLl_result_scan_antivirus,
                "alpha",
                new float[]{1.0f,0f}
        );

        AnimatorSet set_open = new AnimatorSet();
        set_open.setDuration(2000);
        set_open.playTogether(left_trans,left_alpha,right_trans,right_alpha,result_alpha);
        return set_open;
    }


    /**
     * 拍照
     * 将ll_scanning_progress_antivirus的内容右半部分画到iv_right_animation_antivirus上
     * @param ll_scanning_progress_antivirus
     * @param iv_right_animation_antivirus
     */
    private void drawRightIv(LinearLayout ll_scanning_progress_antivirus, ImageView iv_right_animation_antivirus) {
        Log.d("tagtag","ll_scanning_progress_antivirus:"+ll_scanning_progress_antivirus.getWidth());
        ll_scanning_progress_antivirus.setDrawingCacheEnabled(true);
        ll_scanning_progress_antivirus.setDrawingCacheQuality(LinearLayout.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap_src = ll_scanning_progress_antivirus.getDrawingCache();
        Log.d("tagtag","bitmap_src:"+bitmap_src.getWidth());
        Bitmap bitmap = Bitmap.createBitmap(bitmap_src.getWidth()/2, bitmap_src.getHeight(), bitmap_src.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.postTranslate(-bitmap_src.getWidth()/2,0);
        canvas.drawBitmap(bitmap_src, matrix, new Paint());
        iv_right_animation_antivirus.setImageBitmap(bitmap);
        ll_scanning_progress_antivirus.setDrawingCacheEnabled(false);
    }


    /**
     * 拍照
     * 将ll_scanning_progress_antivirus的内容左半部分画到iv_left_animation_antivirus上
     * @param ll_scanning_progress_antivirus
     * @param iv_left_animation_antivirus
     */
    private void drawLeftIv(LinearLayout ll_scanning_progress_antivirus, ImageView iv_left_animation_antivirus) {
        ll_scanning_progress_antivirus.setDrawingCacheEnabled(true);
        ll_scanning_progress_antivirus.setDrawingCacheQuality(LinearLayout.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap_src = ll_scanning_progress_antivirus.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(bitmap_src.getWidth()/2, bitmap_src.getHeight(), bitmap_src.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap_src,new Matrix(), new Paint());
        iv_left_animation_antivirus.setImageBitmap(bitmap);
        ll_scanning_progress_antivirus.setDrawingCacheEnabled(false);
    }

    //开始扫描
    private void startScan() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mHandler.obtainMessage(STARTSCAN).sendToTarget();
                List<AppInfoBean> allAppsInfos = AppInfoUtil.getAllAppsInfos(AntiVirusActivity.this);
                int count=0;
                for (AppInfoBean appInfoBean:allAppsInfos){
                    if (interruptScaning) {
                        return;
                    }
                    count++;
                    scanProgress = Math.round(count*100.0f/allAppsInfos.size());

                    String fileMd5 = Md5Util.getFileMd5(appInfoBean.getInstallPath());
                    Log.d("fileMD5",appInfoBean.getAppName()+":"+fileMd5);
                    boolean virus = VirusDao.isVirus(fileMd5);
                    if (virus){
                        haveVirus = true;
                    }
                    appInfoBean.setVirus(virus);
                    Message message = mHandler.obtainMessage(SCANNING);
                    message.obj = appInfoBean;
                    message.sendToTarget();
                    SystemClock.sleep(200);
                }
                mHandler.obtainMessage(SCANFINISH).sendToTarget();
            }
        }.start();
    }

    private void initView() {
        mLl_scanning_progress_antivirus = findViewById(R.id.ll_scanning_progress_antivirus);
        mCpb_progress_antivirus = findViewById(R.id.cpb_progress_antivirus);
        mTv_scanning_antivirus = findViewById(R.id.tv_scanning_antivirus);

        mLl_result_scan_antivirus = findViewById(R.id.ll_result_scan_antivirus);
        mTv_result_scan_antivirus = findViewById(R.id.tv_result_scan_antivirus);
        mBtn_result_scan_result = findViewById(R.id.btn_result_scan_result);

        mLl_animation_antivirus = findViewById(R.id.ll_animation_antivirus);
        mIv_left_animation_antivirus = findViewById(R.id.iv_left_animation_antivirus);
        mIv_right_animation_antivirus = findViewById(R.id.iv_right_animation_antivirus);

        mRcv_scan_app_antivirus = findViewById(R.id.rcv_scan_app_antivirus);
        mAntiVirusAdapter = new AntiVirusAdapter(this);
        mAntiVirusAdapter.setData(mScanAppInfos);
        mRcv_scan_app_antivirus.setAdapter(mAntiVirusAdapter);
        mRcv_scan_app_antivirus.setLayoutManager(new LinearLayoutManager(this));
        mRcv_scan_app_antivirus.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        interruptScaning = true;
        super.onDestroy();
    }
}
