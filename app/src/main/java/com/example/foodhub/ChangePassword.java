package com.example.foodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.ViewDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText edit_Password,edit_ConfirmPassword;
    String str_Password,str_ConfirmPassword,mobileno;
    Button btn_ChangePassword;
    ViewDialog progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressbar = new ViewDialog(this);

        edit_Password = findViewById(R.id.edit_Password);
        edit_ConfirmPassword = findViewById(R.id.edit_ConfirmPassword);
        btn_ChangePassword = findViewById(R.id.btn_ChangePassword);

        Intent intent = getIntent();
        mobileno = intent.getStringExtra("mobileno");

        btn_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edit_Password.getText())){

                    edit_Password.setError("Fill The Details");

                }else if(TextUtils.isEmpty(edit_ConfirmPassword.getText())){

                    edit_ConfirmPassword.setError("Fill The Details");

                }else if(edit_Password.getText().toString().equals(edit_ConfirmPassword.getText().toString().trim())){

                    str_Password = edit_Password.getText().toString().trim();

                    changePassword(mobileno,str_Password);

                }else{

                    edit_ConfirmPassword.setError("Password not match");
                }

            }
        });

    }

    public void changePassword(String mobile,String password){

        progressbar.showDialog();

        StringRequest stringRequest  = new StringRequest(Request.Method.POST, ServerLinks.change_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressbar.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("true")){

                        String msg = jsonObject.getString("msg");

                        Toast.makeText(ChangePassword.this, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ChangePassword.this,MainActivity.class);
                        startActivity(intent);

                    }else{

                        String msg = jsonObject.getString("msg");

                        Toast.makeText(ChangePassword.this, msg, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(ChangePassword.this, "Facing Technical issues, Try again!", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("mobile",mobile);
                params.put("password",password);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}