package com.padmat.newuser.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.padmat.newuser.OTPVerifactionPage1;
import com.padmat.newuser.databinding.ActivityLoginPageBinding;
import com.padmat.newuser.databinding.LoginmobileBinding;
import com.padmat.newuser.extra.ServerLinks;
import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginOTP extends Fragment {

    LoginmobileBinding binding;
    SessionManager session;
    ViewDialog progressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LoginmobileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.editUserName.getText().toString().trim().equals("")){

                    Toast.makeText(getActivity(), "Please Enter Your Mobile No", Toast.LENGTH_SHORT).show();

                } else if (binding.editUserName.getText().toString().trim().length() !=10){

                    Toast.makeText(getActivity(), "Please Enter Your 10 digit Mobile No", Toast.LENGTH_SHORT).show();
                }else{

                    userLoginPage(binding.editUserName.getText().toString().trim());
                }
            }
        });

        return view;
    }

    public void userLoginPage(String contact) {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
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


                        String contact_otp = jsonObject.getString("recipient");
                        String login_otp = jsonObject.getString("otp");

                       // startActivity(new Intent(getActivity(), Otp.class));
                        Intent intent = new Intent(getActivity(), OTPVerifactionPage1.class);
                        intent.putExtra("message","Login");
                        intent.putExtra("contact_otp",contact_otp);
                        intent.putExtra("contact_otp1",contact);
                        intent.putExtra("login_otp",login_otp);
                        intent.putExtra("classname","classname");
                        startActivity(intent);

                    } else {

                        String contact_otp = jsonObject.getString("recipient");
                        String login_otp = jsonObject.getString("otp");
                        Toast.makeText(getContext(), "Invalide Mobile No", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();

                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
