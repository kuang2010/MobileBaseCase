package com.kzy.mobilesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kzy.mobilesafe.bean.BlackBean;
import com.kzy.mobilesafe.dao.AppLockDao;
import com.kzy.mobilesafe.dao.BlackDao;
import com.kzy.mobilesafe.db.BlackDb;
import com.kzy.mobilesafe.dao.TelAddressDao;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.kzy.mobilesafe", appContext.getPackageName());
    }

    @Test
    public void MyTest1(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackDao blackDao = new BlackDao(appContext);
        for (int i=0;i<10;i++){
            BlackBean blackBean = new BlackBean("110"+i,BlackDb.MODE_PHONE);
            blackDao.insert(blackBean);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
    }

    @Test
    public void MyTest2(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        BlackDao blackDao = new BlackDao(appContext);
        List<BlackBean> list = blackDao.queryAll();
        System.out.println("list>>"+list.toString());
    }

    @Test
    public void MyTest3(){

        String location = TelAddressDao.getLocation("13148814853");

        System.out.println("location>>>>"+location);
    }


    @Test
    public void MyTest4(){
        String packName = "com.sohu.inputmethod.sogou.x86";
        Context context = InstrumentationRegistry.getTargetContext();
        AppLockDao appLockDao = new AppLockDao(context);
        appLockDao.insert(packName);

//        appLockDao.delete(packName);

        boolean b = appLockDao.queryIsLock(packName);
        System.out.println("boolean>>>>"+b);
    }
}
