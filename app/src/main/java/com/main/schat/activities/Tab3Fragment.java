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
import com.adapter.layout.khanguyen.simchat.RecyclerTouchListener;
import com.adapter.layout.khanguyen.simchat.RoomAdapter;
import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Room;
import com.socket.contain.khanguyen.simchat.Constants;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class Tab3Fragment extends Fragment {
    private RecyclerView recyclerView;
    private RoomAdapter mAdapter;
    ArrayList<Room> arr = new ArrayList<>();
    String roomName;
    String numRoom;
    String usrname_current;
    int num =0;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3fragment,container,false);
        mSocket.on("joined", onJoined);
        Intent intent = getActivity().getIntent();
        usrname_current = intent.getStringExtra("name").toString();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_room);
        arr.clear();
        arr.add(new Room("Miền Bắc"));
        arr.add(new Room("Miền Trung"));
        arr.add(new Room("Miền Nam"));
        mAdapter = new RoomAdapter(arr);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(
                getActivity().getApplicationContext(),recyclerView, new RecyclerTouchListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                attemptLogin(arr.get(position));
                Intent intent = new Intent(getActivity(),ChatRoomActivity.class);
                intent.putExtra("usrname",usrname_current);
                intent.putExtra("name",arr.get(position).getRoomName());
                startActivity(intent);
            }
        }));
        return view;
    }

    private void attemptLogin(Room room) {
        String bind_user = room.getRoomName();
        // perform the user login attempt.
        mSocket.emit("add user to room", usrname_current, bind_user);
    }
    private Emitter.Listener onJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//                final String nameRoom = args[0].toString();
                final int i = Integer.parseInt(args[1].toString());
                final int j = Integer.parseInt(args[2].toString());
                final int k = Integer.parseInt(args[3].toString());

                    if (getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
//                                if(nameRoom.equals("Miền Bắc")){
//                                    num = i;
//                                    arr.set(0,new Room("Miền Bắc",i));
//
////                                    arr.add(new Room("Miền Trung"));
////                                    arr.add(new Room("Miền Nam"));
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                                if(nameRoom.equals("Miền Trung")){
//                                    num = j;
//                                    arr.set(1,new Room("Miền Trung",j));
//
////                                    arr.add(new Room("Miền Trung"));
////                                    arr.add(new Room("Miền Nam"));
//                                    mAdapter.notifyDataSetChanged();
//                                }
//                                if(nameRoom.equals("Miền Nam")){
//                                    num = k;
//                                    arr.set(2,new Room("Miền Nam",k));
//
////                                    arr.add(new Room("Miền Trung"));
////                                    arr.add(new Room("Miền Nam"));
//                                    mAdapter.notifyDataSetChanged();
//                                }
////                                else if(nameRoom.equals("Miền Trung")){
////                                    arr.add(new Room("Miền Bắc"));
////                                    arr.add(new Room("Miền Trung",sl));
////                                    arr.add(new Room("Miền Nam"));
////                                }else if(nameRoom.equals("Miền Nam")){
////                                    arr.add(new Room("Miền Bắc"));
////                                    arr.add(new Room("Miền Trung"));
////                                    arr.add(new Room("Miền Nam",sl));
////                                }
                            }
                        });
                    }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("joined", onJoined);
    }
}
