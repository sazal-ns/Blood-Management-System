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
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import config.Config;
import helper.ConnectionDetect;
import helper.LocationAddress;
import helper.LocationService;
import helper.ShowDialog;
import models.FBUsers;
import models.SAddr;
import models.User;

public class SingUpActivity extends AppCompatActivity {

    private EditText donorNameEditText, mobileEditText;
    private MaterialSpinner groupSpinner;
    private Button singUpButtonD;

    private AutoCompleteTextView areaEditText, districtEditText;

    //LocationService appLocationService;

    private ProgressDialog pDialog;
    ConnectionDetect cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        cd = new ConnectionDetect(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (!cd.isConnected()) {
            showNetDisabledAlertToUser(this);
        }

        //appLocationService= new LocationService(SingUpActivity.this);

        /*Location networkLocation= appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation!= null)
        {
            double latitude = networkLocation.getLatitude();
            double longitude = networkLocation.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude , longitude , getApplicationContext(), new GeocoderHandler());
        }*/

        preInIt();
    }
    private String bloodGroup;
    private void preInIt() {
bloodGroup="";
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        groupSpinner = (MaterialSpinner) findViewById(R.id.groupSpinner);
        groupSpinner.setItems(getResources().getStringArray(R.array.blood_group));
        groupSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                bloodGroup = item;
            }
        });

        donorNameEditText = (EditText) findViewById(R.id.donorNameEditText);
        mobileEditText = (EditText) findViewById(R.id.mobileEditText);

        areaEditText = (AutoCompleteTextView) findViewById(R.id.thanaEditText);
        districtEditText = (AutoCompleteTextView) findViewById(R.id.districtEditText);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SAddr.getAreas());
        areaEditText.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SAddr.getDistricts());
        districtEditText.setAdapter(adapter2);

        if (FBUsers.getIsFB().contains("true")){
            donorNameEditText.setText(FBUsers.getName());
        }

        /*try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                areaEditText.setText(strReturnedAddress.toString());
            }
            else{
                areaEditText.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            areaEditText.setText("Canon't get Address!");
        }*/
            /*areaEditText.setText(User.getArea());
            districtEditText.setText(User.getDistrict());*/

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mobileEditText.setText(tMgr.getLine1Number());

        singUpButtonD = (Button) findViewById(R.id.singUnButtonD);
        singUpButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bloodGroup.length()>0 && bloodGroup.length()<=3){
                    if (donorNameEditText.getText().toString().isEmpty()  || mobileEditText.getText().toString().isEmpty() ||
                            areaEditText.getText().toString().isEmpty() || districtEditText.getText().toString().isEmpty())
                        new ShowDialog(SingUpActivity.this, "Error!!!", "Please Provide All Information", getResources().getDrawable(R.drawable.ic_error_red_24dp));

                    else if (!cd.isConnected()) {
                        showNetDisabledAlertToUser(SingUpActivity.this);
                    } else
                        doRegistration(donorNameEditText.getText().toString().trim(), mobileEditText.getText().toString().trim(),
                                areaEditText.getText().toString().trim(), districtEditText.getText().toString().trim(),bloodGroup,"user");
                }else new ShowDialog(SingUpActivity.this, "Warning","Please Select a Blood Group.",getResources().getDrawable(R.drawable.ic_warning_orange_24dp));

            }
        });
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


    private void doRegistration(final String donor, final String mobile, final String thana, final String district,
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
                        hideDialog();
                        Intent intent = new Intent(SingUpActivity.this, MoreActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        hideDialog();
                        Intent intent = new Intent(SingUpActivity.this, MoreActivity.class);
                        startActivity(intent);
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
                if (FBUsers.getIsFB().contains("false")) {
                    params.put("dname", donor);
                    params.put("user_type", user_type);
                    params.put("mobile", mobile);
                    params.put("username", mobile);
                    params.put("thana", thana);
                    params.put("district", district);
                    params.put("bloodg", bloodGroup);
                }else {
                    params.put("dname", FBUsers.getName());
                    params.put("user_type", user_type);
                    params.put("mobile", mobile);
                    params.put("thana", thana);
                    params.put("district", district);
                    params.put("bloodg", bloodGroup);
                    params.put("image",FBUsers.getImageLink());
                }
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

   /* private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress= bundle.getString("address");
                    break;
                default:
                    locationAddress= null;
            }
            districtEditText.setText(locationAddress);
        }
    }*/

}
