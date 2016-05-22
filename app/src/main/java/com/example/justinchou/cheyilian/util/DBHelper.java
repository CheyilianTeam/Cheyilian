package com.example.justinchou.cheyilian.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 2016/1/24.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cheyilian.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS obd (deviceName TEXT, deviceNumber TEXT, targetRotatingSpeed TEXT, targetCarSpeed TEXT, targetThrottlingValue TEXT, PRIMARY KEY(deviceNumber))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE device_list ADD COLUMN other STRING");
    }

}
