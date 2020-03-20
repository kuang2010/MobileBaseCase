package com.kzy.mobilesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.kzy.mobilesafe.bean.BlackBean;
import com.kzy.mobilesafe.db.BlackDao;

import org.junit.Test;
import org.junit.runner.RunWith;

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
        for (int i=0;i<100;i++){
            BlackBean blackBean = new BlackBean("110"+i,1);
            blackDao.insert(blackBean);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>");
    }
}
