package com.example.justinchou.cheyilian.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.example.justinchou.cheyilian.CheyilianApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by J on 2016/4/21.
 */
public class Util {

    public static final String SERVICE_UUID = "";
    public static final String CHARACTERISTIC_UUID = "";
    public static final String NOTIFICATION_UUID = "";

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

}
