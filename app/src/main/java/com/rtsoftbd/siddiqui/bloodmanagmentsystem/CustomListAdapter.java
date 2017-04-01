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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import helper.AppController;
import models.Users;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-09.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Users> userList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

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

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        //ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView headDonorName = (TextView) convertView.findViewById(R.id.donorName);
        TextView headMobileNumber = (TextView) convertView.findViewById(R.id.number);
        TextView headAge = (TextView) convertView.findViewById(R.id.age);
        TextView headArea = (TextView) convertView.findViewById(R.id.area);
        TextView headDistrict =(TextView) convertView.findViewById(R.id.district);


        Users u = userList.get(position);

        if (u.getImageLink().contentEquals("null")) {
            if (u.getBloodg().contentEquals("A+"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Apos.png", imageLoader);
            else if (u.getBloodg().contentEquals("A-"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Aneg.png", imageLoader);
            else if (u.getBloodg().contentEquals("B+"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Bpos.png", imageLoader);
            else if (u.getBloodg().contentEquals("B-"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Bneg.png", imageLoader);
            else if (u.getBloodg().contentEquals("O+"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Opos.png", imageLoader);
            else if (u.getBloodg().contentEquals("O-"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Oneg.png", imageLoader);
            else if (u.getBloodg().contentEquals("AB+"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/ABpos.png", imageLoader);
            else if (u.getBloodg().contentEquals("AB-"))
                thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/ABneg.png", imageLoader);
        }else
        thumbnail.setImageUrl(u.getImageLink(), imageLoader);

        headDonorName.setText(u.getDname());
        headMobileNumber.setText(u.getMobile());
        headAge.setText("Age: "+u.getAge());
        headArea.setText(u.getArea()+",");
        headDistrict.setText(u.getDistrict());


        return convertView;
    }
}
