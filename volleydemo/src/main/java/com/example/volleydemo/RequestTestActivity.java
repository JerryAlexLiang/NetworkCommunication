package com.example.volleydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

/**
 * 创建日期：2017/10/24 on 上午10:19
 * 描述: 用于测试Volley各种方法的类的实例
 * 作者: liangyang
 */
public class RequestTestActivity extends AppCompatActivity {

    public static final String IMAGE_URL = "https://www.baidu.com/img/bd_logo1.png";

    private ImageView mRequestImage;
    private ImageView mLoaderImage;
    private NetworkImageView mNetworkImage;
    private VolleyRequestUtil volleyRequestUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_request);
        mRequestImage = (ImageView) findViewById(R.id.iv_image_request);
        mLoaderImage = (ImageView) findViewById(R.id.iv_image_loader);
        mNetworkImage = (NetworkImageView) findViewById(R.id.iv_image_network);
        Button button = (Button) findViewById(R.id.request_net_btn);

        //用于测试Volley各种方法的类的实例
        volleyRequestUtil = new VolleyRequestUtil();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //用于测试Volley各种方法的类的实例
//                VolleyRequestUtil volleyRequestUtil = new VolleyRequestUtil();

//                //测试Volley的StringRequest的GET请求
//                volleyRequestUtil.volleyStringRequestDemo_GET();

//                //测试Volley的StringRequest的POST请求
//                volleyRequestUtil.volleyStringRequestDome_POST();

//                //测试Volley的JsonObjectRequest的GET请求
//                volleyRequestUtil.volleyJsonObjectRequestDemo_GET();

//                //测试Volley的JsonObjectRequest的POST请求
//                volleyRequestUtil.volleyJsonObjectRequestDome_POST();

                //测试Volley的JsonArrayRequest的GET请求
                volleyRequestUtil.volleyJsonArrayRequestDemo_GET();

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        //当Activity停止运行后，取消activity的所有网络请求
        MyApplication.getRequestQueue().cancelAll(VolleyRequestUtil.VOLLEY_TAG);
        Log.i("tag", "cancel all:tag=" + VolleyRequestUtil.VOLLEY_TAG);
    }
}
