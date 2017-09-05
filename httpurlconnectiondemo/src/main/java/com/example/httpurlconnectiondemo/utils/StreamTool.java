package com.example.httpurlconnectiondemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 创建日期：2017/9/2 on 下午10:40
 * 描述:我们可以conn.getInputStream()获取到的是一个流，所以我们需要写一个类将流转化为二进制数组
 * 作者:yangliang
 */
public class StreamTool {
    //从网络输入流中读取数据输出
    public static byte[] read(InputStream inputStream) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
    }
}
