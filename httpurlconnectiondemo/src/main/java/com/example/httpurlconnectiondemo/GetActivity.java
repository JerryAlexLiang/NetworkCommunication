package com.example.httpurlconnectiondemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpurlconnectiondemo.utils.GetData;

/**
 * 创建日期：2017/9/2 on 下午10:02
 * 描述: HttpURLConnection发送GET请求代码示例
 * 作者: liangyang
 */
public class GetActivity extends AppCompatActivity {

    private static final int GETPIC = 0x001;
    private static final int GETCODE = 0x002;
    private static final int TRANSWEB = 0x003;
    private TextView txtMenu, txtshow;
    private ImageView imgPic;
    private WebView webView;
    private ScrollView scroll;
    private Bitmap bitmap;
    private String detail = "";
    private final static String PIC_URL = "http://ww2.sinaimg.cn/large/7a8aed7bgw1evshgr5z3oj20hs0qo0vq.jpg";
    private final static String HTML_URL = "https://www.baidu.com/";
//    private final static String HTML_URL = "http://japi.juhe.cn/joke/content/list.from?key=fdf03ac4faf35b6463ad8bf843d215d9&page=2&pagesize=10&sort=asc&time=1418745237";

    //用于刷新界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETPIC:
                    //隐藏所有控件的方法
                    hideAllWidget();
                    //显示图片控件
                    imgPic.setVisibility(View.VISIBLE);
                    //更新图片
                    imgPic.setImageBitmap(bitmap);
                    Toast.makeText(GetActivity.this, "图片加载完毕~", Toast.LENGTH_SHORT).show();
                    break;

                case GETCODE:
                    hideAllWidget();
                    scroll.setVisibility(View.VISIBLE);
                    txtshow.setText(detail);
                    Toast.makeText(GetActivity.this, "HTML代码加载完毕", Toast.LENGTH_SHORT).show();
                    break;

                case TRANSWEB:
                    hideAllWidget();
                    webView.setVisibility(View.VISIBLE);
                    /*
                    加载html代码的使用的是webView的loadDataWithBaseURL而非LoadData，
                    如果用LoadData又要去纠结中文乱码的问题，so…用loadDataWithBaseURL就可以不用纠结那么多了
                     */
                    webView.loadDataWithBaseURL("", detail, "text/html", "UTF-8", "");
                    Toast.makeText(GetActivity.this, "网页加载完毕~", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        txtMenu = (TextView) findViewById(R.id.txtMenu);
        txtshow = (TextView) findViewById(R.id.txtshow);
        imgPic = (ImageView) findViewById(R.id.imgPic);
        webView = (WebView) findViewById(R.id.webView);
        scroll = (ScrollView) findViewById(R.id.scroll);
        //给View注册上下文菜单
        registerForContextMenu(txtMenu);

        WebSettings settings = webView.getSettings();
//
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setAppCacheEnabled(true);
        //WebView 默认不支持js，所以需要设置
        settings.setJavaScriptEnabled(true);
        //
        // setWebChromeClient(WebChromeClient client)
        webView.setWebChromeClient(new WebChromeClient());
    }

    // 定义一个隐藏所有控件的方法:
    private void hideAllWidget() {
        imgPic.setVisibility(View.GONE);
        scroll.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
    }

    /**
     * 重写上下文菜单的创建方法: 重写Activity或者Fragment中的onCreateContextMenu方法
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 上下文菜单被点击是触发该方法: 重写Activity或者Fragment中的onContextItemSelected方法，实现菜单事件监听
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                //网络请求操作，在子线程中进行
                getPicData();
                break;

            case R.id.two:
                //网络请求操作，在子线程中进行
                getHtmlCode();
                break;

            case R.id.three:
                //转换web页面
                transWeb();
                break;
        }
        return true;
    }

    /**
     * 准换web页面
     */
    private void transWeb() {

        if (detail.equals("")) {
            Toast.makeText(this, "先进行第二步，请求code代码~", Toast.LENGTH_SHORT).show();
        } else {
            //在主线程中更新UI，使用handler
            //mHandler.sendEmptyMessage(0x003);
            mHandler.sendEmptyMessage(TRANSWEB);
        }
    }

    /**
     * 获取网页代码数据
     */
    private void getHtmlCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                detail = GetData.getHtmlCode(HTML_URL);
                //在主线程中更新UI，使用handler
                //mHandler.sendEmptyMessage(0x002);
                mHandler.sendEmptyMessage(GETCODE);

            }
        }).start();

    }

    /**
     * 获取图片数据
     */
    private void getPicData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = GetData.getImage(PIC_URL);
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                //在主线程中更新UI，使用handler
                //mHandler.sendEmptyMessage(0x001);
                mHandler.sendEmptyMessage(GETPIC);
            }
        }).start();
    }
}
