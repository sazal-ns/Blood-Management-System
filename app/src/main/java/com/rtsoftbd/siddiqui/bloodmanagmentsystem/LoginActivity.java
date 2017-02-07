package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private Button singInButton, singUpButton;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        preInIt();

    }

   private void preInIt(){
       userNameEditText = (EditText) findViewById(R.id.userNameEditText);
       passwordEditText = (EditText) findViewById(R.id.passwordEditText);

       singInButton = (Button) findViewById(R.id.singInButton);
       singUpButton = (Button) findViewById(R.id.singUpButton);

       rememberMeCheckBox = (CheckBox) findViewById(R.id.rememberMeCheckBox);

       inIt();
    }

    private void inIt(){
        singInButton.setOnClickListener(onClickListener);
        singUpButton.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.singInButton:
                    doLogin(userNameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
                    break;
                case R.id.singUpButton:
                    break;
            }
        }
    };

    private void doLogin(final String userName, final String password){
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_LOGIN, new Response.Listener<String>() {
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
                        User.setMobile(object.getString("mobile"));
                        User.setArea(object.getString("area"));
                        User.setThana(object.getString("thana"));
                        User.setUnion(object.getString("union"));
                        User.setDistrict(object.getString("district"));
                        User.setAge(object.getString("age"));
                        User.setBloodg(object.getString("bloodg"));

                        Log.d("Loging",object.toString());

                    }else{
                        new ShowDialog(LoginActivity.this, "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("doLogin onErrorResponse", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userName",userName);
                params.put("password", password);
                //Log.d("info", userName+"->"+password);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
