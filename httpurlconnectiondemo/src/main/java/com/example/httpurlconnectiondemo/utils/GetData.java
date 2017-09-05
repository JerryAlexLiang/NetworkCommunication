package com.example.httpurlconnectiondemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 创建日期：2017/9/2 on 下午10:30
 * 描述:获取数据类
 * 作者:yangliang
 */
public class GetData {

    //定义一个获取网络图片数据的方法(二维数组)
    public static byte[] getImage(String path) {
        InputStream inputStream = null;
        byte[] bytes = null;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接超时为5秒
            connection.setConnectTimeout(5000);
            //设置请求类型为Get类型
            connection.setRequestMethod("GET");
            //对响应吗进行判断，判断请求url是否成功
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("请求url失败");
            }
            //获取网络输入流
            inputStream = connection.getInputStream();
            bytes = StreamTool.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    //获取网页源代码
    public static String getHtmlCode(String path) {
        InputStream inputStream = null;
        String htmlCode = "";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                byte[] bytes = StreamTool.read(inputStream);
                htmlCode = new String(bytes, "UTF-8");
//                return htmlCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        return null;
        return htmlCode;
    }
}
