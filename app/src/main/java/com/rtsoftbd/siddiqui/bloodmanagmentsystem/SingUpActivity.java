/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import helper.ShowDialog;
import models.User;

public class SingUpActivity extends AppCompatActivity {

    private EditText donorNameEditText,userNameEditText, passwordEditText, mobileEditText, areaEditText, thanaEditText,unionEditText,
            districtEditText,ageEditText;
    private Spinner groupSpinner;
    private Button singUpButtonD;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        preInIt();
    }

    private void preInIt() {

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);

        donorNameEditText = (EditText) findViewById(R.id.donorNameEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        areaEditText = (EditText) findViewById(R.id.areaEditText);
        thanaEditText = (EditText) findViewById(R.id.thanaEditText);
        unionEditText = (EditText) findViewById(R.id.unionEditText);
        districtEditText = (EditText) findViewById(R.id.districtEditText);
        ageEditText = (EditText) findViewById(R.id.ageEditText);

        singUpButtonD = (Button) findViewById(R.id.singUnButtonD);
        singUpButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (donorNameEditText.getText().toString().isEmpty() && userNameEditText.getText().toString().isEmpty() &&
                        passwordEditText.getText().toString().isEmpty() && mobileEditText.getText().toString().isEmpty() &&
                        areaEditText.getText().toString().isEmpty() && thanaEditText.getText().toString().isEmpty() &&
                        unionEditText.getText().toString().isEmpty() &&  districtEditText.getText().toString().isEmpty() &&
                        ageEditText.getText().toString().isEmpty())
                    new ShowDialog(SingUpActivity.this, "Error!!!", "Please Provide All Information", getResources().getDrawable(R.drawable.ic_error_red_24dp));

                else
                doRegistration(donorNameEditText.getText().toString().trim(), userNameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(), mobileEditText.getText().toString().trim(),
                        areaEditText.getText().toString().trim(),thanaEditText.getText().toString().trim(),
                        unionEditText.getText().toString().trim(), districtEditText.getText().toString().trim(),
                        ageEditText.getText().toString().trim(), String.valueOf(groupSpinner.getSelectedItem()), "user");
            }
        });
    }

    private void doRegistration(final String donor, final String userName, final String password, final String mobile,
                                final String area, final String thana, final String union, final String district, final String age,
                                final String bloodGroup, final String user_type) {
        pDialog.setMessage("Loading...");
        showDialog();

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

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

                        Intent intent = new Intent(SingUpActivity.this, UserProfile.class);
                        startActivity(intent);
                        finish();

                    }else{
                        hideDialog();
                        new ShowDialog(SingUpActivity.this, "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
                    }
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error reg", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dname",donor);
                params.put("username",userName);
                params.put("password",password);
                params.put("user_type",user_type);
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
