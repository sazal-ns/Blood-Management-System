/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import config.Config;
import helper.ConnectionDetect;
import helper.ShowDialog;
import models.User;

/**
 * Created by RTsoftBD_Siddiqui on 2017-02-08.
 */

public class UserProfile extends AppCompatActivity {

    private EditText donorNameEditText,userNameEditText, passwordEditText, mobileEditText, areaEditText, thanaEditText,unionEditText,
            districtEditText,ageEditText;
    private Spinner groupSpinner;
    private Button singUpButtonD;
    private TextView  singinTextView;

    ConnectionDetect cd;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        cd = new ConnectionDetect(this);

        preInIt();
    }

    private void inIt() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(adapter);

        singinTextView.setText(R.string.updateInformation);
        singUpButtonD.setText(R.string.updateInformation);


            donorNameEditText.setText(User.getDname());
            userNameEditText.setText(User.getUsername());
            passwordEditText.setText(User.getPassword());
            mobileEditText.setText(User.getMobile());
            areaEditText.setText(User.getArea());
            thanaEditText.setText(User.getThana());
            unionEditText.setText(User.getUnion());
            districtEditText.setText(User.getDistrict());
            ageEditText.setText(User.getAge());
            groupSpinner.setSelection(adapter.getPosition(User.getBloodg()));

    }



    private void preInIt() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);
        singinTextView = (TextView) findViewById(R.id.singinTextView);


        donorNameEditText = (EditText) findViewById(R.id.donorNameEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        areaEditText = (EditText) findViewById(R.id.areaEditText);
        thanaEditText = (EditText) findViewById(R.id.thanaEditText);
        unionEditText = (EditText) findViewById(R.id.unionEditText);
        districtEditText = (EditText) findViewById(R.id.districtEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);

        userNameEditText.setClickable(false);
        userNameEditText.setEnabled(false);

        singUpButtonD = (Button) findViewById(R.id.singUnButtonD);
        singUpButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnected()) {
                    showNetDisabledAlertToUser(UserProfile.this);
                } else if (donorNameEditText.getText().toString().isEmpty() && userNameEditText.getText().toString().isEmpty() &&
                        passwordEditText.getText().toString().isEmpty() && mobileEditText.getText().toString().isEmpty() &&
                        areaEditText.getText().toString().isEmpty() && thanaEditText.getText().toString().isEmpty() &&
                        unionEditText.getText().toString().isEmpty() &&  districtEditText.getText().toString().isEmpty() &&
                        ageEditText.getText().toString().isEmpty())
                    new ShowDialog(UserProfile.this, "Error!!!", "Please Provide All Information",
                            getResources().getDrawable(R.drawable.ic_error_red_24dp));

                else
                updateInfo(donorNameEditText.getText().toString().trim(), userNameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(), mobileEditText.getText().toString().trim(),
                        areaEditText.getText().toString().trim(),thanaEditText.getText().toString().trim(),
                        unionEditText.getText().toString().trim(), districtEditText.getText().toString().trim(),
                        ageEditText.getText().toString().trim(), String.valueOf(groupSpinner.getSelectedItem()));
            }
        });
        if (!cd.isConnected()) {
            showNetDisabledAlertToUser(this);
        }
        inIt();
    }

    private void updateInfo(final String donorName, final String userName, final String password, final String mobile,
                            final String area, final String thana, final String union, final String district, final String age, final String bloodGroup) {
        pDialog.setMessage("Loading...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_UPDATE_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                hideDialog();
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("response", response);

                    if (jsonObject.toString().contains("false")){
                        JSONObject object = new JSONObject(jsonObject.getString("user"));

                        User.setId(object.getString("id"));
                        User.setDname(object.getString("dname"));
                        User.setUsername(object.getString("username"));
                        User.setPassword(object.getString("password"));
                        User.setUser_type(object.getString("user_type"));
                        User.setMobile("0"+object.getString("mobile"));
                        User.setArea(object.getString("area"));
                        User.setThana(object.getString("thana"));
                        User.setUnion(object.getString("union"));
                        User.setDistrict(object.getString("district"));
                        User.setAge(object.getString("age"));
                        User.setBloodg(object.getString("bloodg"));

                        inIt();

                    }else{
                        new ShowDialog(UserProfile.this, "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
                    }
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("update error", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",User.getId());
                params.put("dname",donorName);
                params.put("username",userName);
                params.put("password",password);
                params.put("mobile",mobile);
                params.put("area",area);
                params.put("thana",thana);
                params.put("union",union);
                params.put("district",district);
                params.put("age",age);
                params.put("bloodg",bloodGroup);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void showNetDisabledAlertToUser(final Context context){

        Drawable error_icon = getResources().getDrawable(R.drawable.ic_error_red_24dp);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(R.string.error)
                .content(R.string.connectionError)
                .positiveText(R.string.positive)
                .positiveColor(Color.parseColor("#6dc390"))
                .negativeText(R.string.negative)
                .negativeColor(Color.RED)
                .neutralText(R.string.natural)
                .neutralColor(Color.BLUE)
                .icon(error_icon)
                .cancelable(false)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        context.startActivity(dialogIntent);
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
