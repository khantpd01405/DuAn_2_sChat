package com.object.contain.khanguyen.simchat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by kha on 19/10/2016.
 */

public class User implements Parcelable {
    private String id;
    private String user_name;
    private String phone;
    private String password;
    private String email;
    private String image;
    private String sex;
    private String birthday;
    private String fiendName;
    private ArrayList<message> user_message;

    public User() {
    }


    public User(String phone, String password, String user_name) {
        this.phone = phone;
        this.password = password;
        this.user_name = user_name;
    }
    public User(String id, String user_name, String phone, String password, String email, String image, String sex, String birthday, String fiendName, ArrayList<message> user_message) {
        this.id = id;
        this.user_name = user_name;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.image = image;
        this.sex = sex;
        this.birthday = birthday;
        this.fiendName = fiendName;
        this.user_message = user_message;
    }

    protected User(Parcel in) {
        id = in.readString();
        user_name = in.readString();
        phone = in.readString();
        password = in.readString();
        email = in.readString();
        image = in.readString();
        sex = in.readString();
        birthday = in.readString();
        fiendName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFiendName() {
        return fiendName;
    }

    public void setFiendName(String fiendName) {
        this.fiendName = fiendName;
    }

    public ArrayList<message> getUser_message() {
        return user_message;
    }

    public void setUser_message(ArrayList<message> user_message) {
        this.user_message = user_message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_name);
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(sex);
        dest.writeString(birthday);
        dest.writeString(fiendName);
    }
}
