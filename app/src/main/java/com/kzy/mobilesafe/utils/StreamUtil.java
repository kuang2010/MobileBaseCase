package com.kzy.mobilesafe.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author: kuangzeyu2019
 * date: 2020/2/29
 * time: 22:31
 * desc:
 */
public class StreamUtil {

    public static String convertToString(InputStream in) throws IOException {
        StringBuilder builder = new StringBuilder();
        String info = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while (!TextUtils.isEmpty(info=reader.readLine())){
            builder.append(info);
        }
        return builder.toString();
    }
}
