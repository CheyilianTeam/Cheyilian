package com.example.justinchou.cheyilian;

import android.app.Application;
import android.content.Context;

/**
 * Created by J on 2016/4/21.
 */
public class CheyilianApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
