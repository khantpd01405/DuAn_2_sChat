package com.adapter.layout.khanguyen.simchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.schat.activities.R;
import com.object.contain.khanguyen.simchat.HistoryMessage;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kha on 25/10/2016.
 */

public class HistoryMessAdapter extends RecyclerView.Adapter<HistoryMessAdapter.MyViewHolder>{
    private List<ArrayList<Messaging>> mMessageListList_Arr;

    Base64 mBase64;
    @Override
    public HistoryMessAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mess_history_list,parent,false);
        return new HistoryMessAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(HistoryMessAdapter.MyViewHolder holder, int position) {
            Messaging messOb;

            messOb = mMessageListList_Arr.get(position).get(mMessageListList_Arr.get(position).size()-1);


            holder.user_name.setText(messOb.getUsername_fiend());
            holder.message.setText(messOb.getMessage());
            holder.date_time.setText(messOb.getDateTime());

        if(messOb.getString_profile().equals("")){
            holder.image_profile_friend.setImageResource(R.mipmap.logo);
        }else{
            byte[] hinhanh = mBase64.decode(messOb.getString_profile(),0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh,0,hinhanh.length);
            holder.image_profile_friend.setImageBitmap(bitmap);
        }




//            if(messOb.getImage_profile().equals("null")){
//                holder.image_profile_friend.setImageResource(R.drawable.ic_launcher);
//            }else{
//                byte[] hinhanh = mBase64.decode(messOb.getImage_profile().toString(),0);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh,0,hinhanh.length);
//                holder.image_profile_friend.setImageBitmap(bitmap);
//            }
    }

    @Override
    public int getItemCount() {
        return mMessageListList_Arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView user_name, message , date_time;
        private ImageView image_profile_friend;
        public MyViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.tv_username_listmess);
            message = (TextView) view.findViewById(R.id.tv_message_listmess);
            date_time = (TextView) view.findViewById(R.id.tv_date_listmess);
            image_profile_friend = (ImageView) view.findViewById(R.id.image_profile_friend);
        }
    }

    public HistoryMessAdapter(List<ArrayList<Messaging>> userList) {
        this.mMessageListList_Arr = userList;
    }

}