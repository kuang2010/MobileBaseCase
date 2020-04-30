package com.kzy.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author: kuangzeyu2019
 * date: 2020/3/7
 * time: 23:20
 * desc: md5
 */
public class Md5Util {

    /**
     * md5加密
     * @param src
     * @return
     */
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


    /**
     * 获取文件的md5值
     * md5只跟内容有关
     * @param filePaht
     * @return
     */
    public static String getFileMd5(String filePaht){
        String res="";

        try {
            MessageDigest md = MessageDigest.getInstance("md5");

            FileInputStream is = new FileInputStream(new File(filePaht));

            byte[] buf = new byte[1024 * 10];

            int len = 0;

            while ((len=is.read(buf))>=0){

                md.update(buf,0,len);

            }
            is.close();

            //文件读取完毕
            byte[] digest = md.digest();
            //以下是：将字节数组转成十六进制的字符串
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res.toUpperCase();
    }
}
