/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import config.Config;
import helper.ConnectionDetect;
import models.User;
import models.Users;

public class DonorActivity extends AppCompatActivity {

    private Intent intent;

    private ProgressDialog pDialog;

    private ListView listView;
    private List<Users> usersList = new ArrayList<>();
    private CustomListAdapter adapter;

    ConnectionDetect cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);

        listView = (ListView) findViewById(R.id.list);

        adapter = new CustomListAdapter(this, usersList);
        listView.setAdapter(adapter);

        cd = new ConnectionDetect(this);
        intent = getIntent();
        if (!cd.isConnected()) {
            showNetDisabledAlertToUser(this);
        }
        inIt();

        listView.setClickable(true);


       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Users u = (Users) parent.getSelectedItem();
Log.e("onItemClick",u.getMobile());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String phoneNumber = u.getMobile();
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(DonorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });*/

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Users u = (Users) parent.getItemAtPosition(position);
                Log.e("onItemClick",u.getMobile());
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String phoneNumber = u.getMobile();
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(DonorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("onItemClick","do nothing");
            }
        });
    }

    private void inIt() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        TextView textView = (TextView) findViewById(R.id.bloodGroupTextView);
        textView.setText("You Search For Blood Group " + intent.getStringExtra("spinner"));

        Users users = new Users();
        users.setSl("Sl.No");
        users.setDname("Donor Name");
        users.setMobile("Mobile Number");
        users.setAge("Age");
        users.setArea("Area");
        users.setThana("Thana");
        users.setUnion("Union");
        users.setDistrict("District");

        usersList.add(users);

        if (!cd.isConnected()) {
            showNetDisabledAlertToUser(this);
        } else
            searchBlood(intent.getStringExtra("spinner"));
    }

    private void searchBlood(final String bloodGroup) {
        pDialog.setMessage("Loading...");
        showDialog();
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_SEARCH_BLOOD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int i=1;
                JSONObject jsonObject;
                hideDialog();
                try {
                    jsonObject = new JSONObject(response);

                    Iterator keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String dynamicKey = (String) keys.next();

                        if (!dynamicKey.contains("error")) {
                            JSONObject object = jsonObject.getJSONObject(dynamicKey);
                            Log.e("Donor List", object.toString());

                            Users users = new Users();
                            users.setSl(String.valueOf(i));
                            users.setDname(object.getString("dname"));
                            users.setMobile("0" + object.getString("mobile"));
                            users.setAge(object.getString("age"));
                            users.setArea(object.getString("area"));
                            users.setThana(object.getString("thana"));
                            users.setUnion(object.getString("union"));
                            users.setDistrict(object.getString("district"));

                            usersList.add(users);

                            i++;
                        }
                    }
                    adapter.notifyDataSetChanged();
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


    public void callNumber(View view) {
        if (view != null) { // view is the button tapped
            View parent = (View) view.getParent(); // this should be the LinearLayout
            if (parent instanceof LinearLayout) {
                TextView unItemVal = (TextView) parent.findViewById(R.id.headMobileNumber);
                if (unItemVal != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    String phoneNumber = unItemVal.getText().toString();
                    callIntent.setData(Uri.parse("tel:" + phoneNumber));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        }
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
