package com.kzy.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.kzy.mobilesafe.BuildConfig;
import com.kzy.mobilesafe.bean.ContactorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: kuangzeyu2019
 * date: 2020/3/12
 * time: 23:25
 * desc: 获取联系人信息
 */
public class ContactUtil {

    public static void getContactors(final Context context, final OnContactResultListener onContactResultListener){

        new Thread(){
            @Override
            public void run() {
                super.run();
                List<ContactorBean> contactorBeans = null;
                try{
                    Uri uri_rawContactId = Uri.parse("content://com.android.contacts/raw_contacts");
                    Uri uri_data = Uri.parse("content://com.android.contacts/data");
                    Uri uri_mimeType = Uri.parse("content://com.android.contacts");


                    ContentResolver resolver = context.getContentResolver();

                    Cursor cursor_rawContactId = resolver.query(uri_rawContactId, new String[]{"contact_id"}, null, null, null);

                    while (cursor_rawContactId.moveToNext()){

                        String id = cursor_rawContactId.getString(0);

                        if (!TextUtils.isEmpty(id)){

                            if (contactorBeans==null){
                                contactorBeans = new ArrayList<>();
                            }

                            ContactorBean contactorBean = new ContactorBean();

                            contactorBeans.add(contactorBean);

                            //data表里看着是mimetype_id字段，实则是mimetype字段！
                            Cursor cursor_data = resolver.query(uri_data, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{"" + id}, null);

                            while (cursor_data.moveToNext()){

                                String data1 = cursor_data.getString(0);

                                String mimetype = cursor_data.getString(1);

                                if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                                    //data是号码
                                    String phone = contactorBean.getPhone();

                                    if (TextUtils.isEmpty(phone)){

                                        contactorBean.setPhone(data1.replaceAll(" ","").replaceAll("-",""));

                                    }else {

                                        contactorBean.setPhone((phone+","+data1).replaceAll(" ","").replaceAll("-",""));
                                    }

                                }else if ("vnd.android.cursor.item/name".equals(mimetype)){
                                    //data是姓名
                                    contactorBean.setName(data1);
                                }

                            }

                            cursor_data.close();

                        }
                    }

                    cursor_rawContactId.close();

                    if (BuildConfig.DEBUG){
                        SystemClock.sleep(2000);
                    }

                }catch (Exception e){
                    e.printStackTrace();

                }finally {
                    onContactResultListener.contactResult(contactorBeans);
                }
            }
        }.start();

    }

    public interface OnContactResultListener{
        void contactResult(List<ContactorBean> contactorBeans);
    }


}
