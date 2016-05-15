package com.example.justinchou.cheyilian.activity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justinchou.cheyilian.CheyilianApplication;
import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Justin Chou on 2016/4/17.
 * Set whether to connect the obd device, rotating speed, car speed, throttling valve.
 */
public class SettingActivity extends BaseActivity {

    @InjectView(R.id.txt_connection_state)
    TextView txtConnectionState;
    @InjectView(R.id.sw_device_on)
    Switch swDeviceOn;
    @InjectView(R.id.sw_device_off)
    Switch swDeviceOff;
    @InjectView(R.id.sw_auto_control)
    Switch swAutoControl;
    @InjectView(R.id.cb_rotating_speed_control)
    CheckBox cbRotatingSpeedControl;
    @InjectView(R.id.et_rotating_speed)
    EditText etRotatingSpeed;
    @InjectView(R.id.cb_car_speed_control)
    CheckBox cbCarSpeedControl;
    @InjectView(R.id.et_car_speed)
    EditText etCarSpeed;
    @InjectView(R.id.cb_throttling_valve_control)
    CheckBox cbThrottlingValveControl;
    @InjectView(R.id.et_throttling_valve)
    EditText etThrottlingValve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ButterKnife.inject(this);

        swAutoControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
