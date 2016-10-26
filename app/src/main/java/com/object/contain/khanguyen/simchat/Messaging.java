package com.object.contain.khanguyen.simchat;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kha on 20/10/2016.
 */

public class Messaging  implements Serializable{
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String mDateTime;

    private Messaging() {}

    protected Messaging(Parcel in) {
        mType = in.readInt();
        mMessage = in.readString();
        mUsername = in.readString();
        mDateTime = in.readString();
    }



    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public String getDateTime() {
        return mDateTime;
    };



    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mDateTime;
        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder datetime(String datetime) {
            mDateTime = datetime;
            return this;
        }

        public Messaging build() {
            Messaging message = new Messaging();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mDateTime = mDateTime;
            return message;
        }
    }
}
