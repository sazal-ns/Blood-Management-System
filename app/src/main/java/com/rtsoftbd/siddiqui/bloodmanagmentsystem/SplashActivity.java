/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import config.Config;
import models.SAddr;

public class SplashActivity extends AppCompatActivity  {

    ProgressBar ms_ProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ms_ProgressBar = (ProgressBar) findViewById(R.id.progressBar) ;

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_LOC, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;

                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.toString().contains("false")){

                        Iterator keys = jsonObject.keys();
                        while (keys.hasNext()) {
                            String dynamicKey = (String) keys.next();

                            if (!dynamicKey.contains("error")) {
                                JSONObject object = jsonObject.getJSONObject(dynamicKey);
                               // Log.e("Donor List", object.toString());

                               try {
                                   SAddr.setAreas(object.getString("area"));
                               }catch (Exception e ){
                                   e.printStackTrace();
                               }

                                try {
                                    SAddr.setDistricts(object.getString("district"));
                                }catch (Exception e ){
                                    e.printStackTrace();
                                }


                            }
                        }

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("NoConnectionError")) {
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle("Error")
                            .setMessage("No Active Internet Connection :(")
                            .show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("bloodg","Blood");

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
