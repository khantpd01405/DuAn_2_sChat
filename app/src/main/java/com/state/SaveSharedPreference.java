package com.state;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kha on 05/11/2016.
 */

public class SaveSharedPreference {
    static final String PHONE= "phone";
    static final String USERNAME= "username";
    static final String STATE= "state";
    static final String SOCKETID= "socketid";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String phone, String userName /*, String state, String socketid*/)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PHONE, phone);
        editor.putString(USERNAME, userName);
//        editor.putString(STATE, state);
//        editor.putString(SOCKETID, socketid);
        editor.commit();
    }

    public static String getPhone(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PHONE, "");
    }
    public static String getUsername(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USERNAME, "");
    }
    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
