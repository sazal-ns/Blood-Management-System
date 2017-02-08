package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

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
import java.util.Map;

import config.Config;
import helper.ConnectionDetect;

public class MainActivity extends AppCompatActivity {

    Spinner groupSpinner;
    ImageButton searchBloodImageButton;
    Button loginButton, singUpButton;

    ConnectionDetect cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        cd = new ConnectionDetect(this);

        preInIt();
    }

    private void preInIt() {
        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);

        searchBloodImageButton = (ImageButton) findViewById(R.id.searchBloodImageButton);

        loginButton= (Button) findViewById(R.id.loginButton);
        singUpButton= (Button) findViewById(R.id.singUpButton);
        if (!cd.isConnected()) {
            showNetDisabledAlertToUser(this);
        }
        inIt();
    }

    private void inIt() {
        searchBloodImageButton.setOnClickListener(onClickListener);
        loginButton.setOnClickListener(onClickListener);
        singUpButton.setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.loginButton:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
                case R.id.singUpButton:
                    startActivity(new Intent(MainActivity.this, SingUpActivity.class));
                    break;
                case R.id.searchBloodImageButton:
                    Intent intent = new Intent(MainActivity.this, DonorActivity.class);
                    intent.putExtra("spinner",String.valueOf(groupSpinner.getSelectedItem()));
                    startActivity(intent);
                    break;
            }
        }
    };

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
}
