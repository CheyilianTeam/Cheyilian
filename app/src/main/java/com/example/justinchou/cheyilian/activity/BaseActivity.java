package com.example.justinchou.cheyilian.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.justinchou.cheyilian.model.Obd;
import com.example.justinchou.cheyilian.service.DBService;
import com.example.justinchou.cheyilian.util.ActivityCollector;
import com.example.justinchou.cheyilian.util.Util;

/**
 * Created by J on 2016/5/8.
 */
public class BaseActivity extends AppCompatActivity {

    DBService dbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbService = new DBService(this);
        ActivityCollector.addActivity(this);
        Log.i("BaseActivity", "add activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Write obd data to database
        Obd obd = new Obd();
        obd.setDeviceName(Util.getPreference(Util.DEVICE_NAME));
        obd.setDeviceNumber(Util.getPreference(Util.DEVICE_NUMBER));
        obd.setTargetRotatingSpeed(Util.getPreference(Util.TARGET_ROTATING_SPEED));
        obd.setTargetCarSpeed(Util.getPreference(Util.TARGET_CAR_SPEED));
        obd.setTargetThrottlingValue(Util.TARGET_THROTTLING_VALUE);
        dbService.save(obd);
        ActivityCollector.removeActivity(this);
        Log.i("BaseActivity", "remove activity");
    }
}
