/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.History;

/**
 * Created by RTsoftBD_Siddiqui on 2017-04-11.
 */

public class CustomListAdapterHistory extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<History> histories;

    public CustomListAdapterHistory(Activity activity, List<History> histories) {
        this.activity = activity;
        this.histories = histories;
    }


    @Override
    public int getCount() {
        return histories.size();
    }

    @Override
    public Object getItem(int position) {
        return histories.get(position);
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
            convertView = inflater.inflate(R.layout.history_row, null);

        History history = histories.get(position);

        ViewHolder viewHolder = new ViewHolder(convertView);

        viewHolder.ms_DateTextView.setText(history.getDate());
        viewHolder.ms_HospitaTextView.setText(history.getHospital());
        viewHolder.ms_QuanitityTextView.setText(history.getQun());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.dateTextView) TextView ms_DateTextView;
        @BindView(R.id.hospitaTextView) TextView ms_HospitaTextView;
        @BindView(R.id.quanitityTextView) TextView ms_QuanitityTextView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
