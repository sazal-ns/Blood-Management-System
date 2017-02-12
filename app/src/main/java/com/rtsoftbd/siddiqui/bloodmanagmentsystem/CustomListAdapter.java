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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView headDonorName = (TextView) convertView.findViewById(R.id.donorName);
        TextView headMobileNumber = (TextView) convertView.findViewById(R.id.number);
        TextView headAge = (TextView) convertView.findViewById(R.id.age);
        TextView headArea = (TextView) convertView.findViewById(R.id.area);
        TextView headDistrict =(TextView) convertView.findViewById(R.id.district);


        Users u = userList.get(position);

        if (u.getBloodg().contains("A+")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.a_pos));
        else if (u.getBloodg().contains("A-")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.a_neg));
        else if (u.getBloodg().contains("B+")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.b_pos));
        else if (u.getBloodg().contains("B-")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.b_neg));
        else if (u.getBloodg().contains("O+")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.o_pos));
        else if (u.getBloodg().contains("O-")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.o_neg));
        else if (u.getBloodg().contains("AB+")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ab_pos));
        else if (u.getBloodg().contains("AB-")) thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.ab_neg));
        else thumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.logo));


        headDonorName.setText(u.getDname());
        headMobileNumber.setText(u.getMobile());
        headAge.setText("Age: "+u.getAge());
        headArea.setText(u.getArea()+",");
        headDistrict.setText(u.getDistrict());


        return convertView;
    }
}
