package com.adapter.layout.khanguyen.simchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.main.schat.activities.R;
import com.object.contain.khanguyen.simchat.HistoryMessage;
import com.object.contain.khanguyen.simchat.Room;

import java.util.List;

/**
 * Created by kha on 26/10/2016.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder>{
    private List<Room> mRoomList;
    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list,parent,false);
        return new RoomAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(RoomAdapter.MyViewHolder holder, int position) {
        Room room = mRoomList.get(position);
        holder.roomName.setText(room.getRoomName());
        holder.numUser.setText(room.getNumUser()+"/100");

    }

    @Override
    public int getItemCount() {
        return mRoomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView roomName, numUser;

        public MyViewHolder(View view) {
            super(view);
            roomName = (TextView) view.findViewById(R.id.tv_roomName);
            numUser = (TextView) view.findViewById(R.id.tv_numUser);
        }
    }

    public RoomAdapter(List<Room> roomList) {
        this.mRoomList = roomList;
    }

}