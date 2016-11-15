package com.main.schat.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.github.nkzawa.emitter.Emitter;
import com.main.schat.activities.R;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class UiMychat extends AppCompatActivity {
    public static String mUserName , phone_current, mImage_profile, mSocketId;
    public static Socket mSocket;
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


    Emitter.Listener onUpdateImage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data = args[0].toString();
            mImage_profile = data;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSocket.on("update_image",onUpdateImage);
        Intent intent = getIntent();
        mUserName = intent.getStringExtra("name").toString();
        phone_current = intent.getStringExtra("phone").toString();
        mImage_profile = intent.getStringExtra("profile").toString();
        mSocketId = intent.getStringExtra("socketid").toString();
        setContentView(R.layout.activity_ui_mychat);
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip)findViewById(R.id.mPagerSlidingTabStrip);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);

        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        mSocket.on("push to user", onPushToUser);
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mSocket.off("push to user", onPushToUser);
//    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.emit("escape");
        mSocket.off("update_image",onUpdateImage);
        mSocket.disconnect();

    }



}
