package com.example.xuanvinh.demoandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.User;

import org.json.JSONArray;
import org.json.JSONException;
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

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.111:3000");
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
            JSONArray dat =  (JSONArray) args[1];
//            JSONObject data1 = (JSONObject) args[1];
            try {
//                JSONArray arr1 = data1.getJSONArray("array");
//                JSONObject ob1 = arr1.getJSONObject(0);


                if(data == "true"){
                    Intent intent = new Intent(LoginActivity.this,
                            UiMychat.class);
                    Log.d("/////////",dat+"");
                    for (int i = 0 ; i <dat.length(); i++) {
                        JSONObject rec = dat.getJSONObject(i);
                        User User = new User(rec.getString("phone").toString(),rec.getString("password").toString(),rec.getString("usr_name").toString());


                        UserArray.add(User);
                        if(inputPhone.getText().toString().equals(User.getPhone().toString())){
                            tf =true;
                            ob = User;
                        }
                    }
                    if(tf) UserArray.remove(ob);
                    intent.putExtra(EXTRA_KEY,UserArray);
                startActivity(intent);
                finish();
                }else{
                    Log.d("error", "cant login");
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
        mSocket.connect();

        mSocket.on("login", onLogin);



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
}
