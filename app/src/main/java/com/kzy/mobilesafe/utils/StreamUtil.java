package com.kzy.mobilesafe.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
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


    /** 关闭流 */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }



    /**
     * 将流数据转成字符数据
     * @param in
     * @return
     */
    public static String convert2String(InputStream in) throws IOException {
        //底层流对拷，或BufferedReader都可以

        /*BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        StringBuilder builder = new StringBuilder();
        builder.append(line);
        while (!TextUtils.isEmpty(line)){
            String line1 = reader.readLine();
            builder.append(line1);

        }
        String result = builder.toString();*/

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len=in.read(buf))>0){
            out.write(buf,0,len);
        }

        if (out.toString().toLowerCase().contains("utf-8")){
            return out.toString("utf-8");
        }else if (out.toString().toLowerCase().contains("iso8859-1")){
            return out.toString("iso8859-1");
        }else if (out.toString().toLowerCase().contains("gbk")){
            return out.toString("gbk");
        }
        return out.toString();
    }
}
