package com.kzy.mobilesafe;


import com.kzy.mobilesafe.dao.TelAddressDao;

import org.junit.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1(){

        String kzy = md5Encode("13714785411");

        System.out.print("kzy:"+kzy);
    }

    public String md5Encode(String src){
        String res="";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(src.getBytes());
            for (byte dt : digest){
                //将字节或int数据转成十六进制表示
                //一个字节 8bit 可表示2位十六进制数
                //去掉一个int类型前3个字节
                int d = dt & 0x000000ff;
                String hex = Integer.toHexString(d);
                if (hex.length()==1){
                    hex = "0"+hex;
                }
                res = res+hex;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return res;
    }


    @Test
    public void test2(){
        boolean mobilePhone = TelAddressDao.isMobilePhone("13714785411");
        System.out.println("mobilePhone>>"+mobilePhone);
    }
}