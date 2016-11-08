package com.main.schat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Messaging;
import com.socket.contain.khanguyen.simchat.Constants;
import com.object.contain.khanguyen.simchat.User;
import com.state.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputPhone;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    public final static String EXTRA_KEY = "key.loginactivy";
    private ArrayList<User> UserArray;
    private ArrayList<User> UserArray_own = new ArrayList<>();
    private ArrayList<Messaging> messageArray = new ArrayList<>();

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    private Emitter.Listener onLogin = new Emitter.Listener() {
        boolean tf = false;
        User ob;
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();

//            JSONObject data1 = (JSONObject) args[1];
            try {
//                JSONArray arr1 = data1.getJSONArray("array");
//                JSONObject ob1 = arr1.getJSONObject(0);


                if(data == "true"){
                    JSONArray dat =  (JSONArray) args[1];
                    Intent intent = new Intent(LoginActivity.this,
                            UiMychat.class);
//                    Log.d("/////////",dat+"");
                    for (int i = 0 ; i <dat.length(); i++) {
                        JSONObject rec = dat.getJSONObject(i);

//                        JSONArray messArr = rec.getJSONArray("message_usr_arr");

//                        for (int j =0; j < messArr.length(); j++){
//                            JSONObject rec_mess = messArr.getJSONObject(j);
//                            messageArray.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
//                                    .username(rec_mess.getString("usrname"))
//                                    .message(rec_mess.getString("message")).build());
//                        }
                        User User = new User(rec.getString("phone").toString(),rec.getString("password").toString(),rec.getString("usr_name").toString(), rec.getBoolean("status"), rec.getString("socketId").toString() ,messageArray);
                        UserArray.add(User);
                        if(inputPhone.getText().toString().equals(User.getPhone().toString())){
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

//                    Log.d("////////// kha", UserArray.get(0).getUser_message().get(0).getUsername() + ": "+UserArray.get(0).getUser_message().get(0).getMessage());
                    SaveSharedPreference.setUserName(LoginActivity.this,ob.getPhone(), ob.getUser_name());
                    intent.putParcelableArrayListExtra(EXTRA_KEY,UserArray);
                    intent.putExtra("name",ob.getUser_name());
                    intent.putExtra("phone",ob.getPhone());

                startActivity(intent);
                finish();
                }else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                        Toast.makeText(LoginActivity.this, "Mật khẩu không chính xác, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                        }
                    });
                        }
            } catch (Exception e) {
                e.printStackTrace();
            }


            hideDialog();

        }
    };



    private Emitter.Listener onLogin1 = new Emitter.Listener() {
        User ob;
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();
            try {


                if(data == "false"){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            hideDialog();

        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserArray = new ArrayList<>();
        mSocket.on("login", onLogin);
        mSocket.on("login1", onLogin1);

        mSocket.connect();

        inputPhone = (EditText) findViewById(R.id.txtPhone_number);
        inputPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String phone = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!phone.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(phone, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SignupActivity.class);

                startActivity(i);
                finish();
            }
        });
    }

    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request

        pDialog.setMessage("Logging in ...");
        showDialog();

        mSocket.emit("login", email, password);

    }


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

        mSocket.off("login", onLogin);
        mSocket.off("login1", onLogin1);
    }
}
