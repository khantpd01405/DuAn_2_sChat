package com.adapter.layout.khanguyen.simchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.schat.activities.R;
import com.object.contain.khanguyen.simchat.Messaging;

import java.util.List;

/**
 * Created by kha on 20/10/2016.
 */

public class MessageAdapter_room extends RecyclerView.Adapter<MessageAdapter_room.ViewHolder> {

    private List<Messaging> mMessages;
    private int[] mUsernameColors;
    private Boolean you = false;
    public MessageAdapter_room(Context context, List<Messaging> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case Messaging.TYPE_MESSAGE:
                layout = R.layout.item_message_room;
                you = false;
                break;
            case Messaging.TYPE_LOG:
                layout = R.layout.item_log;
                break;
            case Messaging.TYPE_ACTION:
                layout = R.layout.item_action_room;
                you = false;
                break;
            case Messaging.TYPE_MESSAGE_USER:
                layout = R.layout.item_message_user_room;
                you = true;
                break;
            case Messaging.TYPE_IMAGE_USER:
                layout = R.layout.item_image_user_room;
                you = true;
                break;
            case Messaging.TYPE_IMAGE_FRIEND:
                layout = R.layout.item_image_room;
                break;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Messaging message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setUsername(message.getUsername());
        viewHolder.setImage(message.getImage());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;
        private ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);

            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mImageView = (ImageView) itemView.findViewById(R.id.img_Image);
        }

        public void setUsername(String username) {
            if (null == mUsernameView) return;
            if(you){
                mUsernameView.setText("Bạn");
                you = false;
            }else{
                 mUsernameView.setText(username);
            }
            mUsernameView.setTextColor(getUsernameColor(username));
        }

        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

        public void setImage(Bitmap bitmap) {
            if (null == mImageView) return;
            mImageView.setImageBitmap(bitmap);

        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
