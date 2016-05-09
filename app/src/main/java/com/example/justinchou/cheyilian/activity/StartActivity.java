package com.example.justinchou.cheyilian.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.justinchou.cheyilian.R;

/**
 * Created by Justin Chou on 2016/4/17.
 * Show the start view, 2 seconds.
 */
public class StartActivity extends BaseActivity {

    private Handler mHandler;
    private static final int DELAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, BindActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }
}
