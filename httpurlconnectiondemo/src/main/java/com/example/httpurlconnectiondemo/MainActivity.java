package com.example.httpurlconnectiondemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button getButton;
    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getButton = (Button) findViewById(R.id.button_get);
        postButton = (Button) findViewById(R.id.button_post);

        getButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.button_get:
                intent.setClass(MainActivity.this, GetActivity.class);
                break;

            case R.id.button_post:
                intent.setClass(MainActivity.this, PostActivity.class);
                break;
        }
        startActivity(intent);


    }
}
