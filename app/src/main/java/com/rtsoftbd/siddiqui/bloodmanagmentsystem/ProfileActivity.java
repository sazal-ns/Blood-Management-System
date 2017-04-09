/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

public class ProfileActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    mTextMessage.setText(R.string.title_history);
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_settings);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pro_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_our_goal) {
            new MaterialDialog.Builder(this)
                    .title("OUR GOAL")
                    .customView(ourGoal(), true)
                    .show();
        }else if (id == R.id.menu_about_us){
            showAboutUS();
        }else if (id==R.id.menu_logout){

        }

        return super.onOptionsItemSelected(item);
    }

    private View about(){
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view1 = (ViewGroup) inflater1.inflate(R.layout.about_us, null, false);

        return view1;

    }

    public View ourGoal(){
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view1 = (ViewGroup) inflater1.inflate(R.layout.our_goal, null, false);

        return view1;

    }

    public void showAboutUS(){
        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("About US")
                .customView(about(), wrapInScrollView)
                .show();
    }

}
