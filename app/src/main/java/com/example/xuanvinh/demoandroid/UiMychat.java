package com.example.xuanvinh.demoandroid;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.socket.contain.khanguyen.simchat.Constants;

import java.net.URISyntaxException;

public class UiMychat extends AppCompatActivity {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    ViewPager mViewPager;
    PagerSlidingTabStrip mPagerSlidingTabStrip;
    ViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();
        setContentView(R.layout.activity_ui_mychat);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.mPagerSlidingTabStrip);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
