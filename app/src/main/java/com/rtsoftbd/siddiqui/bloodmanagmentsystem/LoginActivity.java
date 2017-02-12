package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import config.Config;
import helper.ConnectionDetect;
import helper.ShowDialog;
import models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private Button singInButton, singUpButton;
    private LoginButton fb_login_button;

    private ProgressDialog pDialog;
    private CallbackManager callbackManager;

    ConnectionDetect cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        cd = new ConnectionDetect(this);

        callbackManager= CallbackManager.Factory.create();
        preInIt();

    }

   private void preInIt(){

       pDialog = new ProgressDialog(this);
       pDialog.setCancelable(false);

       userNameEditText = (EditText) findViewById(R.id.userNameEditText);
       passwordEditText = (EditText) findViewById(R.id.passwordEditText);
       fb_login_button = (LoginButton) findViewById(R.id.fb_login_button);

       singInButton = (Button) findViewById(R.id.singInButton);
       singUpButton = (Button) findViewById(R.id.singUpButton);
       if (!cd.isConnected()) {
           showNetDisabledAlertToUser(this);
       }

       inIt();
    }

    private void inIt(){
        singInButton.setOnClickListener(onClickListener);
        singUpButton.setOnClickListener(onClickListener);

        fb_login_button.setReadPermissions("public_profile", "email","user_friends");
        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.e("response: ", response + "");
                                Log.e("response object: ", object.toString() + "");
                            /*try {
                                user = new FbUser();
                                user.facebookID = object.getString("id");
                                user.email = object.getString("email");
                                user.name = object.getString("name");
                                user.gender = object.getString("gender");
                                user.first_name = object.getString("first_name");
                                user.last_name = object.getString("last_name");
                                PrefUtils.setCurrentUser(user,LoginActivity.this);
                                User.setLoginType(1);
                                registerUser(FbUser.facebookID, FbUser.email, FbUser.facebookID);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            User.setImageLink("null");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //intent.putExtra("fb","itIs");
                            startActivity(intent);
                            finish();*/
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,employee_number,locale,location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("*****Cancel****","On cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("****Error****",error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.singInButton:
                    if (!cd.isConnected()) {
                        showNetDisabledAlertToUser(LoginActivity.this);
                    }else
                    doLogin(userNameEditText.getText().toString().trim(), passwordEditText.getText().toString().trim());
                    break;
                case R.id.singUpButton:
                    startActivity(new Intent(LoginActivity.this, SingUpActivity.class));
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        fb_login_button.setReadPermissions("public_profile", "email","user_friends");
        fb_login_button.performClick();
        fb_login_button.setPressed(true);
        fb_login_button.invalidate();
        //fb_login_button.registerCallback(callbackManager, mCallback);
        fb_login_button.setPressed(false);
        fb_login_button.invalidate();
    }

    private void doLogin(final String userName, final String password){
        pDialog.setMessage("Logging in...");
        showDialog();

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonObject;
                hideDialog();
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

                        Intent intent = new Intent(LoginActivity.this, UserProfile.class);
                        startActivity(intent);
                        finish();

                    }else{
                        hideDialog();
                        new ShowDialog(LoginActivity.this, "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
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
