package com.example.volleydemo.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 创建日期：2017/10/24 on 下午3:21
 * 描述:新建RequestQueueUtil工具类， 单例获取RequestQueue对象
 * 作者:yangliang
 */
public class RequestQueueUtil {

    private static RequestQueue sRequestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        //单例获取Request对象
        if (sRequestQueue == null) {
            synchronized (RequestQueue.class) {
                if (sRequestQueue == null) {
                    sRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
        return sRequestQueue;
    }
}
