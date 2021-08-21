package com.example.ksandroidplayerdemo.utils;

import org.json.JSONObject;

import java.io.*;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class HttpUtils {

    private static String PATH = "http://10.0.0.4:8080";

    public HttpUtils() {}

    static{

    }

    private static String is2String(InputStream inputStream,String encode) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if(inputStream != null){
            try {
                while((len = inputStream.read(data))!=-1){
                    data.toString();
                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(),encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPostRequest(Map<String,String> params, String encode,String api){
        URL url=null;
        try {
            url = new URL(PATH+api);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {//把请求的主体写入正文！！
        StringBuilder data=new StringBuilder();
            if(params!=null&&!params.isEmpty()){
                for(Map.Entry<String, String >entry :params.entrySet()){
                    data.append(entry.getKey()).append("=");
                    data.append(URLEncoder.encode(entry.getValue(),encode));
                    data.append("&");
                }
                data.deleteCharAt(data.length()-1);
            }
            byte[]entity=data.toString().getBytes();
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",String.valueOf(entity.length));
            OutputStream outStream =conn.getOutputStream();
            outStream.write(entity);
            /*
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");//设置参数类型是json格式
            connection.connect();
            OutputStream writer = connection.getOutputStream();
            Writer osw= new OutputStreamWriter(writer);

            osw.write(body.);
            System.out.println(body.toString());
            osw.close();*/
            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                String result = is2String(inputStream, "UTF-8");//将流转换为字符串。
                return result;
            }
            }
         catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Failed";
    }
    public static String sendGetRequest(Map<String, String> params,String ecoding,String api) {
        try {

            StringBuilder Path = new StringBuilder(PATH + api);
            Path.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                Path.append(entry.getKey()).append("=");
                Path.append(URLEncoder.encode(entry.getValue(), ecoding));
                Path.append("&");
            }
            Path.deleteCharAt(Path.length() - 1);
            HttpURLConnection conn = (HttpURLConnection) new URL(Path.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                String result = is2String(inputStream, "UTF-8");//将流转换为字符串。
                return result;
            }
        }
         catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        return "Failed";
    }
}
