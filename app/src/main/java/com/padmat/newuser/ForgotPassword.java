package com.padmat.newuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.padmat.newuser.R;

import com.padmat.newuser.extra.ServerLinks;
import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.ViewDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {

    SessionManager session;
    ViewDialog progressbar;
    ImageView iv_back;
    TextInputEditText ed_email;
    TextView btn_send;
    String um;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        session = new SessionManager(this);
        progressbar = new ViewDialog(this);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ed_email = findViewById(R.id.ed_email);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_email.getText().toString().isEmpty()) {
                    ed_email.setError("Enter Valid Mobile No");

                } else if (ed_email.getText().toString().length() != 10) {
                    ed_email.setError("Enter 10 Digit Mobile No");

                }else{

                    um = ed_email.getText().toString();
                    SendPWd(um);
                }
            }
        });
    }

    public void SendPWd(String um) {
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
                                i.putExtra("mobileno",um);
                                i.putExtra("classname","ForgotPassword");
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
                params.put("mobile", um);

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
}