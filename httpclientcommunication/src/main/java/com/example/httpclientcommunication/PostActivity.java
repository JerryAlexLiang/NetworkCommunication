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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.IllegalFormatCodePointException;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    //    private static final String DATA_URL = "http://v.juhe.cn/joke/randJoke.php?key=fdf03ac4faf35b6463ad8bf843d215d9&type=pic";
    private static final String DATA_URL = "http://v.juhe.cn/joke/randJoke.php";
    //POST向服务器传递的数据：?key=fdf03ac4faf35b6463ad8bf843d215d9&page=2&pagesize=10&sort=asc&time=1418745237
    public static final String PARAMETER = "key=fdf03ac4faf35b6463ad8bf843d215d9&type=pic";
    private Button button;
    private TextView textViewOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        button = (Button) findViewById(R.id.button);
        textViewOne = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                readURLByPost(DATA_URL);
                break;
        }
    }

    private void readURLByPost(String url) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                OutputStream outputStream = null;
                BufferedWriter bufferedWriter = null;
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //传递参数前要对HttpURLConnection进行配置后，才可以向服务器输出数据
//                    urlConnection.setDoInput(true);//默认
                    urlConnection.setDoOutput(true);
                    //设置POST模式
                    urlConnection.setRequestMethod("POST");

                    //获取网商输出流，向服务器写入参数
                    outputStream = urlConnection.getOutputStream();
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream,"utf-8");
//                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
                    bufferedWriter.write(PARAMETER);
                    bufferedWriter.flush();

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
                        bufferedReader.close();
                        inputStream.close();
                        bufferedWriter.close();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                Toast.makeText(PostActivity.this, "开始从服务器读取数据啦~", Toast.LENGTH_SHORT).show();
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
