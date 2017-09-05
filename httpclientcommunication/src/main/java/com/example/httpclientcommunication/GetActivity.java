package com.example.httpclientcommunication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DATA_URL = "http://japi.juhe.cn/joke/content/list.from?key=fdf03ac4faf35b6463ad8bf843d215d9&page=2&pagesize=10&sort=asc&time=1418745237";
    private Button button;
    private TextView textViewOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        button = (Button) findViewById(R.id.button);
        textViewOne = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                readURL(DATA_URL);
                break;
        }
    }

    private void readURL(String url) {

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(params[0]);
                    URLConnection urlConnection = url.openConnection();
                    inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                    String line = null;
                    StringBuilder builder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                        Log.d("tag", "doInBackground: " + builder.toString());
                    }
                    return builder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert bufferedReader != null;
                        bufferedReader.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                Toast.makeText(GetActivity.this, "开始从服务器读取数据啦~", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                textViewOne.setText(s);
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute(url);

    }
}
