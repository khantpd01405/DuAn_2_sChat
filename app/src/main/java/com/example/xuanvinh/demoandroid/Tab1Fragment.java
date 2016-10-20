package com.example.xuanvinh.demoandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.layout.khanguyen.simchat.UserAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class Tab1Fragment extends Fragment {
    TextView test_txt;
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter mAdapter;
    ArrayList<User> arr = new ArrayList<>();
    String phone;
    String password;
    String usrname;
    @Nullable
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.111:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private Emitter.Listener onFragment = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();
            if(data == "true"){

            }
            test_txt.setText(data.toString());
            Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        Intent intent = getActivity().getIntent();
        arr = (ArrayList<User>) intent.getSerializableExtra(LoginActivity.EXTRA_KEY);
        View view = inflater.inflate(R.layout.tab1fragment,container,false);


        mSocket.on("register1", onRegister);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new UserAdapter(arr);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        test_txt = (TextView) view.findViewById(R.id.test_txt);
////        mSocket.connect();
////        mSocket.on("getarr", onFragment);





        mAdapter.notifyDataSetChanged();

//        test_txt.setText(arr.get(0).getUser_name());

        return view;
    }
    private void prepareMovieData(User user) {

        arr.add(user);


        mAdapter.notifyDataSetChanged();
    }



        private Emitter.Listener onRegister = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject)  args[0];


                try {
                    if(data.getString("tf").toString().equals("true")){
                         phone = data.getJSONObject("user").getString("phone").toString();
                         password = data.getJSONObject("user").getString("password").toString();
                         usrname = data.getJSONObject("user").getString("usr_name").toString();
                        Log.d("//////","co mot nguoi dung moi");
                        Log.d("//////",data.getJSONObject("user").getString("usr_name").toString());

                        if (getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    User user = new User(phone, password, usrname);
                                    prepareMovieData(user);
                                    Toast.makeText(getActivity(), "Co nguoi moi dang ky", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        // Launch login activity

                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //   hideDialog();

            }
        };
}
