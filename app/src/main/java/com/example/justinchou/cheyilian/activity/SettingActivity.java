package com.example.justinchou.cheyilian.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justinchou.cheyilian.CheyilianApplication;
import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.model.Obd;
import com.example.justinchou.cheyilian.service.BluetoothLeService;
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

    public static final String DEVICE_NUMBER = "device_number";

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

    private static final int AUTO_CONTROL_SUCCESS = 1;
    private static final int SEND_TIME_DELAY = 500;
    private Handler mHandler;
    private String deviceNumber;

    private final BroadcastReceiver mConnectionStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Util.savePreference(Util.CONNECTION_STATE, Util.STATE_CONNECTED);
                txtConnectionState.setText("已连接");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Util.savePreference(Util.CONNECTION_STATE, Util.STATE_DISCONNECTED);
                txtConnectionState.setText("未连接");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        ButterKnife.inject(this);

        mHandler = new Handler();

        // Get data from the database
        Obd obd = dbService.find(Util.getPreference(Util.DEVICE_NUMBER));
        if (obd != null) {
            Util.savePreference(Util.TARGET_ROTATING_SPEED, obd.getTargetRotatingSpeed());
            Util.savePreference(Util.TARGET_CAR_SPEED, obd.getTargetCarSpeed());
            Util.savePreference(Util.TARGET_THROTTLING_VALUE, obd.getTargetThrottlingValue());
        }

        etRotatingSpeed.setText(Util.getPreference(Util.TARGET_ROTATING_SPEED));
        etCarSpeed.setText(Util.getPreference(Util.TARGET_CAR_SPEED));
        etThrottlingValve.setText(Util.getPreference(Util.TARGET_THROTTLING_VALUE));

        swDeviceOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    JSONObject message = new JSONObject();
                    message.put(Util.CONTROL_COMMAND, Util.COMMAND_ON);
                    MyTabActivity.sendBleMessage(message.toString());
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
                    MyTabActivity.sendBleMessage(message.toString());
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
                                message.put(Util.VALUE_TRANSFERED, Double.parseDouble(targetRotatingSpeed));
                                MyTabActivity.sendBleMessage(message.toString());
                            }
                            if (cbCarSpeedControl.isChecked()) {
                                String targetCarSpeed = etCarSpeed.getText().toString();
                                Util.savePreference(Util.TARGET_CAR_SPEED, targetCarSpeed);
                                final JSONObject message = new JSONObject();
                                message.put(Util.TRANSFER_DATA, Util.CAR_SPEED_CONTROL);
                                message.put(Util.VALUE_TRANSFERED, Double.parseDouble(targetCarSpeed));
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyTabActivity.sendBleMessage(message.toString());

                                    }
                                }, SEND_TIME_DELAY);
                            }
                            if (cbThrottlingValveControl.isChecked()) {
                                String targetThrottlingValve = etThrottlingValve.getText().toString();
                                Util.savePreference(Util.TARGET_THROTTLING_VALUE, targetThrottlingValve);
                                final JSONObject message = new JSONObject();
                                message.put(Util.TRANSFER_DATA, Util.THROTTLING_VALUE_CONTROL);
                                message.put(Util.VALUE_TRANSFERED, Double.parseDouble(targetThrottlingValve));
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyTabActivity.sendBleMessage(message.toString());
                                    }
                                }, SEND_TIME_DELAY * 2);
                            }
                            cbRotatingSpeedControl.setEnabled(false);
                            cbCarSpeedControl.setEnabled(false);
                            cbThrottlingValveControl.setEnabled(false);
                            etRotatingSpeed.setEnabled(false);
                            etCarSpeed.setEnabled(false);
                            etThrottlingValve.setEnabled(false);
                            Toast.makeText(CheyilianApplication.getContext(), "自动控制设置成功", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(CheyilianApplication.getContext(), "请选择控制模式", Toast.LENGTH_SHORT).show();
                        swAutoControl.setChecked(false);
                    }
                } else {
                    cbRotatingSpeedControl.setEnabled(true);
                    cbCarSpeedControl.setEnabled(true);
                    cbThrottlingValveControl.setEnabled(true);
                    etRotatingSpeed.setEnabled(true);
                    etCarSpeed.setEnabled(true);
                    etThrottlingValve.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        txtConnectionState.setText(Util.getPreference(Util.CONNECTION_STATE));

        registerReceiver(mConnectionStateChangeReceiver, connectionStateChangeFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mConnectionStateChangeReceiver);
    }

    private static IntentFilter connectionStateChangeFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        return intentFilter;
    }
}
