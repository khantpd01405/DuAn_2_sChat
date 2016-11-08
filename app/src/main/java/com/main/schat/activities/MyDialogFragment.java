package com.main.schat.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.main.schat.activities.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.socket.contain.khanguyen.simchat.Constants;
import com.state.SaveSharedPreference;

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

    public MyDialogFragment() {

        // Empty constructor is required for DialogFragment

        // Make sure not to add arguments to the constructor

        // Use `newInstance` instead as shown below

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepassword_dialog,container,false);



        return view;
    }



    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
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
                    Toast.makeText(getActivity(),"Nhập lại mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Fetch arguments from bundle and set title

        String title = getArguments().getString("title", "Enter Name");

        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field



        getDialog().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }


    private Emitter.Listener onChangePass = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           final String data = args[0].toString();
            if (getActivity()!=null) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if(data == "true"){
                            Toast.makeText(getActivity(), "Đổi mật khẩu thành công, vui lòng khởi động lại ứng dụng!", Toast.LENGTH_LONG).show();
                            SaveSharedPreference.clearUserName(getActivity());
                            mSocket.disconnect();
                            mSocket.off("change pass",onChangePass);
//                            startActivity(new Intent(getActivity(),LoginActivity.class));
                            getActivity().finish();


                        }else{
                            Toast.makeText(getActivity(), "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
            // Launch login activity

            //   hideDialog();
        }
    };
    public static MyDialogFragment newInstance(String title) {

        MyDialogFragment frag = new MyDialogFragment();

        Bundle args = new Bundle();

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.off("change pass",onChangePass);
    }
}
