package com.example.justinchou.cheyilian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.justinchou.cheyilian.util.ActivityCollector;

/**
 * Created by J on 2016/5/8.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Log.e("BaseActivity", "add activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        Log.e("BaseActivity", "remove activity");
    }
}
