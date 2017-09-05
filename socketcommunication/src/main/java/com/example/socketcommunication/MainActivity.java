package com.example.socketcommunication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ip;
    private EditText editText;
    private TextView text;
    private Button connectBtn;
    private Button sendBtn;
    private String ipAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ip = (EditText) findViewById(R.id.ip);
        editText = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.text);
        connectBtn = (Button) findViewById(R.id.connect);
        sendBtn = (Button) findViewById(R.id.send);
        connectBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        ipAd = ip.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect:
                connect();
                break;

            case R.id.send:
                send();
                break;

        }
    }

    private void send() {
        try {
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                text.append("我说：" + editText.getText().toString() + "\n");
                writer.write(editText.getText().toString() + "\n");
                //网络操作不能在主线程中进行
//            writer.flush();
                // 发送后将内容清空
                editText.setText(null);
            } else {
                Toast.makeText(this, "请输入内容~", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;


    private void connect() {
        // 使用线程进行数据的读写
        // 从网络读取数据
        /**
         * Params:传入值 Progress：中间过程值 Result：结果值
         */
        AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    socket = new Socket(ipAd, 12345);
                    // 从socket获取输入和输出流
                    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    publishProgress("@success,你已经连接到了本地服务器了");
                } catch (UnknownHostException e1) {
                    Toast.makeText(MainActivity.this, "无法建立链接",
                            Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                } catch (IOException e1) {
                    Toast.makeText(MainActivity.this, "无法建立链接",
                            Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                } finally {
                    try {
                        reader.close();
                        writer.close();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        publishProgress(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (values[0].equals("@success")) {
                    Toast.makeText(MainActivity.this, "连接成功~", Toast.LENGTH_SHORT).show();
                }
                text.append("别人说：" + values[0] + "\n");
                super.onProgressUpdate(values);
            }
        };

        read.execute();

    }
}
