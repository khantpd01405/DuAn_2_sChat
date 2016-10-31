package com.adapter.layout.khanguyen.simchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuanvinh.demoandroid.R;
import com.object.contain.khanguyen.simchat.User;

import java.util.List;

/**
 * Created by kha on 19/10/2016.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    private List<User> mUserListList;
    private boolean tf = false;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = mUserListList.get(position);
        holder.user_name.setText(user.getUser_name());
        if(user.isStatus()){
            holder.image.setImageResource(R.drawable.online);
        }else{
            holder.image.setImageResource(R.drawable.offline);
        }
    }

    @Override
    public int getItemCount() {
        return mUserListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.tv_username_listuser);
            image = (ImageView) view.findViewById(R.id.image_status);
        }
    }

    public UserAdapter(List<User> userList) {
        this.mUserListList = userList;
    }

}
