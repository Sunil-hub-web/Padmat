package com.example.foodhub.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodhub.databinding.PersonalInformationBinding;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileInformation extends Fragment {

    PersonalInformationBinding binding;
    SessionManager session;
    ViewDialog progressbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = PersonalInformationBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        getProfileData();

        return view;
    }

    public void getProfileData() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.profile_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String number = jsonObject.getString("number");
                                String img = jsonObject.getString("img");

                                binding.editFillname.setText(name);
                                binding.editMobileNo.setText(number);
                                binding.editEmailId.setText(email);


                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");

                                progressbar.hideDialog();
                                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", session.getUserID());
                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
