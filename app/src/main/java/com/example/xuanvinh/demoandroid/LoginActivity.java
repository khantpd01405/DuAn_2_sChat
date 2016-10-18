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

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputPhone;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.111:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }



    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();

            if(data == "true"){
                Intent intent = new Intent(LoginActivity.this,
                        UiMychat.class);
                startActivity(intent);
                finish();
            }else{
                Log.d("error", "cant login");
            }

            hideDialog();

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
