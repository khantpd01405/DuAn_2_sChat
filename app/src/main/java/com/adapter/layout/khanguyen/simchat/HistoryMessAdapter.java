package com.adapter.layout.khanguyen.simchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xuanvinh.demoandroid.R;
import com.object.contain.khanguyen.simchat.HistoryMessage;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;

import java.util.List;

/**
 * Created by kha on 25/10/2016.
 */

public class HistoryMessAdapter extends RecyclerView.Adapter<HistoryMessAdapter.MyViewHolder>{
    private List<HistoryMessage> mMessageListList;
    @Override
    public HistoryMessAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mess_history_list,parent,false);
        return new HistoryMessAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(HistoryMessAdapter.MyViewHolder holder, int position) {
        HistoryMessage mess = mMessageListList.get(position);
        holder.user_name.setText(mess.getUser_name_mess());
        holder.message.setText(mess.getMessage());
        holder.date_time.setText(mess.getdate_time());

    }

    @Override
    public int getItemCount() {
        return mMessageListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, message , date_time;

        public MyViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.tv_username_listmess);
            message = (TextView) view.findViewById(R.id.tv_message_listmess);
            date_time = (TextView) view.findViewById(R.id.tv_date_listmess);
        }
    }

    public HistoryMessAdapter(List<HistoryMessage> userList) {
        this.mMessageListList = userList;
    }

}