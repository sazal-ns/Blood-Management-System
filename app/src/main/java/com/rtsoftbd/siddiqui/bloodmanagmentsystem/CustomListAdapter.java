/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import models.User;
import models.Users;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-09.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Users> userList;

    public CustomListAdapter(Activity activity, List<Users> userList) {
        this.activity = activity;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView headSlNo = (TextView) convertView.findViewById(R.id.headSlNo);
        TextView headDonorName = (TextView) convertView.findViewById(R.id.headDonorName);
        TextView headMobileNumber = (TextView) convertView.findViewById(R.id.headMobileNumber);
        TextView headAge = (TextView) convertView.findViewById(R.id.headAge);
        TextView headArea = (TextView) convertView.findViewById(R.id.headArea);
        TextView headUnion = (TextView) convertView.findViewById(R.id.headUnion);
        TextView headDistrict =(TextView) convertView.findViewById(R.id.headDistrict);


        Users u = userList.get(position);

        headSlNo.setText(u.getSl());
        headDonorName.setText(u.getDname());
        headMobileNumber.setText(u.getMobile());
        headAge.setText(u.getAge());
        headArea.setText(u.getArea());
        headUnion.setText(u.getUnion());
        headDistrict.setText(u.getDistrict());

        return convertView;
    }
}
