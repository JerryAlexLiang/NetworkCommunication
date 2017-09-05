package com.example.httpclientcommunication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.http.client.HttpClient;

/**
 * 创建日期：2017/9/2 on 上午12:30
 * 描述: 在Android 6.0（API 23） 中，Google已经移除了Apache HttpClient 想关类，推荐使用HttpUrlConnection
 * 在此只是学习一下相关技术点
 * 作者: liangyang
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOne = (Button) findViewById(R.id.button_get);
        buttonTwo = (Button) findViewById(R.id.button_post);
        buttonThree = (Button) findViewById(R.id.button_get_com);
        buttonFour = (Button) findViewById(R.id.button_post_com);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
        buttonThree.setOnClickListener(this);
        buttonFour.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_get:
                intent.setClass(MainActivity.this, GetActivity.class);
                break;

            case R.id.button_post:
                intent.setClass(MainActivity.this, PostActivity.class);
                break;

            case R.id.button_get_com:
                intent.setClass(MainActivity.this, HttpClientGetActivity.class);
                break;

            case R.id.button_post_com:
                intent.setClass(MainActivity.this, HttpClientPostActivity.class);
                break;

        }
        startActivity(intent);
    }
}
