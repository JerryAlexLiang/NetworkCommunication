package com.example.threaddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 创建日期：2017/9/1 on 下午11:12
 * 描述:在程序中，有许多场合都需要执行一些耗时的任务，如读写网络数据、大量计算等，
 * 为了不让这些耗时的任务影响界面响应，就应当使用异步任务进行处理。
 * 作者: liangyang
 */
public class MainActivity extends AppCompatActivity {

    private Button buttonOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOne = (Button) findViewById(R.id.buttonOne);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    //Thread.sleep(1000)：休眠1秒：造成了主线程的阻塞
//                    Thread.sleep(1000);
//                    Log.d("tag", "onClick: " + "Tick");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                //为避免UI线程阻塞，造成页面卡顿的现象，耗时操作应该放在子线程中去执行，不能放在主线程中执行
                //使用线程可以使耗时操作并发执行
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //死循环
                            while (true) {
                                Thread.sleep(1000);
                                Log.d("tag", "onClick: " + "Tick");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                }).start();
            }
        });
    }
}
