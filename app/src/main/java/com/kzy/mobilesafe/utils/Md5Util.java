package com.kzy.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 23:20
 * desc: md5加密
 */
public class Md5Util {

    public static String md5Encode(String src){
        String res="";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] digest = md5.digest(src.getBytes());
            //以下是：将字节数组转成十六进制表示的字符串
            for (byte dt : digest){
                //将字节或int数据转成十六进制表示
                //String hex = Integer.toHexString(dt);//ffffffae,ffffff58,e3,5
                //一个字节 8bit 可表示2位十六进制数
                //去掉一个int类型前3个字节
                int d = dt & 0x000000ff;
                String hex = Integer.toHexString(d);//小的数据类型byte向大的数据类型int类型转换时系统会自动增加分配内存空间，所以要& 0x000000ff去掉多分配的三个字节空间
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
}
