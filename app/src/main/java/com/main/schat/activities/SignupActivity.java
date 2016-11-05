package com.main.schat.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.socket.contain.khanguyen.simchat.Constants;

import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputPhone;
    private EditText inputRePassword;
    private EditText inputPassword;
    private EditText inputUserName;
    private ProgressDialog pDialog;
//    MongoClientURI mongoUri  = new MongoClientURI("mongodb://192.168.0.111:27017/simchat");
//    MongoClient mongoClient = new MongoClient(mongoUri);
//    DB db = mongoClient.getDB("simchat");
//    DBCollection collectionNames = db.getCollection("usrcl");
//    BasicDBObject document = new BasicDBObject();
//    private List<Messaging> messageList = new ArrayList<>();
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public SignupActivity() throws UnknownHostException {
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
                String re_pass = inputRePassword.getText().toString().trim();
                String username = inputUserName.getText().toString().trim();

                if (!username.isEmpty() && !pass.isEmpty() && !phone.isEmpty()) {
                    if(!pass.equals(re_pass)){
                        Toast.makeText(getApplicationContext(),
                                "Nhập lại password không khớp!", Toast.LENGTH_LONG)
                                .show();
                    }else{
                        registerUser(phone, pass, username);
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG)
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

//        messageList.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
//                .username("kha").message("cai chi mi").build());
//        messageList.add(new Messaging.Builder(Messaging.TYPE_MESSAGE)
//                .username("di").message("e kha").build());
//        JSONArray jsArray = new JSONArray(messageList);

        mSocket.emit("register", phone, password, usrname ,false);

        pDialog.setMessage("Registering ...");
        showDialog();
    }

    private Emitter.Listener onRegister = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            String data =  args[0].toString();
            if(data == "true"){
                runOnUiThread(new Runnable() {
                    public void run() {
                        hideDialog();
                        Toast.makeText(SignupActivity.this, "Đăng ký thành công ", Toast.LENGTH_SHORT).show();
                    }
                });

                // Launch login activity
                Intent intent = new Intent(
                        SignupActivity.this,
                        LoginActivity.class);

                startActivity(intent);

                finish();

            }else{
                runOnUiThread(new Runnable() {
                    public void run() {
                        hideDialog();
                        Toast.makeText(SignupActivity.this, "Số điện thoại này đã được sử ", Toast.LENGTH_SHORT).show();
                    }
                    });
                }



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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("register", onRegister);
    }
}
