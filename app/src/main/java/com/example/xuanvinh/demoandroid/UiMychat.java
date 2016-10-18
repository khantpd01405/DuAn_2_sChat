package com.example.xuanvinh.demoandroid;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;

public class UiMychat extends AppCompatActivity {

    ViewPager mViewPager;
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    ViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_mychat);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.mPagerSlidingTabStrip);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);

    }
}
