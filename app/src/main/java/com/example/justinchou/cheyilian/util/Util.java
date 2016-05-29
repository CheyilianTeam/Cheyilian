package com.example.justinchou.cheyilian.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.justinchou.cheyilian.CheyilianApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by J on 2016/4/21.
 */
public class Util {

    public static final String ACTION_DATA_CHANGED = "DATA_CHANGE_ACTION";

    public static final String CONNECTION_STATE = "connectionState";
    public static final String STATE_CONNECTED = "connected";
    public static final String STATE_DISCONNECTED = "disconnected";

    public static final String CONTROL_COMMAND = "A";
    public static final int COMMAND_ON = 0;
    public static final int COMMAND_OFF = 1;

    public static final String TRANSFER_DATA = "B";
    public static final String SET_ID = "C";
    public static final String VALUE_TRANSFERED = "V";

    public static final String DEVICE_NAME = "deviceName";
    public static final String DEVICE_NUMBER = "deviceNumber";
    public static final String ROTATING_SPEED = "rotatingSpeed";
    public static final String CAR_SPEED = "carSpeed";
    public static final String THROTTLING_VALUE = "throttlingValue";
    public static final String TARGET_ROTATING_SPEED = "targetRotatingSpeed";
    public static final String TARGET_CAR_SPEED = "targetCarSpeed";
    public static final String TARGET_THROTTLING_VALUE = "targetThrottlingValue";

    public static final int ROTATING_SPEED_CONTROL = 1;
    public static final int CAR_SPEED_CONTROL = 2;
    public static final int THROTTLING_VALUE_CONTROL = 3;

    public static final String SHAREDPREFERENCE_FILE_NAME = "com.example.justinchou.cheyilian.PREFERENCE";

    public static final String SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static final String CHARACTERISTIC_UUID = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String NOTIFICATION_UUID = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static final String NOTIFICATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";

    public static final int SUCCESS_MESSAGE = 0;
    public static final int ERROR_MESSAGE = 1;

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) CheyilianApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private static void sendMessage(Handler handler, int what, String msg) {
        Message message = handler.obtainMessage(what, msg);
        handler.sendMessage(message);
    }

    public static void httpGet(final String url, final Handler handler) {
        if (isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Request request = new Request.Builder().url(url).build();
                    try {
                        Response response = mOkHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) sendMessage(handler, SUCCESS_MESSAGE, response.body().string());
                        else sendMessage(handler, ERROR_MESSAGE, response.body().string());
                    } catch (IOException e) {
                        sendMessage(handler, ERROR_MESSAGE, "出现错误，请稍后重试");
                    }
                }
            }).start();
        } else sendMessage(handler, ERROR_MESSAGE, "没有网络");
    }

    public static void httpPost(final String url, final Handler handler, final Map<String, String> params) {
        if (isNetworkAvailable()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String strJson = new JSONObject(params).toString();
                    RequestBody body = RequestBody.create(JSON, strJson);
                    Request request = new Request.Builder().url(url).post(body).build();
                    try {
                        Response response = mOkHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) sendMessage(handler, SUCCESS_MESSAGE, response.body().string());
                        else sendMessage(handler, ERROR_MESSAGE, response.body().string());
                    } catch (IOException e) {
                        sendMessage(handler, ERROR_MESSAGE, "出现错误，请稍后重试");
                    }
                }
            }).start();
        } else sendMessage(handler, ERROR_MESSAGE, "没有网络");
    }

    public static boolean scanValidation(byte[] scanRecord) {
//        if (scanRecord[7] == 3 && scanRecord[8] == 'c' && scanRecord[9] == 'y' && scanRecord[10] == 'l') return true;
//        return false;
        return true;
    }

    public static String getPreference(String key) {
        SharedPreferences pref = CheyilianApplication.getContext().getSharedPreferences(SHAREDPREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String value = pref.getString(key, "0");
        Log.i("Util", "Get preference: " + value);
        return value;
    }

    public static void savePreference(String key, String value) {
        SharedPreferences.Editor editor = CheyilianApplication.getContext().getSharedPreferences(SHAREDPREFERENCE_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
        Log.i("Util", "Put preference: " + key + " " + value);
    }

    public static void clearPreference() {
        SharedPreferences.Editor editor = CheyilianApplication.getContext().getSharedPreferences(SHAREDPREFERENCE_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Log.i("Util", "Clear preference");
    }

}
