package com.object.contain.khanguyen.simchat;

import android.graphics.Bitmap;
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
    public static final int TYPE_MESSAGE_USER = 3;
    public static final int TYPE_IMAGE_USER = 4;
    public static final int TYPE_IMAGE_FRIEND = 5;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String mDateTime;
    private Bitmap mImage;
    private Bitmap mImage_profile;

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

    public Bitmap getImage() {
        return mImage;
    };

    public Bitmap getImage_profile() {
        return mImage_profile;
    };

    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mDateTime;
        private Bitmap mImage;
        private Bitmap mImage_profile;
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
        public Builder image_profile(Bitmap image) {
            mImage_profile = image;
            return this;
        }
        public Builder image(Bitmap image) {
            mImage = image;
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
            message.mImage = mImage;
            message.mImage_profile = mImage_profile;
            return message;
        }
    }
}
