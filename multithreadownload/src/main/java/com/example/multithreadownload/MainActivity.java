package com.example.multithreadownload;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;

import static android.R.attr.path;

/**
 * 创建日期：2017/10/10 on 下午6:04
 * 描述: 多线程下载,手机版断点续传
 * 作者: liangyang
 */
public class MainActivity extends AppCompatActivity {

    private String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";

    //线程数
    private int threadCount = 3;
    //所有线程下载总进度
    private int downloadProgress = 0;
    private int finishedThread = 0;

    //显示下载的进度
    private ProgressBar progressBar;

    private TextView textView;

    /**
     * 使用Handler更新UI界面
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            textView.setText((long) progressBar.getProgress() * 100 / progressBar.getMax() + "%");
            float temp = (float) progressBar.getProgress() * 100 / progressBar.getMax();
            textView.setText("下载进度： " + temp + "%");
            if (temp == 100) {
                Toast.makeText(MainActivity.this, "下载完成~", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.btn_click);
        progressBar = (ProgressBar) findViewById(R.id.download_progress);
        textView = (TextView) findViewById(R.id.tv);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDownload();
            }
        });

    }

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void doDownload() {

        //多线程下载
        Thread thread = new Thread() {
            @Override
            public void run() {
                //发送http请求，拿到目标文件长度
                try {
                    URL url = new URL(downloadUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);

                    if (urlConnection.getResponseCode() == 200) {
                        //获取下载文件总长度
                        int length = urlConnection.getContentLength();
                        //创建临时文件
                        File file = new File(Environment.getExternalStorageDirectory(), getNameFromPath(downloadUrl));
                        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                        //设置临时文件大小与目标文件一致
                        raf.setLength(length);
                        raf.close();

                        //设置进度条的最大值
                        progressBar.setMax(length);

                        //计算每个线程的下载区间长度
                        int size = length / threadCount;

                        //计算每个线程下载的开始位置和结束位置
                        for (int id = 0; id < threadCount; id++) {
                            int startIndex = id * size;
                            int endIndex = (id + 1) * size - 1;
                            if (id == threadCount - 1) {
                                endIndex = length - 1;
                            }
                            System.out.println("线程" + id + "  下载的区间：" + startIndex + " ~ " + endIndex);
                            //开启线程，按照计算出来的开始结束位置开始下载数据
                            new DownLoadThread(id, startIndex, endIndex).start();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //开启线程获取总的下载文件长度信息
        thread.start();
    }

    public String getNameFromPath(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    /**
     * 开启线程，按照计算出来的开始和结束位置开始下载数据
     */
    private class DownLoadThread extends Thread {

        int threadId;//线程id
        int startIndex;//开始指标
        int endIndex;//结束指标

        DownLoadThread(int threadId, int startIndex, int endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public void run() {
            try {
                File fileProgress = new File(Environment.getExternalStorageDirectory(), threadId + ".txt");
                int lastProgress = 0;
                if (fileProgress.exists()) {
                    //读取进度临时文件中的内容
                    FileInputStream fis = new FileInputStream(fileProgress);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    //得到上一次的下载进度
                    lastProgress = Integer.parseInt(br.readLine());
                    //改变下载的开始位置，上一次下载过的，这次就不再请求了
                    startIndex += lastProgress;
                    //关闭文件输入流
                    fis.close();

                    //将上一次下载的进度加到进度条进度中(所有线程的总进度数)
                    downloadProgress += lastProgress;
                    //发送消息，更新UI文本进度改变
                    handler.sendEmptyMessage(1);
                }
                //再次发送请求至下载地址，请求开始位置至结束位置的数据
                //发送HTTP请求，请求要下载的数据
                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                //向服务器请求部分数据，设置请求数据的区间
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);

                //请求部分数据，成功的响应码是206
                if (conn.getResponseCode() == 206) {
                    //获取文件网络输入流
                    InputStream is = conn.getInputStream();
                    //定义长度
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    //当前线程下载的总进度
                    int total = lastProgress;
                    File file = new File(Environment.getExternalStorageDirectory(), getNameFromPath(downloadUrl));
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    //设置写入的开始位置
                    raf.seek(startIndex);
                    //循环遍历每条线程下载进度
                    while ((len = is.read(bytes)) != -1) {
                        raf.write(bytes, 0, len);
                        total += len;
                        System.out.println("线程" + threadId + "下载了：" + total);

                        //创建一个临时文件，保存下载的进度,每次下载都把新的下载位置写入缓存文本文件
                        RandomAccessFile rafProgress = new RandomAccessFile(fileProgress, "rwd");
                        //每次下载1024个字节,就马上把1024写入进度临时文件
                        rafProgress.write((total + "").getBytes());
                        rafProgress.close();
                        //进度条需要显示三条线程的整体下载进度，所以三条线程每下载一次，就要把新下载的长度加入进度条
                        //定义一个int全局变量，记录三条线程的总下载长度
                        //每次下载len个长度的字节，马上把len加到下载进度中，让进度条能反应这len个长度的下载进度
                        //把当前线程本次下载的长度加到进度条里
                        downloadProgress += len;
                        progressBar.setProgress(downloadProgress);
                        //发送消息，让文本进度条改变
                        handler.sendEmptyMessage(1);
                    }
                    raf.close();
                    System.out.println("线程" + threadId + "下载完毕------------------");

                    //3条线程全部下载完毕，再去删除进度临时文件
                    finishedThread++;

                    synchronized (downloadUrl) {
                        if (finishedThread == threadCount) {
                            for (int i = 0; i < threadCount; i++) {
                                File f = new File(Environment.getExternalStorageDirectory(), i + ".txt");
                                f.delete();
                            }
                            finishedThread = 0;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
