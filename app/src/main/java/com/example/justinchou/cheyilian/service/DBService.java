package com.example.justinchou.cheyilian.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.justinchou.cheyilian.model.Obd;
import com.example.justinchou.cheyilian.util.DBHelper;

/**
 * Created by Justin on 2016/1/24.
 */
public class DBService {

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBService(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void save(Obd obd) {
        if (null == find(obd.getDeviceNumber())) db.execSQL("INSERT INTO obd VALUES (?, ?, ?, ?, ?)",new String[]{obd.getDeviceName(), obd.getDeviceNumber(), obd.getTargetRotatingSpeed(), obd.getTargetCarSpeed(), obd.getTargetThrottlingValue()});
        else db.execSQL("INSERT INTO obd VALUES (?, ?, ?, ?, ?)",new String[]{obd.getDeviceName(), obd.getDeviceNumber(), obd.getTargetRotatingSpeed(), obd.getTargetCarSpeed(), obd.getTargetThrottlingValue()});
    }

    public Obd find(String deviceNumber) {
        Obd obd = null;
        Cursor cursor = db.rawQuery("SELECT * FROM obd WHERE deviceNumber = ?",new String[]{deviceNumber});
        while(cursor.moveToNext()) {
            obd = new Obd();
            obd.setDeviceName(cursor.getString(2));
            obd.setDeviceNumber(cursor.getString(3));
            obd.setTargetRotatingSpeed(cursor.getString(4));
            obd.setTargetCarSpeed(cursor.getString(5));
            obd.setTargetThrottlingValue(cursor.getString(6));
        }
        cursor.close();
        return obd;
    }

}
