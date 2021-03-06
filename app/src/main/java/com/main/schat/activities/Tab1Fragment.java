package com.main.schat.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.layout.khanguyen.simchat.DividerItemDecoration;
import com.adapter.layout.khanguyen.simchat.RecyclerTouchListener;
import com.adapter.layout.khanguyen.simchat.UserAdapter;
import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;
import com.state.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class Tab1Fragment extends Fragment {
    TextView test_txt;
    private RecyclerView recyclerView;
    private UserAdapter mAdapter;
    ArrayList<User> arr;
    int getPosition;
    String phone, usrname, usrname_current, phone_current , profile_other, socketId_other;
    @Nullable
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        arr = (ArrayList<User>) intent.getSerializableExtra(LoginActivity.EXTRA_KEY);
        usrname_current = intent.getStringExtra("name").toString();
        phone_current = intent.getStringExtra("phone").toString();
        View view = inflater.inflate(R.layout.tab1fragment,container,false);

        mSocket.on("register1", onRegister);
        mSocket.on("user off", onUserOff);
        mSocket.on("user online", onUserOnline);
        mSocket.on("client gui image dai dien", onUserSendProfile);
        mSocket.on("push to user", onPushToUser);
        mSocket.on("logout", onLogOut);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new UserAdapter(arr);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
//        test_txt = (TextView) view.findViewById(R.id.test_txt);

////        mSocket.on("getarr", onFragment);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                getActivity().getApplicationContext(),recyclerView, new RecyclerTouchListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                attemptLogin(arr.get(position));
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra("name",arr.get(position).getUser_name());
                intent.putExtra("socketfriend",arr.get(position).getSocketId());
                intent.putExtra("profile_image",arr.get(position).getImage());
                startActivity(intent);
            }
        }));




//        test_txt.setText(arr.get(0).getUser_name());

        return view;
    }
    private void prepareMovieData(User user) {

        arr.add(user);


        mAdapter.notifyDataSetChanged();
    }

    private void attemptLogin(User user) {

        int bind_two_user = Integer.parseInt(user.getPhone()) + Integer.parseInt(phone_current);
        // perform the user login attempt.
        mSocket.emit("add user", usrname_current, bind_two_user);
    }

        private Emitter.Listener onRegister = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                try {
                    JSONObject data = (JSONObject)  args[0];
                    if(data.getString("tf").toString().equals("true")){
                         phone = data.getJSONObject("user").getString("phone").toString();
                         usrname = data.getJSONObject("user").getString("usr_name").toString();
                         profile_other = data.getJSONObject("user").getString("image_profile").toString();
                         socketId_other = data.getJSONObject("user").getString("socketId").toString();

                                    User user = new User(usrname, false);
                                    user.setPhone(phone);
                                    user.setSocketId(socketId_other);
                                    user.setImage(profile_other);
                                    prepareMovieData(user);
//                                    Toast.makeText(getActivity(), "Một người dùng mới đã sử dụng ứng dụng :)", Toast.LENGTH_LONG).show();

                        // Launch login activity
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                        }
                    });
                }
                //   hideDialog();
            }
        };




    private Emitter.Listener onUserOff = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject data = (JSONObject) args[0];
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            int getPosition = 0;
                            User user = new User(usrname, false);
                            user.setUser_name(data.getString("username"));
                            user.setPhone(data.getString("phone"));
                            user.setStatus(Boolean.parseBoolean(data.getString("status")));
                            user.setImage(data.getString("profile"));
                            for(int i = 0; i < arr.size(); i++){
                                if(user.getPhone().equals(arr.get(i).getPhone())){
                                    getPosition = i;
                                }
                            }
                            arr.set(getPosition,user);
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), "Co nguoi offline", Toast.LENGTH_SHORT).show();
                    }
                });
            }
                    // Launch login activity


            //   hideDialog();
        }
    };


    private Emitter.Listener onUserOnline = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject data = (JSONObject)  args[0];
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            User user = new User();

                            user.setPhone(data.getString("phone"));
                            user.setUser_name(data.getString("username"));
                            user.setSocketId(data.getString("socketid"));
                            user.setStatus(Boolean.parseBoolean(data.getString("status")));
                            user.setImage(data.getString("profile"));
                            for(int i = 0; i < arr.size(); i++){
                                if(data.getString("phone").equals(arr.get(i).getPhone())){
                                    getPosition = i;
                                    break;
                                }
                            }
                            arr.set(getPosition,user);
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getActivity(), "Co nguoi online", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            // Launch login activity


            //   hideDialog();
        }
    };

    private Emitter.Listener onUserSendProfile = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final JSONObject data = (JSONObject)  args[0];
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            int getPosition = 0;
                            User user = new User(usrname, false);

                            user.setPhone(data.getString("phone"));
                            user.setUser_name(data.getString("username"));
                            user.setSocketId(data.getString("socketid"));
                            user.setStatus(Boolean.parseBoolean(data.getString("status")));
                            user.setImage(data.getString("profile"));
                            for(int i = 0; i < arr.size(); i++){
                                if(user.getPhone().equals(arr.get(i).getPhone())){
                                    getPosition = i;
                                }
                            }
                            arr.set(getPosition,user);
                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            // Launch login activity


            //   hideDialog();
        }
    };


    public void sendNotification(String username,String message, String socketid, int bind_two_user, String profile) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("socketfriend", socketid);
        intent.putExtra("usrname_friend",UiMychat.mUserName);
        intent.putExtra("name",username);
        intent.putExtra("click",true);
        intent.putExtra("bindtoserver",bind_two_user);
        intent.putExtra("profile_image",profile);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(username)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
    }

    private Emitter.Listener onPushToUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {

                            usrname = data.getString("username");
                            String message = data.getString("message").toString();
                            int phone = data.getInt("phone");
                            String socketid = data.getString("socketId");
                            String profile = data.getString("profile");
                            int bind_two_user = phone + Integer.parseInt(phone_current);
                            // perform the user login attempt.

                            sendNotification(usrname, message, socketid, bind_two_user, profile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
            // Launch login activity


            //   hideDialog();
        }
    };

    private Emitter.Listener onLogOut = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {


            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    String data =   args[0].toString();
                    if(data.equals(phone_current)){
                        Toast.makeText(getActivity(),"Một thiết bị khác đã đăng nhập, bạn sẽ bị logout!",Toast.LENGTH_LONG).show();
                        SaveSharedPreference.clearUserName(getActivity());

                        getActivity().finish();
                    }else{
                        Toast.makeText(getActivity(),"Never mind",Toast.LENGTH_SHORT).show();
                    }



                }
            });

            // Launch login activity


            //   hideDialog();
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("push to user", onPushToUser);
        mSocket.off("register1", onRegister);
        mSocket.off("user off", onUserOff);
        mSocket.off("logout", onLogOut);
        mSocket.off("user online", onUserOnline);
        mSocket.off("client gui image dai dien", onUserSendProfile);
    }
}
