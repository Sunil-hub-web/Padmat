package com.example.foodhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodhub.databinding.ActivityMainBinding;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.fragment.LoginPage;
import com.example.foodhub.fragment.RegisterPage;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    LoginPage test;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(MainActivity.this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginPage loginPageFragment = new LoginPage();
        ft.replace(R.id.fram, loginPageFragment, "loginpage");
        ft.commit();

        binding.login.setTextColor(getResources().getColor(R.color.some_color));
        binding.signup.setTextColor(getResources().getColor(R.color.some_color31));

        test = (LoginPage) getSupportFragmentManager().findFragmentByTag("loginpage");

        if (test != null && test.isVisible()) {

            binding.login.setTextColor(getResources().getColor(R.color.some_color));
            binding.signup.setTextColor(getResources().getColor(R.color.some_color31));
        }

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                LoginPage loginPageFragment = new LoginPage();
                ft.replace(R.id.fram, loginPageFragment, "loginpage");
                ft.addToBackStack(null);
                ft.commit();

                binding.login.setTextColor(getResources().getColor(R.color.some_color));
                binding.signup.setTextColor(getResources().getColor(R.color.some_color31));

            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                RegisterPage registerPage = new RegisterPage();
                ft.replace(R.id.fram, registerPage);
                ft.commit();

                binding.login.setTextColor(getResources().getColor(R.color.some_color31));
                binding.signup.setTextColor(getResources().getColor(R.color.some_color));

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

      /*  Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);*/

        this.finish();
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sessionManager.isLogin()){

            startActivity(new Intent(MainActivity.this,DeshBoard.class));
        }

    }
}