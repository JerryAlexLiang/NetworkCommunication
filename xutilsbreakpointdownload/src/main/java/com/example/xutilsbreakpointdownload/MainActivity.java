package com.example.xutilsbreakpointdownload;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 创建日期：2017/10/12 on 下午2:34
 * 描述: 使用开源框架xUtils - HttpUtils实现多线程断点续传下载
 * 作者: liangyang
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSuccess;
    private TextView tvFailure;
    private ProgressBar pb;
    private TextView tvProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        findViews();
    }

    /**
     * 初始化视图
     */
    private void findViews() {

        Button btnDownload = (Button) findViewById(R.id.btn_download);
        tvSuccess = (TextView) findViewById(R.id.tv_success);
        tvFailure = (TextView) findViewById(R.id.tv_failure);
        pb = (ProgressBar) findViewById(R.id.pb);
        tvProgress = (TextView) findViewById(R.id.tv_progress);

        btnDownload.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        HttpUtils utils = new HttpUtils();
        String target = Environment.getExternalStorageDirectory() + "/update.apk";
        String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
        utils.download(downloadUrl,  //目标文件链接
                target, //指定存储的路径和文件名
                true,//是否支持断点续传
                true,//如果响应头中包含文件名，下载完成后自动重命名
                new RequestCallBack<File>() {

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
                        tvFailure.setText(s);
                        System.out.println("onFailure: " + s);
                        Log.d("tag", "onFailure: " + s);
                    }

                    //下载过程中不断调用

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        pb.setMax((int) total);
                        pb.setProgress((int) current);
                        tvProgress.setText(current * 100 / total + "%");
                        System.out.println("onLoading: " + current);
                        if ((current * 100 / total) == 100) {
                            Toast.makeText(MainActivity.this, "下载成功~", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

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
