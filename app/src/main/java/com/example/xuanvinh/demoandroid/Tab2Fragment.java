package com.example.xuanvinh.demoandroid;

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

import com.adapter.layout.khanguyen.simchat.DividerItemDecoration;
import com.adapter.layout.khanguyen.simchat.HistoryMessAdapter;
import com.adapter.layout.khanguyen.simchat.UserAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.HistoryMessage;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class Tab2Fragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryMessAdapter mAdapter;
    ArrayList<User> arr;
    String usrname;
    String usrname_current;
    String phone_current;
    private ArrayList<User> UserArray;
    private ArrayList<Messaging> messageArray = new ArrayList<>();
    private ArrayList<HistoryMessage> mMessageListList = new ArrayList<>();
    JSONArray dat;
    @Nullable
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private Emitter.Listener on_emit_message = new Emitter.Listener() {
        boolean tf = false;
        User ob;
        @Override
        public void call(Object... args) {
//            Toast.makeText(getActivity(), "1 tin nhan da duoc luu", Toast.LENGTH_SHORT).show();
//            Log.d("//////////////","1 tin nhan");

            dat =  (JSONArray) args[0];


    getActivity().runOnUiThread(new Runnable() {
        public void run() {
            clearArray();
            try {
                for (int i = 0 ; i <dat.length(); i++) {
                    JSONObject rec = dat.getJSONObject(i);
                    JSONArray messArr = rec.getJSONArray("message_usr_arr");
                    for (int j =0; j < messArr.length(); j++){
                        JSONObject rec_mess = messArr.getJSONObject(j);
                        messageArray.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
                                .username(rec_mess.getString("usrname"))
                                .message(rec_mess.getString("message")).build());
                    }
                    User User = new User(rec.getString("phone").toString(),rec.getString("password").toString(),rec.getString("usr_name").toString(),messageArray);
                    arr.add(User);
                    if(usrname_current.equals(User.getUser_name().toString())){
                        tf = true;
                        ob = User;
                    }
                }

                if(tf) {
                    arr.remove(ob);
                }

                String me = "";
                for (int i = 0 ; i < arr.size() ; i++){

                    me = arr.get(i).getUser_message().get(arr.get(i).getUser_message().size()-1).getMessage();

                    mMessageListList.add(new HistoryMessage(arr.get(i).getUser_name()
                            ,me.toString()));
//            mMessageListList = arr.get(i).getUser_message();
                }

                        mAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    });

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2fragment,container,false);
        Intent intent = getActivity().getIntent();
        mSocket.connect();
        mSocket.on("on_emit_message", on_emit_message);


        arr =  intent.getParcelableArrayListExtra(LoginActivity.EXTRA_KEY);
        usrname_current = intent.getStringExtra("name").toString();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        String me = "";
        for (int i = 0 ; i < arr.size() ; i++){

                me = arr.get(i).getUser_message().get(arr.get(i).getUser_message().size()-1).getMessage();

            mMessageListList.add(new HistoryMessage(arr.get(i).getUser_name()
                    ,me.toString()));
//            mMessageListList = arr.get(i).getUser_message();
        }

        mAdapter = new HistoryMessAdapter(mMessageListList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private void clearArray(){
        arr.clear();
        mMessageListList.clear();
//        mAdapter.notifyDataSetChanged();
    }

    private void newMessage(User user) {

        arr.add(user);


        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();

        mSocket.off("on_emit_message", on_emit_message);

    }
}
