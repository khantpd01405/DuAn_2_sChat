package com.object.contain.khanguyen.simchat;

/**
 * Created by kha on 25/10/2016.
 */

public class HistoryMessage {
    private String user_name_mess;
    private String message;
    private String date_time;


    public HistoryMessage() {
    }

    public HistoryMessage(String user_name_mess, String message) {
        this.user_name_mess = user_name_mess;
        this.message = message;
    }

    public HistoryMessage(String user_name_mess, String message, String date_time) {
        this.user_name_mess = user_name_mess;
        this.message = message;
        this.date_time = date_time;
    }

    public String getUser_name_mess() {
        return user_name_mess;
    }

    public void setUser_name_mess(String user_name_mess) {
        this.user_name_mess = user_name_mess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getdate_time() {
        return date_time;
    }

    public void setdate_time(String date_time) {
        this.date_time = date_time;
    }
}
