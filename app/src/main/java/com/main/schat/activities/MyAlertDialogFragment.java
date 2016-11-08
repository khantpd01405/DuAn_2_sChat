package com.main.schat.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.state.SaveSharedPreference;

/**
 * Created by kha on 06/11/2016.
 */

public class MyAlertDialogFragment extends DialogFragment {

    public MyAlertDialogFragment() {

        // Empty constructor required for DialogFragment

    }



    public static MyAlertDialogFragment newInstance(String title) {

        MyAlertDialogFragment frag = new MyAlertDialogFragment();

        Bundle args = new Bundle();

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }



    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString("title");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder.setMessage("Thoát tài khoản hiện tại bạn có thể đăng nhập bằng tài khoản khác!");

        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {
                SaveSharedPreference.clearUserName(getActivity());
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();
            }

        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });



        return alertDialogBuilder.create();

    }

}