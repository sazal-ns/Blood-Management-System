/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import helper.ShowDialog;
import models.User;

public class MoreActivity extends AppCompatActivity {

   private EditText ageEditText, emailEditText, passwordEditText, repetPasswordEditText, userNameEditText;
   private AppCompatButton saveButton;

    private String age, email, password, repetPassword, userName;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ageEditText = (EditText) findViewById(R.id.ageEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        repetPasswordEditText = (EditText) findViewById(R.id.repeatPasswordEditText);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        saveButton = (AppCompatButton) findViewById(R.id.updateButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {
        setData();
        if (!validate()) return;

        pDialog.setMessage("Updating...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_MORE_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pDialog.dismiss();
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

                        Intent intent = new Intent(MoreActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        pDialog.dismiss();
                       // Intent intent = new Intent(MoreActivity.this, MoreActivity.class);
                       // startActivity(intent);
                        new ShowDialog(MoreActivity.this, "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Log.e("email",email);
                params.put("id", User.getId());
                params.put("username",userName);
                params.put("password",password);
                params.put("age",age);
                params.put("bEmail",email);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void setData() {
        email = emailEditText.getText().toString().trim();
        age = ageEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        repetPassword = repetPasswordEditText.getText().toString().trim();
        userName = userNameEditText.getText().toString().trim();
    }

    private boolean validate() {
        boolean valid = true;

        if (age.isEmpty()){
            ageEditText.setError("give valid age");
            valid = false;
        } else
            if (Integer.valueOf(age) < 0 || Integer.valueOf(age) > 80){
                ageEditText.setError("give valid age");
                valid = false;
            }else ageEditText.setError(null);

        if (userName.isEmpty() || userName.length()<5){
            userNameEditText.setError("user name min length 5");
            valid = false;
        }else userNameEditText.setError(null);

        if (password.isEmpty() || password.length() < 5){
            passwordEditText.setError("password min length is 5");
            valid = false;
        }else passwordEditText.setError(null);

        if (!repetPassword.contentEquals(password)){
            repetPasswordEditText.setError("not match!!");
            valid = false;
        }else repetPasswordEditText.setError(null);

        if (email.isEmpty() || !email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            emailEditText.setError("give valid email address");
            valid = false;
        } else emailEditText.setError(null);

        return valid;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Going Back...")
                .setMessage("If you go back you lost track your account.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MoreActivity.super.onBackPressed();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
