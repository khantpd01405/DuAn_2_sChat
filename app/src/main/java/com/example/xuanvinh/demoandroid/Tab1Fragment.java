package com.example.xuanvinh.demoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by XuanVinh on 14/10/2016.
 */

public class Tab1Fragment extends Fragment {
    TextView test_txt;
    String[] arr = new String[3];

    @Nullable
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.0.111:3000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private Emitter.Listener onFragment = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String data =  args[0].toString();
            if(data == "true"){

            }
            test_txt.setText(data.toString());
            Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1fragment,container,false);
        test_txt = (TextView) view.findViewById(R.id.test_txt);
        mSocket.connect();
        mSocket.on("getarr", onFragment);

        return view;
    }
}
