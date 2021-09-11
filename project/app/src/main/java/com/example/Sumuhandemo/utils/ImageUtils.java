package com.example.Sumuhandemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
    public static byte[] downImage(String U) {
        try {
            //创建url连接
            URL url = new URL(U);
            // 拿到HTTP连接对象
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // HTTP请求方式
            connection.setRequestMethod("GET");
            // 获得响应状态码
            int code = connection.getResponseCode();
            if (code == 200) {// 表示获取成功
                // 拿到输入流，用于读取响应的内容
                InputStream is = connection.getInputStream();
                // 输出流用于写数据
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                // 设置缓存数组
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    byteArrayOut.write(buffer, 0, len);
                }
                return byteArrayOut.toByteArray();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
