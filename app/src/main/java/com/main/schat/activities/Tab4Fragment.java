package com.main.schat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.adapter.layout.khanguyen.simchat.CustomSetting;
import com.main.schat.activities.R;
import com.state.SaveSharedPreference;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by kha on 30/10/2016.
 */

public class Tab4Fragment extends Fragment {

    private String[] arrLg;
    private String[] arrCl;
    private ArrayAdapter<String> mAdapter;
    private TextView usrName, phone;
    // ListView Setting
    private ListView mListView;
    private CustomSetting customst;
    private String[] nameSettingArray = {"Đổi mật khẩu","Photos và Camera","Thay đổi ngôn ngữ","Đăng xuất"};
    private Integer[] imageArray =
            {
                    R.drawable.changepass,
                    R.drawable.gallary,
                    R.drawable.language,
                    R.drawable.logout
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



        customst = new CustomSetting(getActivity(),nameSettingArray,imageArray);
        mListView.setAdapter(customst);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    MyDialogFragment myDialogFragment =  MyDialogFragment.newInstance("Đổi mật khẩu");
                    myDialogFragment.show(fragmentManager,"MyDialog");
                }
                if(i == 3) {
                    showAlertDialog();
                }
            }
        });
        return rootView;
    }
    private void showAlertDialog() {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Bạn muốn thoát chứ?");

        alertDialog.show(fm, "fragment_alert");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}