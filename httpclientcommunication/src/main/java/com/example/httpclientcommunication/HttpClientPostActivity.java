package com.example.httpclientcommunication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpClientPostActivity extends AppCompatActivity {

    //暂时没找到Post的网站，又不想自己写个Servlet，So,直接贴核心代码吧~
    public static final String NET_URL = "http://www.w3cschool.cc/python/python-tutorial.html";
    public static final int SHOW_DATA = 0X123;
    private Button button;
    private WebView wView;
    private String detail;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_DATA) {
                wView.loadDataWithBaseURL("", detail, "text/html", "UTF-8", "");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_client_post);
        button = (Button) findViewById(R.id.btnGet);
        wView = (WebView) findViewById(R.id.wView);
        wView.getSettings().setDomStorageEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostByHttpClient();
            }
        });
    }

    private void PostByHttpClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(NET_URL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("user", "猪大哥"));
                    params.add(new BasicNameValuePair("pawd", "123"));
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
                    httpPost.setEntity(entity);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity2 = httpResponse.getEntity();
                        detail = EntityUtils.toString(entity2, "utf-8");
                        handler.sendEmptyMessage(SHOW_DATA);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
