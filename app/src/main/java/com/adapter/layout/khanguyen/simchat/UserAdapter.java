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
import com.main.schat.activities.UiMychat;
import com.object.contain.khanguyen.simchat.User;

import java.util.List;

/**
 * Created by kha on 19/10/2016.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    private List<User> mUserListList;
    private boolean tf = false;
    Base64 mBase64;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = mUserListList.get(position);
        holder.user_name.setText(user.getUser_name());
        if(user.getImage().equals("null")){
            holder.image_profile.setImageResource(R.drawable.user);
        }else{
            byte[] hinhanh = mBase64.decode(user.getImage(),0);
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh,0,hinhanh.length);
            holder.image_profile.setImageBitmap(bitmap);
        }

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
        ImageView image, image_profile;

        public MyViewHolder(View view) {
            super(view);
            user_name = (TextView) view.findViewById(R.id.tv_username_listuser);
            image = (ImageView) view.findViewById(R.id.image_status);
            image_profile = (ImageView) view.findViewById(R.id.img_user_profile);
        }
    }

    public UserAdapter(List<User> userList) {
        this.mUserListList = userList;
    }

}
