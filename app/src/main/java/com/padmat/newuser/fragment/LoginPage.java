package com.padmat.newuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.padmat.newuser.DeshBoard;
import com.padmat.newuser.databinding.ActivityLoginPageBinding;
import com.padmat.newuser.extra.ServerLinks;
import com.padmat.newuser.ForgotPassword;

import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends Fragment {

    ActivityLoginPageBinding binding;
    SessionManager session;
    ViewDialog progressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ActivityLoginPageBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.editUserName.getText().toString().trim().length()==0){
                    binding.editUserName.setError("enter username");
                    binding.editUserName.requestFocus();

                }else if(binding.editPassword.getText().toString().trim().length()==0){
                    binding.editPassword.setError("enter password");
                    binding.editPassword.requestFocus();

                }else{

                    String username = binding.editUserName.getText().toString().trim();
                    String password = binding.editPassword.getText().toString().trim();
                    loginUser(username,password);

                }
            }
        });

        binding.textClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ForgotPassword.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public void loginUser(String userName, String password) {

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                        Intent i = new Intent(getActivity(), DeshBoard.class);
                        startActivity(i);
                        getActivity().finish();

                    } else {

                        String msg = jsonObject.getString("msg");
                        progressbar.hideDialog();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(getActivity(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("usr_name", userName);
                params.put("password", password);

                Log.d("paramsforhomeapi", "" + params);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

}
