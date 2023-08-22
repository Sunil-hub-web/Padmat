package com.padmat.newuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.padmat.newuser.fragment.LoginOTP;
import com.padmat.newuser.fragment.LoginPage;
import com.padmat.newuser.fragment.RegisterPage;

import com.padmat.newuser.R;

import com.padmat.newuser.extra.SessionManager;

import com.padmat.newuser.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    LoginPage test;
    SessionManager sessionManager;
    private UiModeManager uiModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(MainActivity.this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginOTP loginPageFragment = new LoginOTP();
        ft.replace(R.id.fram, loginPageFragment, "loginpage");
        ft.commit();

      //  binding.login.setTextColor(getResources().getColor(R.color.some_color));
   //     binding.signup.setTextColor(getResources().getColor(R.color.some_color31));

        test = (LoginPage) getSupportFragmentManager().findFragmentByTag("loginpage");

//        if (test != null && test.isVisible()) {
//
//            binding.login.setTextColor(getResources().getColor(R.color.some_color));
//            binding.signup.setTextColor(getResources().getColor(R.color.some_color31));
//        }

//        binding.login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                LoginPage loginPageFragment = new LoginPage();
//                ft.replace(R.id.fram, loginPageFragment, "loginpage");
//                ft.addToBackStack(null);
//                ft.commit();
//
//                binding.login.setTextColor(getResources().getColor(R.color.some_color));
//                binding.signup.setTextColor(getResources().getColor(R.color.some_color31));
//
//            }
//        });
//
//        binding.signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                RegisterPage registerPage = new RegisterPage();
//                ft.replace(R.id.fram, registerPage);
//                ft.commit();
//
//                binding.login.setTextColor(getResources().getColor(R.color.some_color31));
//                binding.signup.setTextColor(getResources().getColor(R.color.some_color));
//
//            }
//        });

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