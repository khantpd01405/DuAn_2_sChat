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

import java.net.URISyntaxException;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputPhone;
    private EditText inputRePassword;
    private EditText inputPassword;
    private EditText inputUserName;
    private ProgressDialog pDialog;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.111:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSocket.connect();

        inputPhone = (EditText) findViewById(R.id.txtSignuPhone);
        inputPassword = (EditText) findViewById(R.id.txtSignupPassword);
        inputRePassword = (EditText) findViewById(R.id.txtSignupAgainPassword);
        inputUserName = (EditText) findViewById(R.id.txtSignupUserName);
        btnRegister = (Button) findViewById(R.id.btnSignup_up);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mSocket.on("register", onRegister);



        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String phone = inputPhone.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();
                String username = inputUserName.getText().toString().trim();

                if (!username.isEmpty() && !pass.isEmpty() && !phone.isEmpty()) {
                    registerUser(phone, pass, username);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser(final String phone, final String password,
                              final String usrname) {


        mSocket.emit("register", phone, password, usrname);

        pDialog.setMessage("Registering ...");
        showDialog();
    }

    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();

            if(data == "true"){
                Log.d("//////","Dang ky thanh cong");
                // Launch login activity
                Intent intent = new Intent(
                        SignupActivity.this,
                        LoginActivity.class);

                startActivity(intent);

                finish();
            }else{
                Toast.makeText(SignupActivity.this, "Sdt hien da ton tai", Toast.LENGTH_SHORT).show();
            }

            //   hideDialog();

        }
    };

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }
    }
}
