package com.main.schat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Globals;
import com.object.contain.khanguyen.simchat.Messaging;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;
import com.state.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunActivity extends AppCompatActivity {
    private ArrayList<User> UserArray;


    public final static String EXTRA_KEY = "key.loginactivy";
    private ProgressDialog pDialog;
    List<String> keysList = new ArrayList<String>();
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
                            ArrayList<ArrayList<Messaging>> array_of_messageArray = new ArrayList<>();
                            JSONObject rec = dat.getJSONObject(i);

                            JSONObject obb = rec.getJSONObject("id_message");
                            Iterator keysToCopyIterator = obb.keys();


                            while(keysToCopyIterator.hasNext()) {

                                String key = (String) keysToCopyIterator.next();
                                JSONArray arrayMessage = obb.getJSONArray(key);
//                                Log.d("array message", arrayMessage.toString());
                                ArrayList<Messaging> messageArray = new ArrayList<>();
                                for (int j =0; j < arrayMessage.length(); j++){
                                    JSONObject rec_mess = arrayMessage.getJSONObject(j);
//                                    messageArray.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
//                                    .username(rec_mess.getString("usrname"))
//                                    .message(rec_mess.getString("message")).build());

                                    messageArray.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
                                            .string_profile(rec_mess.getString("profile_friend")).username(rec_mess.getString("usrname")).username_fiend(rec_mess.getString("username_friend"))
                                            .message(rec_mess.getString("message")).datetime(rec_mess.getString("date_time")).build());
                                    }
                                array_of_messageArray.add(messageArray);
                            }




                            User User = new User(rec.getString("phone").toString(),rec.getString("password").toString(),rec.getString("usr_name").toString(), rec.getBoolean("status"), rec.getString("socketId").toString(),array_of_messageArray);
                                User.setImage(rec.getString("image_profile"));

                                if(SaveSharedPreference.getPhone(RunActivity.this).toString().equals(User.getPhone().toString())){
                                    tf = true;
                                    ob = User;
                                }else{
                                    User.setUser_message(null);
                                    UserArray.add(User);
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
                                jsOb.put("profile",ob.getImage());

                                mSocket.emit("user online", jsOb);

                            }
                            intent.putParcelableArrayListExtra(EXTRA_KEY,UserArray);

                            Globals g = Globals.getInstance();
                            g.setData((ArrayList<ArrayList<Messaging>>)ob.getUser_message());

                            intent.putExtra("name",ob.getUser_name());
                            intent.putExtra("phone",ob.getPhone());
                            intent.putExtra("socketid",ob.getSocketId());
                            if(ob.getImage().equals("null")){
                                intent.putExtra("profile","");
                            }else{
                                intent.putExtra("profile",ob.getImage());
                            }

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
        mSocket.off("logged",onLogged);

    }

}
