package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

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

public class MainActivity extends AppCompatActivity {

    Spinner groupSpinner;
    ImageButton searchBloodImageButton;
    Button loginButton, singUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preInIt();
    }

    private void preInIt() {
        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);

        searchBloodImageButton = (ImageButton) findViewById(R.id.searchBloodImageButton);

        loginButton= (Button) findViewById(R.id.loginButton);
        singUpButton= (Button) findViewById(R.id.singUpButton);

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
}
