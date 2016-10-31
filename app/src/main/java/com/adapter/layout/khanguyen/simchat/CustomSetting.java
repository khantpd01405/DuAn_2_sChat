package com.adapter.layout.khanguyen.simchat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuanvinh.demoandroid.R;

/**
 * Created by XuanVinh on 27/10/2016.
 */

public class CustomSetting extends ArrayAdapter<String> {


    private Context context;
    private String[] nameSetting;
    private Integer[] iconSetting;
    public CustomSetting(Context context, String[] nameSetting, Integer[] iconSetting) {
        super(context, R.layout.list_setting, nameSetting);
        this.context = context;
        this.nameSetting = nameSetting;
        this.iconSetting = iconSetting;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_setting,parent,false);
        TextView txtSetting = (TextView)view.findViewById(R.id.txtSetting);
        ImageView mImage =(ImageView)view.findViewById(R.id.mImage);

        txtSetting.setText(nameSetting[position]);
        mImage.setImageResource(iconSetting[position]);
        return view;
    }
}
