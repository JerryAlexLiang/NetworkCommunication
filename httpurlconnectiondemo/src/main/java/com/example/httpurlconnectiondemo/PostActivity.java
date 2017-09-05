package com.example.httpurlconnectiondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpurlconnectiondemo.utils.PostLoginUtils;

/**
 * 创建日期：2017/9/5 on 上午10:29
 * 描述: 有GET自然有POST，我们通过openConnection获取到的HttpURLConnection默认是进行Get请求的,
 * 所以我们使用POST提交数据，
 * (1)、、应提前设置好相关的参数:conn.setRequestMethod(“POST”);
 * (2)、、conn.setDoOutput(true);conn.setDoInput(true);设置允许输入，输出
 * (3)、还有:conn.setUseCaches(false); POST方法不能缓存，要手动设置为false,
 * 作者: liangyang
 */
public class PostActivity extends AppCompatActivity {

    // 声明控件对象
    private EditText et_name, et_pass;
    // 声明显示返回数据库的控件对象
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // 通过 findViewById(id)方法获取用户名的控件对象
        et_name = (EditText) findViewById(R.id.et_name);
        // 通过 findViewById(id)方法获取用户密码的控件对象
        et_pass = (EditText) findViewById(R.id.et_pass);
        // 通过 findViewById(id)方法获取显示返回数据的控件对象
        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    /**
     * 通过android:onClick="login"指定的方法 ， 要求这个方法中接受你点击控件对象的参数v
     */
    public void login(View view) {
        // 获取点击控件的id
        int id = view.getId();
        // 根据id进行判断进行怎么样的处理
        switch (id) {
            // 登陆事件的处理
            case R.id.btn_login:
                // 获取用户名
                final String userName = et_name.getText().toString();
                // 获取用户密码
                final String userPass = et_pass.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPass)) {
                    Toast.makeText(this, "用户名或者密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    // 网络操作不能在主线程中进行，开启子线程
                    new Thread() {
                        public void run() {
                            // 调用loginByPost方法
                            final String result = PostLoginUtils.loginByPost(userName, userPass);
                            // 通过runOnUiThread方法进行修改主线程的控件内容
                            PostActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 在这里把返回的数据写在控件上
                                    tv_result.setText(result);
                                }
                            });
                        }
                    }.start();
                }
                break;
        }

    }
}
