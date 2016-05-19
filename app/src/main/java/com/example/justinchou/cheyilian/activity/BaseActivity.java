package com.example.justinchou.cheyilian.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.justinchou.cheyilian.service.DBService;
import com.example.justinchou.cheyilian.util.ActivityCollector;

/**
 * Created by J on 2016/5/8.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Log.i("BaseActivity", "add activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Write obd data to database
        DBService dbService = new DBService(this);

        ActivityCollector.removeActivity(this);
        Log.i("BaseActivity", "remove activity");
    }
}
