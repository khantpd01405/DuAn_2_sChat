package com.adapter.layout.khanguyen.simchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xuanvinh.demoandroid.R;
import com.object.contain.khanguyen.simchat.User;

import java.util.List;

/**
 * Created by kha on 19/10/2016.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    private List<User> mUserListList;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = mUserListList.get(position);
        holder.password.setText(user.getPassword());
        holder.phone.setText(user.getPhone());
        holder.user_name.setText(user.getUser_name());
    }

    @Override
    public int getItemCount() {
        return mUserListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView phone, password , user_name;

        public MyViewHolder(View view) {
            super(view);
            password = (TextView) view.findViewById(R.id.tv_pass_usr);
            phone = (TextView) view.findViewById(R.id.tv_phone);
            user_name = (TextView) view.findViewById(R.id.tv_usr_name);
        }
    }

    public UserAdapter(List<User> userList) {
        this.mUserListList = userList;
    }

}
