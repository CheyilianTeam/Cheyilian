package com.example.justinchou.cheyilian.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
 * Created by J on 2016/4/23.
 * Show car speed, rotating speed, car speed, throttling valve.
 */
public class CarStateActivity extends BaseActivity {


    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private BluetoothGattService mPrimaryService;
    private static BluetoothGattCharacteristic mPrimaryCharacteristic;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private boolean mConnected = false;
    private static BluetoothLeService mBluetoothLeService;
    private String mDeviceName;
    private String mDeviceAddress;

    @InjectView(R.id.txt_rotating_speed)
    TextView txtRotatingSpeed;
    @InjectView(R.id.txt_car_speed)
    TextView txtCarSpeed;
    @InjectView(R.id.txt_throttling_value)
    TextView txtThrottlingValue;
    @InjectView(R.id.btn_profile)
    Button btnProfile;
    @InjectView(R.id.btn_setting)
    Button btnSetting;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("CarStateActivity", "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i("CarStateActivity", "Service discovered");
                if(mBluetoothLeService != null) {
                    // Find the primary service
                    for(BluetoothGattService temp : mBluetoothLeService.getSupportedGattServices()) {
                        if (temp.getUuid().toString().equals(Util.SERVICE_UUID)) {
                            mPrimaryService = temp;
                            break;
                        }
                    }

                    for(BluetoothGattCharacteristic temp : mPrimaryService.getCharacteristics()) {
                        Log.i("CarStateActivity", "Characteristic found: " + temp.getUuid().toString());
                        // Find the characteristic to communicate
                        if (temp.getUuid().toString().equals(Util.CHARACTERISTIC_UUID)) {
                            mPrimaryCharacteristic = temp;
                        }
                        // Find the notification characteristic
                        if (temp.getUuid().toString().equals(Util.NOTIFICATION_UUID)) {
                            mNotifyCharacteristic = temp;
                            mBluetoothLeService.setCharacteristicNotification(temp, false);
                            mBluetoothLeService.setCharacteristicNotification(temp, true);
                        }
                    }
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String received = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.i("CarStateActivity", "Data Received: " + received);
                try {
                    JSONObject message = new JSONObject(received);
                    int type = message.getInt(Util.TRANSFER_DATA);
                    double value = message.getDouble(Util.VALUE_TRANSFERED);
                    switch (type) {
                        case Util.ROTATING_SPEED_CONTROL:
                            Util.savePreference(Util.ROTATING_SPEED, Double.toString(value));
                            break;
                        case Util.CAR_SPEED_CONTROL:
                            Util.savePreference(Util.CAR_SPEED, Double.toString(value));
                            break;
                        case Util.THROTTLING_VALUE_CONTROL:
                            Util.savePreference(Util.THROTTLING_VALUE, Double.toString(value));
                            break;
                    }
                    Intent dataChangeIntent = new Intent(Util.ACTION_DATA_CHANGED);
                    sendBroadcast(dataChangeIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (Util.ACTION_DATA_CHANGED.equals(action)) {
                txtRotatingSpeed.setText(Util.getPreference(Util.ROTATING_SPEED));
                txtCarSpeed.setText(Util.getPreference(Util.CAR_SPEED));
                txtThrottlingValue.setText(Util.getPreference(Util.THROTTLING_VALUE));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_state);

        ButterKnife.inject(this);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        Util.savePreference(Util.DEVICE_NAME, mDeviceName);
        Util.savePreference(Util.DEVICE_NUMBER, mDeviceAddress);

        // Get data from the database
        Obd obd = dbService.find(Util.getPreference(Util.DEVICE_NUMBER));
        if (obd != null) {
            Util.savePreference(Util.TARGET_ROTATING_SPEED, obd.getTargetRotatingSpeed());
            Util.savePreference(Util.TARGET_CAR_SPEED, obd.getTargetCarSpeed());
            Util.savePreference(Util.TARGET_THROTTLING_VALUE, obd.getTargetThrottlingValue());
        }

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheyilianApplication.getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheyilianApplication.getContext(), SettingActivity.class);
                intent.putExtra(SettingActivity.DEVICE_NUMBER, Util.getPreference(Util.DEVICE_NUMBER));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        txtRotatingSpeed.setText(Util.getPreference(Util.ROTATING_SPEED));
        txtCarSpeed.setText(Util.getPreference(Util.CAR_SPEED));
        txtThrottlingValue.setText(Util.getPreference(Util.THROTTLING_VALUE));

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d("CarStateActivity", "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
        Util.clearPreference();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(Util.ACTION_DATA_CHANGED);
        return intentFilter;
    }

    // Use this to communicate with obd
    public static void sendBleMessage(String message) {
        mBluetoothLeService.writeCharacteristic(mPrimaryCharacteristic, message);
    }

}
