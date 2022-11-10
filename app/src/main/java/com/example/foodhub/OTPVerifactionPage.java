package com.example.foodhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.extra.ViewDialog;
import com.example.foodhub.fragment.LoginPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTPVerifactionPage extends AppCompatActivity {

    TextView text_Timer,resend_OTP,resendtext,mobileNumber;
    Button btn_verifayOtp;
    SessionManager sessionManager;
    String userOTP,userMobileNo,classname,str_MobileNo;
    OtpTextView otp_view;
    ViewDialog progressbar;
    String str_UserFullName, str_MobileNumber, str_EmailId, str_UserName, str_Password,OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverifaction_page);

        progressbar = new ViewDialog(this);
        /*Window window = OTPVerifactionPage.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(OTPVerifactionPage.this, R.color.white));*/

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        text_Timer = findViewById(R.id.timer);
        resend_OTP = findViewById(R.id.resend_OTP);
        btn_verifayOtp = findViewById(R.id.btn_verifayOtp);
        resendtext = findViewById(R.id.resendtext);
        otp_view = findViewById(R.id.otp_view);
        mobileNumber = findViewById(R.id.mobileNumber);

        classname = getIntent().getStringExtra("classname");

        if(classname.equals("ForgotPassword")){

            str_MobileNo = getIntent().getStringExtra("mobileno");
            classname = getIntent().getStringExtra("classname");

        }else {

            str_UserFullName = getIntent().getStringExtra("str_UserFullName");
            str_MobileNumber = getIntent().getStringExtra("str_MobileNumber");
            str_EmailId = getIntent().getStringExtra("str_EmailId");
            str_Password = getIntent().getStringExtra("str_Password");
            OTP = getIntent().getStringExtra("OTP");

            mobileNumber.setText(str_MobileNumber);

        }

        mobileNumber.setText(str_MobileNo);

        btn_verifayOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(otp_view.getOTP().length() != 4){

                    Toast.makeText(OTPVerifactionPage.this, "Enter Valide Otp", Toast.LENGTH_SHORT).show();

                }else{

                    if(classname.equals("ForgotPassword")){

                        String getOTP = otp_view.getOTP();
                        verifayOtp(str_MobileNo,getOTP);

                    }else{

                        String getOTP = otp_view.getOTP();

                        if(OTP.equals(getOTP)){

                            registerUser(str_UserFullName,str_MobileNumber,str_EmailId,str_MobileNumber,str_Password);

                        }else{

                            Toast.makeText(OTPVerifactionPage.this, "OTP Not Match", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });

        resend_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(str_MobileNo.length() != 0){

                    SendPWd();

                }else{

                    Toast.makeText(OTPVerifactionPage.this, "Mobile No not provide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        timer();
    }

    public void verifayOtp(String mobileNo,String OTP){
        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.otpveryfy, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              progressbar.hideDialog();
                Log.d("Ranjeet_verfyOtp",response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    if(success.equals("true")){

                        String msg = jsonObject.getString("msg");

                        Toast.makeText(OTPVerifactionPage.this, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(OTPVerifactionPage.this,ChangePassword.class);
                        intent.putExtra("mobileno",mobileNo);
                        startActivity(intent);

                    }else{

                        String msg = jsonObject.getString("msg");

                        Toast.makeText(OTPVerifactionPage.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressbar.hideDialog();

                error.printStackTrace();
                Toast.makeText(OTPVerifactionPage.this, "Facing Technical issues, Try again!", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("mobile",mobileNo);
                params.put("otp",OTP);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerifactionPage.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void SendPWd() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.forgetpasswordotp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                String msg = jsonObject.getString("msg");

                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), OTPVerifactionPage.class);
                                i.putExtra("mobileno",str_MobileNo);
                                startActivity(i);
                                finish();

                            } else {
                                String msg = jsonObject.getString("msg");


                                progressbar.hideDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

//
                            }
                        } catch (JSONException e) {
                            progressbar.hideDialog();
                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", str_MobileNo);

                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public void timer(){

        //Initialize time duration
        long duration = TimeUnit.MINUTES.toMillis(1);
        //Initialize countdown timer

        new CountDownTimer(duration, 5) {
            @Override
            public void onTick(long millisUntilFinished) {

                //When tick
                //Convert millisecond to minute and second

                String sDuration = String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                text_Timer.setText(sDuration);

            }

            @Override
            public void onFinish() {

                text_Timer.setVisibility(View.GONE);
                resendtext.setVisibility(View.GONE);
                resend_OTP.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    public void registerUser(String fullname, String contact,String mail,String username,String password) {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.signup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String success = jsonObject.getString("success");

                    if (success.equalsIgnoreCase("true")) {

                        String msg = jsonObject.getString("msg");
                        Toast.makeText(OTPVerifactionPage.this, msg, Toast.LENGTH_SHORT).show();
                        Toast.makeText(OTPVerifactionPage.this, "Login to continue", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(OTPVerifactionPage.this,MainActivity.class);
                        startActivity(intent);

                    } else {

                        String msg = jsonObject.getString("msg");
                        progressbar.hideDialog();
                        Toast.makeText(OTPVerifactionPage.this, msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressbar.hideDialog();
                Log.d("error_response", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(OTPVerifactionPage.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OTPVerifactionPage.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("fullname", fullname);
                params.put("contact", contact);
                params.put("mail", mail);
                params.put("username", username);
                params.put("password", password);

                Log.d("paramsforhomeapi", "" + params);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OTPVerifactionPage.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }
}