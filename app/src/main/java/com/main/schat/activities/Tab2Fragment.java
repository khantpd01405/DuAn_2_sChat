package com.main.schat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adapter.layout.khanguyen.simchat.DividerItemDecoration;
import com.adapter.layout.khanguyen.simchat.HistoryMessAdapter;
import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Globals;
import com.object.contain.khanguyen.simchat.HistoryMessage;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;
import com.state.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

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
    private ArrayList<ArrayList<Messaging>> array_of_messageArray = new ArrayList<>();
    private ArrayList<HistoryMessage> mMessageListList = new ArrayList<>();
    JSONArray dat;
    Globals mGlobals = Globals.getInstance();
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
            if (getActivity()!=null) {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        clearArray();
                        for (int i = 0 ; i <dat.length(); i++) {
                            JSONObject rec = null;
                            try {
                                rec = dat.getJSONObject(i);
                                JSONObject obb = rec.getJSONObject("id_message");
                                Iterator keysToCopyIterator = obb.keys();


                                while(keysToCopyIterator.hasNext()) {

                                    String key = (String) keysToCopyIterator.next();
                                    JSONArray arrayMessage = obb.getJSONArray(key);
//                                Log.d("array message", arrayMessage.toString());
                                    ArrayList<Messaging> messageArray = new ArrayList<>();
                                    for (int j =0; j < arrayMessage.length(); j++){
                                        JSONObject rec_mess = arrayMessage.getJSONObject(j);
//                                    messageArray.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
//                                    .username(rec_mess.getString("usrname"))
//                                    .message(rec_mess.getString("message")).build());

                                        messageArray.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
                                                .string_profile(rec_mess.getString("profile_friend")).username(rec_mess.getString("usrname")).username_fiend(rec_mess.getString("username_friend"))
                                                .message(rec_mess.getString("message")).datetime(rec_mess.getString("date_time")).build());
                                    }
                                    array_of_messageArray.add(messageArray);
                                    mAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }

                    }
                });
            }


        }
    };





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2fragment,container,false);
        Intent intent = getActivity().getIntent();
        mSocket.on("on_emit_message", on_emit_message);


        arr =  intent.getParcelableArrayListExtra(LoginActivity.EXTRA_KEY);
        usrname_current = intent.getStringExtra("name").toString();
        array_of_messageArray = mGlobals.getData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_history);
        String me = "";
//        for (int i = 0 ; i < arr.size()-1 ; i++){
//
//                me = arr.get(i).getUser_message().get(arr.get(i).getUser_message().size()-1).getMessage();
//
//            mMessageListList.add(new HistoryMessage(arr.get(i).getUser_name()
//                    ,me.toString()));
////            mMessageListList = arr.get(i).getUser_message();
//        }

        mAdapter = new HistoryMessAdapter(array_of_messageArray);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private void clearArray(){
        array_of_messageArray.clear();
//        mAdapter.notifyDataSetChanged();
    }

    private void newMessage(User user) {

        arr.add(user);


        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("on_emit_message", on_emit_message);

    }
}
