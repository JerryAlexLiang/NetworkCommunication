package com.example.asynctaskdemo;

import android.app.ProgressDialog;
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

/**
 * 创建日期：2017/9/1 on 下午11:16
 * 描述:在程序中，有许多场合都需要执行一些耗时的任务，如读写网络数据、大量计算等，
 * 为了不让这些耗时的任务影响界面响应，就应当使用异步任务进行处理。
 * 作者: liangyang
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private TextView textViewOne;
//    public static final String DATA_URL = "https://www.baidu.com/";
    public static final String DATA_URL = "http://japi.juhe.cn/joke/content/list.from?key=fdf03ac4faf35b6463ad8bf843d215d9&page=2&pagesize=10&sort=asc&time=1418745237";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        textViewOne = (TextView) findViewById(R.id.textView);
        progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("执行进度：");
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                readURL(DATA_URL);
                break;

            default:
                break;
        }
    }

    private void readURL(String url) {

        /**
         * 注意：在AsyncTask方法中只进行耗时操作，不进行与主线程UI相关交互的动作，这些动作在其复写的回调方法中执行
         */
        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... params) {
                //执行后台操作
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                StringBuilder builder = null;
                try {
                    URL url = new URL(params[0]);
                    //获取互联网的连接
                    URLConnection urlConnection = url.openConnection();
                    //获取当前读取对象的全部长度
                    long total = urlConnection.getContentLength();
                    //获取网络输入流
                    inputStream = urlConnection.getInputStream();
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                    bufferedReader = new BufferedReader(inputStreamReader);
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    //开始读取数据
                    //用来存放读取的一行字符串信息
                    String line = null;
                    //用来存放和连接读取的数据
                    builder = new StringBuilder();
                    //循环读取
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                        //发布任务执行进度
                        long curProgress = builder.toString().length() / total;
                        publishProgress(Integer.valueOf((int) curProgress));
                    }
                    Log.d("tag", "doInBackground: " + builder.toString());
                    return builder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        //关闭流
                        bufferedReader.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            //复写方法

            /**
             * 在当AsyncTask执行完毕前会执行这个方法
             */
            @Override
            protected void onPreExecute() {
                Toast.makeText(MainActivity.this, "开始读取网络数据啦~", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                super.onPreExecute();
            }

            /**
             * 当AsyncTask执行完毕后会执行这个方法
             * @param s  doInBackground的返回值
             */
            @Override
            protected void onPostExecute(String s) {
                textViewOne.setText(s);
                progressDialog.dismiss();
                super.onPostExecute(s);
            }

            /**
             * 在AsyncTask执行过程中，对外发布执行进度
             * @param values 对应AsyncTask第二个参数
             */
            @Override
            protected void onProgressUpdate(Integer... values) {
                Log.e("tag", "onProgressUpdate: " + values[0]);
                progressDialog.setProgress(Integer.valueOf(String.valueOf(values[0])));
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
