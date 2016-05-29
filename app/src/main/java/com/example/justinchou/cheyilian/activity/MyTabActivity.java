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
import android.view.LayoutInflater;
import android.widget.TabHost;

import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.service.BluetoothLeService;
import com.example.justinchou.cheyilian.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by J on 2016/5/29.
 */
public class MyTabActivity extends android.app.TabActivity {

    private BluetoothGattService mPrimaryService;
    private static BluetoothGattCharacteristic mPrimaryCharacteristic;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private static BluetoothLeService mBluetoothLeService;

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
            mBluetoothLeService.connect(Util.getPreference(Util.DEVICE_NUMBER));
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
            if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.my_tab, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("排气控制").setIndicator("排气控制").setContent(new Intent(this, CarStateActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("我的").setIndicator("我的").setContent(new Intent(this, ProfileActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(Util.getPreference(Util.DEVICE_NUMBER));
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
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    // Use this to communicate with obd
    public static void sendBleMessage(String message) {
        mBluetoothLeService.writeCharacteristic(mPrimaryCharacteristic, message);
    }
}
