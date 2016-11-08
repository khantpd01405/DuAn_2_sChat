package com.main.schat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;
import com.state.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class RunActivity extends AppCompatActivity {
    private ArrayList<User> UserArray;
    private ArrayList<Messaging> messageArray = new ArrayList<>();
    public final static String EXTRA_KEY = "key.loginactivy";
    private ProgressDialog pDialog;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        UserArray = new ArrayList<>();
        mSocket.connect();
        mSocket.on("logged", onLogged);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        if(SaveSharedPreference.getPhone(RunActivity.this).length() == 0)
        {
            // call Login Activity
            startActivity(new Intent(RunActivity.this, LoginActivity.class));
            finish();
        }
        else
        {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mSocket.emit("logged",SaveSharedPreference.getPhone(RunActivity.this).toString(),SaveSharedPreference.getUsername(RunActivity.this).toString());
                }

            }, 2000L);

        }

    }



    private Emitter.Listener onLogged = new Emitter.Listener() {
        boolean tf = false;
        User ob;
        @Override
        public void call(final Object... args) {



            try {
                        JSONArray dat =  (JSONArray) args[0];
                        Intent intent = new Intent(RunActivity.this,
                                UiMychat.class);
                        for (int i = 0 ; i <dat.length(); i++) {
                            JSONObject rec = null;

                                rec = dat.getJSONObject(i);
                                User User = new User(rec.getString("phone").toString(),rec.getString("password").toString(),rec.getString("usr_name").toString(), rec.getBoolean("status"), rec.getString("socketId").toString() ,messageArray);
                                UserArray.add(User);
                                if(SaveSharedPreference.getPhone(RunActivity.this).toString().equals(User.getPhone().toString())){
                                    tf = true;
                                    ob = User;
                                }

                            }
                            if(tf) {
                                UserArray.remove(ob);
                                ob.setStatus(true);
                                JSONObject jsOb = new JSONObject();
                                jsOb.put("phone",ob.getPhone());
                                jsOb.put("username",ob.getUser_name());
                                jsOb.put("socketid",ob.getSocketId());
                                jsOb.put("status",ob.isStatus());
                                mSocket.emit("user online", jsOb);
                            }
                            intent.putParcelableArrayListExtra(EXTRA_KEY,UserArray);
                            intent.putExtra("name",ob.getUser_name());
                            intent.putExtra("phone",ob.getPhone());
                            startActivity(intent);
                            finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    };

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("logged");

    }

}
