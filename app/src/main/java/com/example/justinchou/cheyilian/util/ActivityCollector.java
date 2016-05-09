package com.example.justinchou.cheyilian.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by J on 2016/5/8.
 */
public class ActivityCollector {

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) activity.finish();
        }
    }

}
