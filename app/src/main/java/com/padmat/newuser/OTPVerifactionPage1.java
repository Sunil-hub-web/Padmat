package com.padmat.newuser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.padmat.newuser.databinding.ActivityOtpverifactionPage1Binding;
import com.padmat.newuser.extra.ServerLinks;
import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTPVerifactionPage1 extends AppCompatActivity {

    String contact_otp, login_otp, classname, contact_otp1;
    ActivityOtpverifactionPage1Binding binding;
    SessionManager session;
    ViewDialog progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_otpverifaction_page1);

        binding = ActivityOtpverifactionPage1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressbar = new ViewDialog(OTPVerifactionPage1.this);
        session = new SessionManager(OTPVerifactionPage1.this);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        contact_otp = getIntent().getStringExtra("contact_otp");
        login_otp = getIntent().getStringExtra("login_otp");
        contact_otp1 = getIntent().getStringExtra("contact_otp1");

        classname = getIntent().getStringExtra("classname");

        timer();

        if (classname.equals("ForgotPassword")) {

            contact_otp = getIntent().getStringExtra("mobileno");
            classname = getIntent().getStringExtra("classname");

        } else {

            binding.mobileNumber.setText(contact_otp);

        }

        binding.mobileNumber.setText(contact_otp);

        binding.btnVerifayOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.otpView.getOTP().length() != 4) {

                    Toast.makeText(OTPVerifactionPage1.this, "Enter Valide Otp", Toast.LENGTH_SHORT).show();

                } else {

                    String getOTP = binding.otpView.getOTP();

                    if (getOTP.equals(login_otp)) {

                        userOTPPage(contact_otp1);
                    }

                }
            }
        });

        binding.resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(contact_otp1.length() != 0){

                    userLoginPage(contact_otp1);

                }else{

                    Toast.makeText(OTPVerifactionPage1.this, "Mobile No not provide", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void timer() {

        //Initialize time duration
        long duration = TimeUnit.MINUTES.toMillis(1);
        //Initialize countdown timer

        new CountDownTimer(duration, 5) {
            @Override
            public void onTick(long millisUntilFinished) {

                //When tick
                //Convert millisecond to minute and second

                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                binding.timer.setText(sDuration);

            }

            @Override
            public void onFinish() {

                binding.timer.setVisibility(View.GONE);
                binding.resendtext.setVisibility(View.GONE);
                binding.resendOTP.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    public void userOTPPage(String contact) {

        ProgressDialog progressDialog = new ProgressDialog(OTPVerifactionPage1.this);
        progressDialog.setMessage("Login please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.method_verifydetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    if (success.equalsIgnoreCase("true")) {

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        String contact = jsonObject.getString("contact");
                        String img = jsonObject.getString("img");

                        session.setUserID(id);
                        session.setUserName(name);
                        session.setUserEmail(email);
                        session.setUserPhonenumber(contact);
                        session.setLogin();

                        progressbar.hideDialog();
                        Intent i = new Intent(OTPVerifactionPage1.this, DeshBoard.class);
                        startActivity(i);
                        OTPVerifactionPage1.this.finish();

                    } else {

                        String msg = jsonObject.getString("msg");
                        progressbar.hideDialog();
                        Toast.makeText(OTPVerifactionPage1.this, msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(OTPVerifactionPage1.this, "" + error, Toast.LENGTH_SHORT).show();

/*                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginPage.this, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }*/
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("contactno", contact);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerifactionPage1.this);
        requestQueue.add(stringRequest);
    }

    public void userLoginPage(String contact) {

        ProgressDialog progressDialog = new ProgressDialog(OTPVerifactionPage1.this);
        progressDialog.setMessage("Login please wait");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.method_OTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    progressDialog.dismiss();

                    if (status.equals("success")) {

                        // Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();


                        contact_otp = jsonObject.getString("recipient");
                        login_otp = jsonObject.getString("otp");

                        // startActivity(new Intent(getActivity(), Otp.class));
//                        Intent intent = new Intent(OTPVerifactionPage1.this, OTPVerifactionPage1.class);
//                        intent.putExtra("message","Login");
//                        intent.putExtra("contact_otp",contact_otp);
//                        intent.putExtra("contact_otp1",contact);
//                        intent.putExtra("login_otp",login_otp);
//                        intent.putExtra("classname","classname");
//                        startActivity(intent);

                    } else {

                        String contact_otp = jsonObject.getString("recipient");
                        String login_otp = jsonObject.getString("otp");
                        Toast.makeText(OTPVerifactionPage1.this, "Invalide Mobile No", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(OTPVerifactionPage1.this, "" + error, Toast.LENGTH_SHORT).show();

/*                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(LoginPage.this, data, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }


                    }

                }*/
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("contactno", contact);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerifactionPage1.this);
        requestQueue.add(stringRequest);
    }
}