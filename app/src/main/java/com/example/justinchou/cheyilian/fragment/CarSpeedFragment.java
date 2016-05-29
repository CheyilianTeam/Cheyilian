package com.example.justinchou.cheyilian.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.util.Util;

/**
 * Created by J on 2016/5/29.
 */
public class CarSpeedFragment extends Fragment {

    TextView txtCarSpeed;

    private final BroadcastReceiver dataChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Util.ACTION_DATA_CHANGED.equals(action)) {
                txtCarSpeed.setText(Util.getPreference(Util.CAR_SPEED));
            }
        }
    };

    public static CarSpeedFragment newInstance() {
        return new CarSpeedFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(dataChangeReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().registerReceiver(dataChangeReceiver, dataChangeIntentFilter());
        View rootView = inflater.inflate(R.layout.car_speed_fragment, container, false);
        txtCarSpeed = (TextView) rootView.findViewById(R.id.txt_car_speed);
        txtCarSpeed.setText(Util.getPreference(Util.CAR_SPEED));
        return rootView;
    }

    private static IntentFilter dataChangeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Util.ACTION_DATA_CHANGED);
        return intentFilter;
    }
}
