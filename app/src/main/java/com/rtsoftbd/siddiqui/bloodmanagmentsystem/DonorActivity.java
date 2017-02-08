/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import config.Config;

public class DonorActivity extends AppCompatActivity {

    private TableLayout stk;
    float textSiz = (float) 15.0;

    private Intent intent;

    private ProgressDialog pDialog;
    private String call;

    private EditText t3v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        intent = getIntent();
        inIt();
    }

    private void inIt() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Sl.No ");
        tv0.setTextSize(textSiz);
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Donor Name ");
        tv1.setTextSize(textSiz);
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Mobile ");
        tv2.setTextSize(textSiz);
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" Age ");
        tv3.setTextSize(textSiz);
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setText(" Area ");
        tv4.setTextSize(textSiz);
        tv4.setTextColor(Color.WHITE);
        tbrow0.addView(tv4);
        TextView tv5 = new TextView(this);
        tv5.setText(" Union ");
        tv5.setTextSize(textSiz);
        tv5.setTextColor(Color.WHITE);
        tbrow0.addView(tv5);
        TextView tv6 = new TextView(this);
        tv6.setText(" Thana ");
        tv6.setTextSize(textSiz);
        tv6.setTextColor(Color.WHITE);
        tbrow0.addView(tv6);
        TextView tv7 = new TextView(this);
        tv7.setText(" District ");
        tv7.setTextSize(textSiz);
        tv7.setTextColor(Color.WHITE);
        tbrow0.addView(tv7);
        stk.addView(tbrow0);

        TextView textView = (TextView) findViewById(R.id.bloodGroupTextView);
        textView.setText("You Search For Blood Group " + intent.getStringExtra("spinner"));

        searchBlood(intent.getStringExtra("spinner"));
    }

    private void searchBlood(final String bloodGroup) {
        pDialog.setMessage("Loading...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_SEARCH_BLOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                hideDialog();
                try {
                    jsonObject = new JSONObject(response);

                    Iterator keys = jsonObject.keys();
                    int i = 1;
                    while (keys.hasNext()) {
                        String dynamicKey = (String) keys.next();

                        if (!dynamicKey.contains("error")) {
                            JSONObject object = jsonObject.getJSONObject(dynamicKey);
                            Log.e("Donor List", object.toString());

                            TableRow tbrow = new TableRow(DonorActivity.this);
                            TextView t1v = new TextView(DonorActivity.this);
                            t1v.setText("" + i);
                            t1v.setTextSize(textSiz);
                            t1v.setPadding(5, 0, 5, 0);
                            t1v.setTextColor(Color.WHITE);
                            t1v.setGravity(Gravity.CENTER);
                            tbrow.addView(t1v);
                            TextView t2v = new TextView(DonorActivity.this);
                            t2v.setText(object.getString("dname"));
                            t2v.setTextSize(textSiz);
                            t2v.setPadding(5, 0, 5, 0);
                            t2v.setTextColor(Color.WHITE);
                            t2v.setGravity(Gravity.CENTER);
                            tbrow.addView(t2v);
                            t3v = new EditText(DonorActivity.this);
                            t3v.setText("0" + object.getString("mobile"));
                            call = t3v.getText().toString().trim();
                            t3v.setTextSize(textSiz);
                            t3v.setPadding(5, 0, 5, 0);
                            if (i % 2 == 0)
                                t3v.setTextColor(Color.RED);
                            else
                                t3v.setTextColor(Color.CYAN);
                            t3v.setGravity(Gravity.CENTER);
                            tbrow.addView(t3v);
                            TextView t4v = new TextView(DonorActivity.this);
                            t4v.setText(object.getString("age"));
                            t4v.setTextSize(textSiz);
                            t4v.setPadding(5, 0, 5, 0);
                            t4v.setTextColor(Color.WHITE);
                            t4v.setGravity(Gravity.CENTER);
                            tbrow.addView(t4v);
                            TextView t5v = new TextView(DonorActivity.this);
                            t5v.setText(object.getString("area"));
                            t5v.setTextSize(textSiz);
                            t5v.setPadding(5, 0, 5, 0);
                            t5v.setTextColor(Color.WHITE);
                            t5v.setGravity(Gravity.CENTER);
                            tbrow.addView(t5v);
                            TextView t6v = new TextView(DonorActivity.this);
                            t6v.setText(object.getString("thana"));
                            t6v.setTextSize(textSiz);
                            t6v.setPadding(5, 0, 5, 0);
                            t6v.setTextColor(Color.WHITE);
                            t6v.setGravity(Gravity.CENTER);
                            tbrow.addView(t6v);
                            TextView t7v = new TextView(DonorActivity.this);
                            t7v.setText(object.getString("union"));
                            t7v.setTextSize(textSiz);
                            t7v.setPadding(5, 0, 5, 0);
                            t7v.setTextColor(Color.WHITE);
                            t7v.setGravity(Gravity.CENTER);
                            tbrow.addView(t7v);
                            TextView t8v = new TextView(DonorActivity.this);
                            t8v.setText(object.getString("district"));
                            t8v.setTextSize(textSiz);
                            t8v.setPadding(5, 0, 5, 0);
                            t8v.setTextColor(Color.WHITE);
                            t8v.setGravity(Gravity.CENTER);
                            tbrow.addView(t8v);
                            stk.addView(tbrow);

                            i++;
                        }
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
                Log.e("Search error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("bloodg", bloodGroup);
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
