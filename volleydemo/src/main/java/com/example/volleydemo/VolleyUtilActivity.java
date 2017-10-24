package com.example.volleydemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.volleydemo.utils.NetworkUtil;

import java.util.HashMap;
import java.util.Map;

public class VolleyUtilActivity extends AppCompatActivity {

    public static final String URL = "http://v.juhe.cn/postcode/query?postcode=210044&key=effd958a513778eadd21f8d29a675644";
    private static final int WEATHER_TAG = 1;
    private TextView textView;
    private String postcode = "210044";
    public static final String JUHE_APPKEY = "effd958a513778eadd21f8d29a675644";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_util);
        Button button = (Button) findViewById(R.id.test_button);
        textView = (TextView) findViewById(R.id.test_content);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> paramMap = new HashMap<>();
                paramMap.put("postcode", postcode);
                paramMap.put("key", JUHE_APPKEY);
                NetworkUtil.getInstance().post(VolleyUtilActivity.this, URL, paramMap, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case WEATHER_TAG:
                                Log.i("result", msg.obj.toString());
                                textView.setText(msg.obj.toString());
                                break;
                        }
                    }
                }, WEATHER_TAG);
            }
        });

    }
}
