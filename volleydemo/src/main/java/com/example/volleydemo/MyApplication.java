package com.example.volleydemo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 创建日期：2017/10/23 on 下午5:49
 * 描述:使用volley前，首先要建立一个volley的全局请求队列，用来管理请求。
 * 在Application类中创建全局请求队列，在onCreate()方法中实例化请求队列，为请求队列设置get方法。
 * 作者:yangliang
 */
public class MyApplication extends Application {

    //Volley的全局请求队列
    public static RequestQueue sRequestQueue;

    /**
     * Volley全局请求队列
     */
    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //实例化Volley全局请求队列
        sRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
}
