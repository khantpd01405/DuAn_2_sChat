package com.example.xuanvinh.demoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.adapter.layout.khanguyen.simchat.CustomSetting;
import com.adapter.layout.khanguyen.simchat.DividerItemDecoration;
import com.adapter.layout.khanguyen.simchat.RecyclerTouchListener;
import com.adapter.layout.khanguyen.simchat.RoomAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.object.contain.khanguyen.simchat.Room;
import com.socket.contain.khanguyen.simchat.Constants;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by kha on 30/10/2016.
 */

public class Tab4Fragment extends Fragment {
    private MaterialBetterSpinner mCustomLanguage;
    private MaterialBetterSpinner mTheme;
    private String[] arrLg;
    private String[] arrCl;
    private ArrayAdapter<String> mAdapter;
    private TextView usrName, phone;
    // ListView Setting
    private ListView mListView;
    private CustomSetting customst;
    private String[] nameSettingArray = {"Đổi mật khẩu","Volume","Text Font","Photos và Camera"};
    private Integer[] imageArray =
            {
                    R.drawable.online,
                    R.drawable.online,
                    R.drawable.online,
                    R.drawable.online
            };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.tab4fragment, container, false);
        usrName = (TextView) rootView.findViewById(R.id.MyNameUser);
        phone = (TextView) rootView.findViewById(R.id.MyNumberPhone);
        usrName.setText(UiMychat.mUserName.toString());
        phone.setText(UiMychat.phone_current.toString());
        mListView = (ListView)rootView.findViewById(R.id.mListView);
        mCustomLanguage = (MaterialBetterSpinner)rootView.findViewById(R.id.mCustomLanguage);
        mTheme = (MaterialBetterSpinner)rootView.findViewById(R.id.mTheme);
        doSpinnerLanguage();
        doSpinnerColor();

        customst = new CustomSetting(getActivity(),nameSettingArray,imageArray);
        mListView.setAdapter(customst);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    FragmentManager fragmentManager = getFragmentManager();
                    MyDialogFragment myDialogFragment = new MyDialogFragment();
                    myDialogFragment.show(fragmentManager,"MyDialog");
                }
            }
        });
        return rootView;
    }
    public void doSpinnerLanguage()
    {
        arrLg = getResources().getStringArray(R.array.mLanguage);
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,arrLg);
        mCustomLanguage.setAdapter(mAdapter);
    }

    public void doSpinnerColor()
    {
        arrCl = getResources().getStringArray(R.array.mTheme);
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,arrCl);
        mTheme.setAdapter(mAdapter);
    }


}