package com.example.httpurlconnectiondemo.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * 创建日期：2017/9/5 on 上午10:35
 * 描述:HttpURLConnection发送POST请求代码示例---登录方法
 * 作者:yangliang
 */
public class PostLoginUtils {

    public static final String LOGIN_URL = "http://172.16.237.200:8080/MyServlet/servlet/com.liangyang.LoginServlet";

    public static String loginByPost(String number, String password) {
        String msg = "";
        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("POST");
            //设置请求超时信息
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            //设置允许输入输出（发送POST请求必须设置允许输入输出，setDoInput的默认值就是true  ）
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //POST方式不能缓存，所以需要手动设置为false
            connection.setUseCaches(false);
            //我们请求的数据
            String data = "password=" + URLEncoder.encode(password, "UTF-8")
                    + "&number=" + URLEncoder.encode(number, "UTF-8");
            //这里可以写一些请求头的东西
            /*
            // 设置请求的头
            connection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置请求的头
            connection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
            // 设置请求的头
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
             */
            //获取网络输出流对象,向服务器写入数据
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());
            //清除缓存
            outputStream.flush();
            //判断请求码
            if (connection.getResponseCode() == 200) {
                //获取服务器响应的网络输入流对象
                InputStream inputStream = connection.getInputStream();
                //创建字节输出流对象
                ByteArrayOutputStream message = new ByteArrayOutputStream();
                //定义读取的长度
                int len = 0;
                //定义缓冲区
                byte[] bytes = new byte[1024];
                //按照缓冲区的大小，循环读取
                while ((len = inputStream.read(bytes)) != -1) {
                    //根据读取的长度写入到outputStream对象中
                    message.write(bytes, 0, len);
                }
                //释放资源
                inputStream.close();
                message.close();
                //返回字符串(网页的二进制数据)
                msg = new String(message.toByteArray());
                return msg;

            } else {
                Log.d("tag", "loginByPost: " + "登录失败~");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return msg;
    }

}
