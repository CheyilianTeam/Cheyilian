package com.example.justinchou.cheyilian.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.justinchou.cheyilian.R;

/**
 * Created by Justin Chou on 2016/4/17.
 * Set whether to connect the obd device, rotating speed, car speed, throttling valve.
 */
public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
    }
}
