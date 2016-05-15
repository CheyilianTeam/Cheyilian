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

//    public void save(Obd obd) {
//        if (null != find(obd.getName())) db.execSQL("INSERT INTO user VALUES (?, ?)",new String[]{user.getName(), Encryption.encrypt(user.getPassword())});
//    }

//    public Obd find(String name) {
//        Obd user = null;
//        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE name = ?",new String[]{name});
//        while(cursor.moveToNext()) {
//            user = new Obd(cursor.getString(1), Encryption.decrypt(cursor.getString(2)));
//        }
//        cursor.close();
//        return user;
//    }

}
