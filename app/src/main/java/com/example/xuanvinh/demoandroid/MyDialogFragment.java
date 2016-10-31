package com.example.xuanvinh.demoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.User;
import com.socket.contain.khanguyen.simchat.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by XuanVinh on 28/10/2016.
 */

public class MyDialogFragment extends DialogFragment {
    Button btnOk;
    EditText txtPassOld, txtPassNew1, txtPassNew2 ;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepassword_dialog,container,false);

        mSocket.on("change pass",onChangePass );

        btnOk = (Button)view.findViewById(R.id.btnOk);
        txtPassOld = (EditText)view.findViewById(R.id.txtPassOld);
        txtPassNew1 = (EditText)view.findViewById(R.id.txtPassNew1);
        txtPassNew2 = (EditText)view.findViewById(R.id.txtPassNew2);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPassNew2.getText().toString().equals(txtPassNew1.getText().toString())){
                    mSocket.emit("change pass", txtPassOld.getText().toString().trim(), txtPassNew1.getText().toString().trim());
                }else{
                    Toast.makeText(getActivity(),"Nhap lai mat khau khong chinh xac", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private Emitter.Listener onChangePass = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           final String data = args[0].toString();
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if(data == "true"){
                            Toast.makeText(getActivity(), "Doi mat khau thanh cong", Toast.LENGTH_SHORT).show();
                            mSocket.disconnect();
                            mSocket.off("change pass",onChangePass);
//                            startActivity(new Intent(getActivity(),LoginActivity.class));
                            getActivity().finish();


                        }else{
                            Toast.makeText(getActivity(), "Mat khau khong chinh xac", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
            // Launch login activity

            //   hideDialog();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("change pass",onChangePass);
    }
}
