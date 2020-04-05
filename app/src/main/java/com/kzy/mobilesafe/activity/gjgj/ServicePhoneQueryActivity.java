package com.kzy.mobilesafe.activity.gjgj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.MyExpendAdapter;
import com.kzy.mobilesafe.bean.ServicePhoneBean;
import com.kzy.mobilesafe.db.ServicePhoneDao;

/**
 * 服务号码查询页
 */
public class ServicePhoneQueryActivity extends Activity {

    private ExpandableListView mEpdLvServicePhone;
    private ServicePhoneBean mServicePhoneDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_phone_query);
        initView();
        initData();
        initEnvent();
    }

    private void initEnvent() {

        //组视图的点击事件
        mEpdLvServicePhone.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                showToastShort(mServicePhoneDatas.getFirstLayerDatas().get(groupPosition));
                return false;
            }
        });

        //子视图的点击事件
        mEpdLvServicePhone.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                showToastShort(mServicePhoneDatas.getSecondLayerDatas().get(groupPosition).get(childPosition).getName());
                callPhone(mServicePhoneDatas.getSecondLayerDatas().get(groupPosition).get(childPosition).getPhone());
                return true;
            }
        });

        //用于当组项折叠时的通知。
        mEpdLvServicePhone.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                showToastShort("折叠了组数据___"+mServicePhoneDatas.getFirstLayerDatas().get(groupPosition));
            }
        });

        //用于当组项折叠时的通知。
        mEpdLvServicePhone.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                showToastShort("展开了组数据___"+mServicePhoneDatas.getFirstLayerDatas().get(groupPosition));

                for (int i=0;i<mServicePhoneDatas.getFirstLayerDatas().size();i++){

                    if (i!=groupPosition){
                        mEpdLvServicePhone.collapseGroup(i);
                    }
                }
            }
        });
    }

    /**
     * 拨打电话
     * @param phone
     */
    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);//  ACTION_CALL  ACTION_DIAL
        intent.setData(Uri.parse("tel://"+phone));
        startActivity(intent);
    }

    private void initData() {

        mServicePhoneDatas = ServicePhoneDao.getServicePhoneDatas();

        MyExpendAdapter myAdapter = new MyExpendAdapter(this);

        myAdapter.setDatas(mServicePhoneDatas);

        mEpdLvServicePhone.setAdapter(myAdapter);

        //默认展开第一个数组
        mEpdLvServicePhone.expandGroup(0);
        //关闭数组某个数组，可以通过该属性来实现全部展开和只展开一个列表功能
        //mEpdLvServicePhone.collapseGroup(0);

    }

    private void initView() {

        View rootView = findViewById(android.R.id.content);

        mEpdLvServicePhone = rootView.findViewById(R.id.epd_lv_service_phone);

    }


    private void showToastShort(String mess){
        Log.d("showToastShort",mess);
    }
}
