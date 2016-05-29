package com.example.justinchou.cheyilian.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.justinchou.cheyilian.R;
import com.example.justinchou.cheyilian.fragment.CarSpeedFragment;
import com.example.justinchou.cheyilian.fragment.RotatingSpeedFragment;
import com.example.justinchou.cheyilian.fragment.ThrottlingValueFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by J on 2016/4/23.
 * Show car speed, rotating speed, car speed, throttling valve.
 */
public class CarStateActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int tabCount = 0;

    @InjectView(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_state);

        ButterKnife.inject(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() - e1.getX() > 0) {
            //mViewFlipper.showPrevious();
            tabCount--;
            if (tabCount < 0) tabCount = 2;
            mViewPager.setCurrentItem(tabCount);
        } else {
            //mViewFlipper.showNext();
            tabCount++;
            if (tabCount > 2) tabCount = 0;
            mViewPager.setCurrentItem(tabCount);
        }
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch(position){
                case 0: return CarSpeedFragment.newInstance();
                case 1: return RotatingSpeedFragment.newInstance();
                case 2: return ThrottlingValueFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
