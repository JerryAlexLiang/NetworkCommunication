package com.example.xutilsbreakpointdownload;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 创建日期：2017/10/12 on 下午5:26
 * 描述: 使用开源框架xUtils - HttpUtils实现多线程断点续传下载 -> 添加暂停功能
 * 断点续传,肯定少不了暂停下载和继续下载,做到这一点也很简单,只需要设置一个flag即可
 * 作者: liangyang
 */
public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private boolean isDownloading = false;

    private TextView tvSuccess;
    private TextView tvFailure;
    private ProgressBar pb;
    private TextView tvProgress;
    private HttpHandler<File> downloadHandler;
    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //初始化视图
        findViews();
    }

    /**
     * 初始化视图
     */
    private void findViews() {

        btnDownload = (Button) findViewById(R.id.btn_download);
        tvSuccess = (TextView) findViewById(R.id.tv_success);
        tvFailure = (TextView) findViewById(R.id.tv_failure);
        pb = (ProgressBar) findViewById(R.id.pb);
        tvProgress = (TextView) findViewById(R.id.tv_progress);

        btnDownload.setOnClickListener(this);

    }

    /**
     * 利用xUtils进行断点续传下载，并实现可暂停功能
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        HttpUtils utils = new HttpUtils();
        //如果想增加或减少下载文件时的线程数量,可以在HttpUtils初始化完毕后设置
//        utils.configRequestThreadPoolSize(5);//设置由几条线程进行下载
        String target = Environment.getExternalStorageDirectory() + "/update.apk";
        final String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";

        //没有下载时点击,进行下载
        if (!isDownloading) {
            isDownloading = true;
            btnDownload.setText("暂停下载");
            //第一个参数:下载地址
            //第二个参数:文件存储路径
            //第三个参数:是否断点续传
            //第四个参数:是否重命名
            //第五个参数:请求回调
            downloadHandler = utils.download(downloadUrl,  //目标文件链接
                    target, //指定存储的路径和文件名
                    true,//是否支持断点续传
                    true,//如果响应头中包含文件名，下载完成后自动重命名
                    new RequestCallBack<File>() {

                        //开始下载
                        @Override
                        public void onStart() {
                            super.onStart();
                            tvSuccess.setText("开始下载");
                            Toast.makeText(Main2Activity.this, "开始下载~", Toast.LENGTH_SHORT).show();
                        }

                        //下载完成后调用
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            tvSuccess.setText("下载完成，路径： " + responseInfo.result.getPath());
                            System.out.println("下载完成，路径： " + responseInfo.result.getPath());
                            Log.d("tag", "onSuccess: " + responseInfo.result.getPath());
                            //自动安装程序
                            openFile(responseInfo.result);
                        }

                        //下载失败时调用
                        @Override
                        public void onFailure(HttpException e, String s) {
                            System.out.println("onFailure: " + s);
                            Log.d("tag", "onFailure: " + s);
                            if (s.equals("maybe the file has downloaded completely")) {
                                Toast.makeText(Main2Activity.this, "安装包已下载~", Toast.LENGTH_SHORT).show();
                                tvFailure.setText("安装包已下载~");
                                pb.setProgress(100);
                                tvProgress.setText(100 + "%");
                            } else {
                                tvFailure.setText(s);
                            }
                        }

                        //下载过程中不断调用
                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            pb.setMax((int) total);
                            pb.setProgress((int) current);
                            tvProgress.setText(current * 100 / total + "%");
                            System.out.println("onLoading: " + current);
                            if ((current * 100 / total) == 100) {
                                Toast.makeText(Main2Activity.this, "下载成功~", Toast.LENGTH_SHORT).show();
                                btnDownload.setEnabled(false);
                                btnDownload.setText("下载完成");
                            }
                        }
                    }
            );
        } else {
            //正在下载时点击,则停止下载
            downloadHandler.cancel();
            isDownloading = false;
            btnDownload.setText("下载");
            tvSuccess.setText("你暂停了下载哦~");
        }
    }

    /**
     * 打开安装程序
     *
     * @param file
     */
    private void openFile(File file) {
        Log.d("tag", "openFile: " + file.getName());
        Log.d("tag", "openFile: " + file.getPath());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
