package com.example.xutilsbreakpointdownload;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Button buttonOne = (Button) findViewById(R.id.test_one);
        Button buttonTwo = (Button) findViewById(R.id.test_two);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.test_one:
                intent.setClass(ItemActivity.this,MainActivity.class);
                break;

            case R.id.test_two:
                intent.setClass(ItemActivity.this,Main2Activity.class);
                break;

            default:
                break;
        }
        startActivity(intent);
    }

    /**
     * 返回键退出应用(连按两次)
     *
     * @param keyCode
     * @param event
     * @return
     */
    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                Toast.makeText(ItemActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else if (KeyEvent.KEYCODE_HOME == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
