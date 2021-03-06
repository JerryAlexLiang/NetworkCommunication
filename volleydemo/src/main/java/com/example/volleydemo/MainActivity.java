package com.example.volleydemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonOne = (Button) findViewById(R.id.button_one);
        Button buttonTwo = (Button) findViewById(R.id.button_two);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_one:
                intent.setClass(MainActivity.this, RequestTestActivity.class);
                break;

            case R.id.button_two:
                intent.setClass(MainActivity.this, VolleyUtilActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
