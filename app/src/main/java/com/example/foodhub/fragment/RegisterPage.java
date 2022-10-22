package com.example.foodhub.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.foodhub.R;
import com.example.foodhub.databinding.ActivityRegisterPageBinding;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends Fragment {

    ActivityRegisterPageBinding binding;
    SessionManager session;
    ViewDialog progressbar;
    String str_UserFullName, str_MobileNumber, str_EmailId, str_UserName, str_Password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = ActivityRegisterPageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        String checkBox_html = "<font color=#817F7F>Read our   </font> <font color=#e09441><b><u>Terms &amp; Conditions</u></b></font>";
        binding.termsconditions.setText(Html.fromHtml(checkBox_html));

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.editUserFullName.getText())) {

                    binding.editUserFullName.setError("Please Enter User Name");

                } else if (TextUtils.isEmpty(binding.editMobileNumber.getText())) {

                    binding.editMobileNumber.setError("Please Enter Mobile No");

                } else if (binding.editMobileNumber.getText().toString().trim().length() != 10) {

                    binding.editMobileNumber.setError("Enter Your 10 Digit Mobile Number");

                } else if (TextUtils.isEmpty(binding.editEmailId.getText())) {

                    binding.editEmailId.setError("Please Enter EmailId");

                } else if (!isValidEmail(binding.editEmailId.getText().toString().trim())) {

                    binding.editEmailId.requestFocus();
                    binding.editEmailId.setError("Please Enter Valide Email id");

                } else if (TextUtils.isEmpty(binding.editPassword.getText())) {

                    binding.editPassword.setError("Please EnterYour password");

                } else if (!binding.termsconditions.isChecked()) {

                    Toast.makeText(getActivity(), "Please Click Terms & Conditions", Toast.LENGTH_SHORT).show();

                } else {

                    str_UserFullName = binding.editUserFullName.getText().toString().trim();
                    str_MobileNumber = binding.editMobileNumber.getText().toString().trim();
                    str_EmailId = binding.editEmailId.getText().toString().trim();
                    str_Password = binding.editPassword.getText().toString().trim();


                    registerUser(str_UserFullName, str_MobileNumber, str_EmailId, str_MobileNumber, str_Password);

                    //Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
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
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Login to continue", Toast.LENGTH_SHORT).show();

                        LoginPage loginPage = new LoginPage();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fram, loginPage,"loginpage");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    public boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

        pattern = Patterns.EMAIL_ADDRESS;
        matcher = pattern.matcher(email);

        return matcher.matches();

        //return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
