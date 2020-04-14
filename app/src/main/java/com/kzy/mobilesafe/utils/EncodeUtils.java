package com.kzy.mobilesafe.utils;

import java.io.UnsupportedEncodingException;

/**
 * author: kuangzeyu2019
 * date: 2020/4/14
 * time: 22:40
 * desc:
 */
public class EncodeUtils {

    /**
     * 对字符串进行异或加解密（a^b^b=a）
     * @param en_de_str 源字符串或者加密后的字符串
     * @param seed 种子b
     * */
    public static String en_de_codeStr(String en_de_str,byte seed){
        try {
            byte[] bytes = en_de_str.getBytes("gbk");
            for (int i=0;i<bytes.length;i++){
                bytes[i] ^= seed;
            }
            return new String(bytes,"gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
