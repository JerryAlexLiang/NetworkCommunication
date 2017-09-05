package com.example.httpclientcommunication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HttpClientGetActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_http_client_get);
        button = (Button) findViewById(R.id.btnGet);
        wView = (WebView) findViewById(R.id.wView);
        wView.getSettings().setDomStorageEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetByHttpClient();
            }
        });
    }

    private void GetByHttpClient() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(NET_URL);
//                    //如果是带有参数的GET请求的话，我们可以把参数放到List集合中，在对参数进行URL编码:然后和URL拼接
//                    List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
//                    params.add(new BasicNameValuePair("user", "猪小弟"));
//                    params.add(new BasicNameValuePair("pawd", "123"));
//                    String param = URLEncodedUtils.format(params, "UTF-8");
//                    HttpGet httpGet = new HttpGet("http://www.baidu.com" + "?" + param);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        detail = EntityUtils.toString(entity, "UTF-8");
                        handler.sendEmptyMessage(SHOW_DATA);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


}
