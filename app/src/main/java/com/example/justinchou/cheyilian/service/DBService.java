package com.example.justinchou.cheyilian.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        if (null == find(obd.getDeviceNumber())) db.execSQL("INSERT INTO obd (deviceName, deviceNumber, targetRotatingSpeed, targetCarSpeed, targetThrottlingValue) VALUES (?, ?, ?, ?, ?)",new String[]{obd.getDeviceName(), obd.getDeviceNumber(), obd.getTargetRotatingSpeed(), obd.getTargetCarSpeed(), obd.getTargetThrottlingValue()});
        else {
            String update = "UPDATE obd SET deviceName='" + obd.getDeviceName() + "', targetRotatingSpeed='" + obd.getRotatingSpeed() +
                    "', targetCarSpeed='" + obd.getCarSpeed() + "', targetThrottlingValue='" +
                    obd.getTargetThrottlingValue() + "' WHERE deviceNumber='" + obd.getDeviceNumber() + "'";
            db.execSQL(update);
        }
    }

    public Obd find(String deviceNumber) {
        Obd obd = null;
        Cursor cursor = db.rawQuery("SELECT * FROM obd WHERE deviceNumber = ?",new String[]{deviceNumber});
        while(cursor.moveToNext()) {
            obd = new Obd();
            obd.setDeviceName(cursor.getString(1));
            obd.setDeviceNumber(cursor.getString(2));
            obd.setTargetRotatingSpeed(cursor.getString(3));
            obd.setTargetCarSpeed(cursor.getString(4));
            obd.setTargetThrottlingValue(cursor.getString(5));
            Log.i("Database", "Find obd: " + obd.getDeviceName() + " " + obd.getDeviceNumber() + " " +
            obd.getTargetRotatingSpeed() + " " + obd.getTargetCarSpeed() + " " + obd.getTargetThrottlingValue());
        }
        cursor.close();
        return obd;
    }

}
