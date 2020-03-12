package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.kzy.mobilesafe.R;
import com.kzy.mobilesafe.adapter.ContactsAdapter;
import com.kzy.mobilesafe.bean.ContactorBean;
import com.kzy.mobilesafe.utils.ContactUtil;

import java.util.List;

/**
 * 展示通讯录信息
 */
public class ContactListActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private ContactsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        ListView lv_contacts = findViewById(R.id.lv_contacts);
        mAdapter = new ContactsAdapter(this);
        lv_contacts.setAdapter(mAdapter);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载数据");
        mProgressDialog.setCancelable(false);
        getContacts();
    }

    private void getContacts() {

        mProgressDialog.show();

        ContactUtil.getContactors(this, new ContactUtil.OnContactResultListener() {
            @Override
            public void contactResult(final List<ContactorBean> contactorBeans) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        if (contactorBeans!=null){

                            Log.d("tagtag","size:"+contactorBeans.size());

                            mAdapter.setData(contactorBeans);

                        }
                    }
                });
            }
        });
    }


}
