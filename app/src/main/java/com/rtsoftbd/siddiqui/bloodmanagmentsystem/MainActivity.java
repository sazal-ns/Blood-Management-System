package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.Config;
import helper.ConnectionDetect;
import helper.ShowDialog;
import models.FBUsers;
import models.SAddr;
import models.User;
import models.Users;

public class MainActivity extends AppCompatActivity {

    MaterialSpinner groupSpinner;
    Button searchBloodImageButton;
    Button loginButton, singUpButton;

    TextView aboutUS, ourGoalTextView;

    AutoCompleteTextView areaET, districtET;

    private LoginButton fb_login_button;
    private CallbackManager callbackManager;

    ConnectionDetect cd;
    boolean fb = false;

    private List<String> areas = new ArrayList<>();
    private List<String> districts = new ArrayList<>();

    private String bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        cd = new ConnectionDetect(this);

        bloodGroup = "";

        callbackManager= CallbackManager.Factory.create();
        fb_login_button = (LoginButton) findViewById(R.id.fb_login_button);
        fb_login_button.setReadPermissions("public_profile", "email");

        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.e("response: ", response + "");
                                Log.e("response object: ", object.toString() + "");
                            try {
                                FBUsers.setId(object.getString("id"));
                                FBUsers.setEmail(object.getString("email"));
                                FBUsers.setName(object.getString("name"));
                                FBUsers.setImageLink("https://graph.facebook.com/"+object.getString("id")+"/picture?type=large");
                                FBUsers.setIsFB("true");
                                FBUsers.setFirst_name(object.getString("first_name"));
                                JSONObject jsonObject = object.getJSONObject("age_range");
                                FBUsers.setAge_range(jsonObject.getString("min"));
                                FBUsers.setIsFB("true");
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                                LoginManager.getInstance().logOut();
                                    doRegistration(FBUsers.getName(),FBUsers.getFirst_name(),String.valueOf(FBUsers.getId()),"","","","","",
                                            FBUsers.getAge_range(),"","user");
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,age_range,email,name,first_name");
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

        preInIt();
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
            new MaterialDialog.Builder(MainActivity.this)
                    .title("OUR GOAL")
                    .customView(ourGoal(), true)
                    .show();
        }else if (id == R.id.menu_about_us){
            showAboutUS();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doRegistration(final String donor, final String userName, final String password, final String mobile,
                                final String area, final String thana, final String union, final String district, final String age,
                                final String bloodGroup, final String user_type) {
        Log.e("doReg","##############");

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
                        User.setImageLink(object.getString("image"));

                        doLogin(userName,password);

                    }else{
                        doLogin(userName,password);

                    }
                } catch (JSONException e) {
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
                    params.put("dname", donor);
                    params.put("username", userName);
                    params.put("password", password);
                    params.put("user_type", user_type);
                    params.put("mobile", mobile);
                    params.put("area", area);
                    params.put("thana", thana);
                    params.put("union", union);
                    params.put("district", district);
                    params.put("age", age);
                    params.put("bloodg", bloodGroup);
                    params.put("image",FBUsers.getImageLink());

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void doLogin(final String userName, final String password){

        Log.e("doLogin","******************");
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
                        User.setMobile("0"+object.getString("mobile"));
                        User.setArea(object.getString("area"));
                        User.setThana(object.getString("thana"));
                        User.setUnion(object.getString("union"));
                        User.setDistrict(object.getString("district"));
                        User.setAge(object.getString("age"));
                        User.setBloodg(object.getString("bloodg"));
                        User.setImageLink(object.getString("image"));

                        Intent intent = new Intent(MainActivity.this, UserProfile.class);
                        startActivity(intent);


                    }else{
                        new ShowDialog(MainActivity.this, "Error", "You changed password.\n Please login by clicked login \n Your user name is"+FBUsers.getFirst_name(),getResources().getDrawable(R.drawable.ic_error_red_24dp));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing App")
                .setMessage("Are you sure you want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        /*fb_login_button.setReadPermissions("public_profile", "email","user_friends"
        fb_login_button.setPressed(true);
        fb_login_button.invalidate();
        //fb_login_button.registerCallback(callbackManager, mCallback);
        fb_login_button.setPressed(false);
        fb_login_button.invalidate();*/
    }

    private void preInIt() {

        groupSpinner = (MaterialSpinner) findViewById(R.id.groupSpinner);
        groupSpinner.setItems(getResources().getStringArray(R.array.blood_group));
        groupSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                bloodGroup = item;
                Log.d("Blood",item + "-->" + bloodGroup);
            }
        });

        searchBloodImageButton = (Button) findViewById(R.id.searchBloodImageButton);

        /*aboutUS = (TextView) findViewById(R.id.aboutusTextView);
        ourGoalTextView = (TextView) findViewById(R.id.ourGoalTextView);
        aboutUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutUS();
            }
        });

        ourGoalTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(MainActivity.this)
                        .title("OUR GOAL")
                        .customView(ourGoal(), wrapInScrollView)
                        .show();
            }
        });*/

        loginButton= (Button) findViewById(R.id.loginButton);
        singUpButton= (Button) findViewById(R.id.singUpButton);
        areaET = (AutoCompleteTextView) findViewById(R.id.serachLoctionarea);
        districtET = (AutoCompleteTextView) findViewById(R.id.serachLoction);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SAddr.getAreas());
        areaET.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SAddr.getDistricts());
        districtET.setAdapter(adapter2);

        if (!cd.isConnected()) {
            showNetDisabledAlertToUser(this);
        }
        inIt();
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
                    if (bloodGroup.length()>0 && bloodGroup.length()<=3){
                        if (!areaET.getText().toString().isEmpty() || !districtET.getText().toString().isEmpty()){
                    Intent intent = new Intent(MainActivity.this, DonorActivity.class);
                    intent.putExtra("spinner",bloodGroup);
                            intent.putExtra("area", areaET.getText().toString());
                            intent.putExtra("dis", districtET.getText().toString());
                    startActivity(intent);
                        }
                        else
                            new ShowDialog(MainActivity.this, "Warning","Please provide area or district.",getResources().getDrawable(R.drawable.ic_warning_orange_24dp));

                    }
                    else new ShowDialog(MainActivity.this, "Warning","Please Select a Blood Group.",getResources().getDrawable(R.drawable.ic_warning_orange_24dp));
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
