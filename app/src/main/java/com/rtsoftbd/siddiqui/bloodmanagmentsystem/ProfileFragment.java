/*
 * Copyright (c) 2017. By RTSoftBD.
 * Author: Noor Nabiul Alam Siddiqui
 */

package com.rtsoftbd.siddiqui.bloodmanagmentsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.Config;
import helper.AppController;
import helper.ShowDialog;
import models.FBUsers;
import models.SAddr;
import models.User;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.thumbnail) NetworkImageView ms_Thumbnail;
    @BindView(R.id.fb_login_button) LoginButton ms_FbLoginButton;
    @BindView(R.id.donorNameEditText) EditText ms_DonorNameEditText;
    @BindView(R.id.timeAgoTextView) TextView ms_TimeAgoTextView;
    @BindView(R.id.phoneEditText) EditText ms_PhoneEditText;
    @BindView(R.id.thanaAutoCompleteTextView) AutoCompleteTextView ms_ThanaAutoCompleteTextView;
    @BindView(R.id.ageEditText) EditText ms_AgeEditText;
    @BindView(R.id.disAutoCompleteTextView) AutoCompleteTextView ms_DisAutoCompleteTextView;
    @BindView(R.id.groupSpinner) MaterialSpinner ms_GroupSpinner;
    @BindView(R.id.updateButton) AppCompatButton ms_UpdateButton;

    private String donorName, phone, thana, age, district, bloodGroup;

    private ProgressDialog progressDialog;

    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private  CallbackManager callbackManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        callbackManager = CallbackManager.Factory.create();
        ms_FbLoginButton.setFragment(this);
        ms_FbLoginButton.setReadPermissions("public_profile", "email");

        final String[] blood = getResources().getStringArray(R.array.blood_group);
        ms_GroupSpinner.setItems(getResources().getStringArray(R.array.blood_group));
        ms_GroupSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                bloodGroup = item;
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SAddr.getAreas());
        ms_ThanaAutoCompleteTextView.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SAddr.getDistricts());
        ms_DisAutoCompleteTextView.setAdapter(adapter2);

        ms_DonorNameEditText.setText(User.getDname());
        ms_PhoneEditText.setText(User.getMobile());
        ms_AgeEditText.setText(User.getAge());
        ms_ThanaAutoCompleteTextView.setText(User.getThana());
        ms_DisAutoCompleteTextView.setText(User.getDistrict());

        for (int i=0; i< blood.length; i++){
            if (blood[i].contentEquals(User.getBloodg()))
            ms_GroupSpinner.setSelectedIndex(i);
        }
        bloodGroup = User.getBloodg();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Working....");

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        loadImage();

        return view;
    }

    private void loadImage() {
        if (User.getImageLink().contentEquals("null")) {
            if (User.getBloodg().contentEquals("A+"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Apos.png", imageLoader);
            else if (User.getBloodg().contentEquals("A-"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Aneg.png", imageLoader);
            else if (User.getBloodg().contentEquals("B+"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Bpos.png", imageLoader);
            else if (User.getBloodg().contentEquals("B-"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Bneg.png", imageLoader);
            else if (User.getBloodg().contentEquals("O+"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Opos.png", imageLoader);
            else if (User.getBloodg().contentEquals("O-"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/Oneg.png", imageLoader);
            else if (User.getBloodg().contentEquals("AB+"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/ABpos.png", imageLoader);
            else if (User.getBloodg().contentEquals("AB-"))
                ms_Thumbnail.setImageUrl("http://api.rtsoftbd.us/blood/img/ABneg.png", imageLoader);
        }else {
            ms_Thumbnail.setImageUrl(User.getImageLink(), imageLoader);
            ms_FbLoginButton.setVisibility(View.GONE);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R.id.fb_login_button, R.id.updateButton})
    public void ms_OnClick(View view) {
        switch (view.getId()) {
            case R.id.fb_login_button:
                getFbPic();
                break;
            case R.id.updateButton:
                getData();
                break;
        }
    }

    private void getFbPic() {

        ms_FbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
                                    User.setImageLink("https://graph.facebook.com/"+object.getString("id")+"/picture?type=large");
                                    FBUsers.setIsFB("true");
                                    FBUsers.setFirst_name(object.getString("first_name"));
                                    JSONObject jsonObject = object.getJSONObject("age_range");
                                    FBUsers.setAge_range(jsonObject.getString("min"));
                                    FBUsers.setIsFB("true");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                LoginManager.getInstance().logOut();
                                ms_FbLoginButton.setVisibility(View.GONE);
                                loadImage();
                                updateImg();
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

    }

    private void updateImg() {
        StringRequest request = new StringRequest(Request.Method.POST, Config.fbImg, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("response", response);

                    if (jsonObject.toString().contains("false")){
                        new MaterialDialog.Builder(getContext())
                                .content("Successfully Update")
                                .show();
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
                        User.setEmail(object.getString("email"));

                        ms_DonorNameEditText.setText(User.getDname());
                        ms_PhoneEditText.setText(User.getMobile());
                        ms_AgeEditText.setText(User.getAge());
                        ms_ThanaAutoCompleteTextView.setText(User.getThana());
                        ms_DisAutoCompleteTextView.setText(User.getDistrict());

                    }else{
                        new ShowDialog(getContext(), "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
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

                params.put("id",User.getId());
                params.put("image",User.getImageLink());

                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void getData() {
        donorName = ms_DonorNameEditText.getText().toString().trim();
        phone = ms_PhoneEditText.getText().toString().trim();
        age = ms_AgeEditText.getText().toString().trim();
        thana = ms_ThanaAutoCompleteTextView.getText().toString().trim();
        district = ms_DisAutoCompleteTextView.getText().toString().trim();

        update();
    }

    private void update() {
        if (!valid()){

            return;}
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Config.URL_UPDATE_USER_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    Log.e("response", response);

                    if (jsonObject.toString().contains("false")){
                        new MaterialDialog.Builder(getContext())
                                .content("Successfully Update")
                                .show();
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
                        User.setEmail(object.getString("email"));

                        ms_DonorNameEditText.setText(User.getDname());
                        ms_PhoneEditText.setText(User.getMobile());
                        ms_AgeEditText.setText(User.getAge());
                        ms_ThanaAutoCompleteTextView.setText(User.getThana());
                        ms_DisAutoCompleteTextView.setText(User.getDistrict());

                    }else{
                        new ShowDialog(getContext(), "Error", jsonObject.getString("error_msg"),getResources().getDrawable(R.drawable.ic_error_red_24dp));
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

                params.put("id",User.getId());
                params.put("dname",donorName);
                params.put("mobile",phone);
                params.put("thana",thana);
                params.put("district",district);
                params.put("age",age);
                params.put("bloodg",bloodGroup);
                if (User.getImageLink().isEmpty())
                    params.put("image","");
                else
                    params.put("image",User.getImageLink());
                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
    }

    private boolean valid() {
        boolean valid = true;

        if (donorName.isEmpty()){
            valid = false;
            ms_DonorNameEditText.setError("give name");
        }else ms_DonorNameEditText.setError(null);

        if (phone.isEmpty() || phone.length()!= 11){
            valid = false;
            ms_PhoneEditText.setError("give valid phone number");
        }else ms_PhoneEditText.setError(null);

        if (age.isEmpty()){
            ms_AgeEditText.setError("give valid age");
            valid = false;
        } else
        if (Integer.valueOf(age) < 0 || Integer.valueOf(age) > 80){
            ms_AgeEditText.setError("give valid age");
            valid = false;
        }else ms_AgeEditText.setError(null);

        if (bloodGroup.length()>4){
            new MaterialDialog.Builder(getContext())
                    .content("Select Blood Group")
                    .show();
            valid = false;
        }

        if (thana.isEmpty()){
            valid = false;

            ms_ThanaAutoCompleteTextView.setError("provide thana name");
        }else  ms_ThanaAutoCompleteTextView.setError(null);

        if (district.isEmpty())
        {
            valid = false;
            ms_DisAutoCompleteTextView.setError("provide district name");
        }else  ms_DisAutoCompleteTextView.setError(null);


        return valid;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
