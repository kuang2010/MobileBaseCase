package com.kzy.mobilesafe.activity.sjfd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kzy.mobilesafe.Constant.MyConstants;
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
        final ListView lv_contacts = findViewById(R.id.lv_contacts);
        mAdapter = new ContactsAdapter(this);
        lv_contacts.setAdapter(mAdapter);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载数据");
        mProgressDialog.setCancelable(false);
        getContacts();
        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactorBean contactorBean = (ContactorBean) lv_contacts.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(MyConstants.CONTACTERNAME,contactorBean.getName());
                intent.putExtra(MyConstants.CONTACTERPHONE,contactorBean.getPhone());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
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
