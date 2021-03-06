package com.example.justinchou.cheyilian.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.databinding.ProfileBinding;
import com.example.justinchou.cheyilian.model.User;

/**
 * Created by Justin Chou on 2016/4/17.
 * Show user profile, can be modified.
 */
public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.profile);
        User user = new User();
        user.setDeviceNumber("0000-0000-0000-0001");
        user.setUserName("zzy");
        user.setPhoneNumber("15850537201");
        binding.setUser(user);
        user.setDeviceNumber("fefe");
    }
}
