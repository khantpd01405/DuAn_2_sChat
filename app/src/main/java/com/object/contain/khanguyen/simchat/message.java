package com.object.contain.khanguyen.simchat;

/**
 * Created by kha on 19/10/2016.
 */

public class message {
    private String id;
    private String txt_user_mess;
    private String txt_fr_mess;
    private String date_send;

    public message(String id, String txt_user_mess, String txt_fr_mess, String date_send) {
        this.id = id;
        this.txt_user_mess = txt_user_mess;
        this.txt_fr_mess = txt_fr_mess;
        this.date_send = date_send;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxt_user_mess() {
        return txt_user_mess;
    }

    public void setTxt_user_mess(String txt_user_mess) {
        this.txt_user_mess = txt_user_mess;
    }

    public String getTxt_fr_mess() {
        return txt_fr_mess;
    }

    public void setTxt_fr_mess(String txt_fr_mess) {
        this.txt_fr_mess = txt_fr_mess;
    }

    public String getDate_send() {
        return date_send;
    }

    public void setDate_send(String date_send) {
        this.date_send = date_send;
    }

}
