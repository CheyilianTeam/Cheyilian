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

        swDeviceOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    JSONObject message = new JSONObject();
                    message.put(Util.CONTROL_COMMAND, Util.COMMAND_ON);
                    CarStateActivity.sendBleMessage(message.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        swDeviceOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    JSONObject message = new JSONObject();
                    message.put(Util.CONTROL_COMMAND, Util.COMMAND_OFF);
                    CarStateActivity.sendBleMessage(message.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        swAutoControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (cbRotatingSpeedControl.isChecked() || cbCarSpeedControl.isChecked() || cbThrottlingValveControl.isChecked()) {
                        try {
                            if (cbRotatingSpeedControl.isChecked()) {
                                String targetRotatingSpeed = etRotatingSpeed.getText().toString();
                                Util.savePreference(Util.TARGET_ROTATING_SPEED, targetRotatingSpeed);
                                JSONObject message = new JSONObject();
                                message.put(Util.TRANSFER_DATA, Util.ROTATING_SPEED_CONTROL);
                                message.put(Util.VALUE_TRANSFERED, targetRotatingSpeed);
                                CarStateActivity.sendBleMessage(message.toString());
                            }
                            if (cbCarSpeedControl.isChecked()) {
                                String targetCarSpeed = etCarSpeed.getText().toString();
                                Util.savePreference(Util.TARGET_CAR_SPEED, targetCarSpeed);
                                JSONObject message = new JSONObject();
                                message.put(Util.TRANSFER_DATA, Util.CAR_SPEED_CONTROL);
                                message.put(Util.VALUE_TRANSFERED, targetCarSpeed);
                                CarStateActivity.sendBleMessage(message.toString());
                            }
                            if (cbThrottlingValveControl.isChecked()) {
                                String targetThrottlingValve = etThrottlingValve.getText().toString();
                                Util.savePreference(Util.TARGET_THROTTLING_VALUE, targetThrottlingValve);
                                JSONObject message = new JSONObject();
                                message.put(Util.TRANSFER_DATA, Util.THROTTLING_VALUE_CONTROL);
                                message.put(Util.VALUE_TRANSFERED, targetThrottlingValve);
                                CarStateActivity.sendBleMessage(message.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CheyilianApplication.getContext(), "请选择控制模式", Toast.LENGTH_SHORT).show();
                        swAutoControl.setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        txtConnectionState.setText(Util.getPreference(Util.CONNECTION_STATE));
    }
}
