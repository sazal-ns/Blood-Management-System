/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.Config;
import helper.ShowDialog;
import models.SAddr;
import models.User;

public class AddHistoryActivity extends AppCompatActivity {

    @BindView(R.id.dateEditText) EditText ms_DateEditText;
    @BindView(R.id.hospitalEditText) AutoCompleteTextView ms_HospitalEditText;
    @BindView(R.id.quantityEditText) EditText ms_QuantityEditText;
    @BindView(R.id.submitAppCompatButton) AppCompatButton ms_SubmitAppCompatButton;

    String donationDate, hospitalName, quantity;

    private  Calendar calendar;
    private SimpleDateFormat df;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_history);
        ButterKnife.bind(this);

        StringRequest request = new StringRequest(Request.Method.POST, Config.HOSPITAL_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                Log.i("data",response);
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
                                    SAddr.setHospital(object.getString("hospital"));
                                }catch (Exception e ){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("NoConnectionError")) {
                    new AlertDialog.Builder(AddHistoryActivity.this)
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


        calendar = Calendar.getInstance();
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = df.format(calendar.getTime());

        ms_DateEditText.setText(formattedDate);
        donationDate = ms_DateEditText.getText().toString().trim();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, SAddr.getHospital());
        ms_HospitalEditText.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Working....");
    }

    @OnClick({R.id.dateEditText, R.id.submitAppCompatButton})
    public void ms_OnClick(View view) {
        switch (view.getId()) {
            case R.id.dateEditText:
                new DatePickerDialog(this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.submitAppCompatButton:
                getData();
                break;
        }
    }


    private void getData() {
        hospitalName = ms_HospitalEditText.getText().toString().trim();
        quantity = ms_QuantityEditText.getText().toString().trim();

        add();
    }

    private void add() {
        if (!valid()) return;

        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Config.HISTORYADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("response", response);

                    if (jsonObject.toString().contains("false")){
                        new MaterialDialog.Builder(AddHistoryActivity.this)
                                .content("Successfully Update")
                                .show();

                        ms_HospitalEditText.setText("");
                        ms_QuantityEditText.setText("");


                    }else{
                        new ShowDialog(AddHistoryActivity.this, "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("userId",User.getId());
                params.put("hospital",hospitalName);
                params.put("date",donationDate);
                params.put("quantity", quantity);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    private boolean valid() {
        boolean valid = true;

        if (donationDate.isEmpty()){
            ms_DateEditText.setError("date needed");
            valid = false;
        }else ms_DateEditText.setError(null);

        if (quantity.isEmpty()){
            ms_QuantityEditText.setError("need quantity of blood in bag");
            valid = false;
        }else ms_QuantityEditText.setError(null);

        if (hospitalName.isEmpty()){
            ms_HospitalEditText.setError("provide hospital name");
            valid = false;
        }else ms_HospitalEditText.setError(null);

        try {
            int t = Integer.parseInt(quantity);
            ms_QuantityEditText.setError(null);
        }catch (Exception e ){
            ms_QuantityEditText.setError("provide only number");
            valid = false;
        }

        return valid;
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            setDate();
        }

    };

    private void setDate() {
        String formattedDate = df.format(calendar.getTime());

        ms_DateEditText.setText(formattedDate);
        donationDate = ms_DateEditText.getText().toString().trim();
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
