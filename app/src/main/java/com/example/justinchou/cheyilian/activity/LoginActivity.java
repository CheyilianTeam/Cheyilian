package com.example.justinchou.cheyilian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.justinchou.cheyilian.CheyilianApplication;
import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by J on 2016/4/28.
 * Login script
 */
public class LoginActivity extends Activity {

    private Handler mHandler;

    private EditText etPhoneNumber;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Util.SUCCESS_MESSAGE) Toast.makeText(CheyilianApplication.getContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                else Toast.makeText(CheyilianApplication.getContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "zzy");
                Util.httpPost("https://www.baidu.com", mHandler, params);
            }
        });
    }
}
