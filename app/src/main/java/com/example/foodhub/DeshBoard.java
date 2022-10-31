package com.example.foodhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.foodhub.databinding.ActivityDeshBoardBinding;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.extra.SharedPreference;
import com.example.foodhub.fragment.AddresssDetails;
import com.example.foodhub.fragment.CartPageFragment;
import com.example.foodhub.fragment.ContactFragment;
import com.example.foodhub.fragment.HomepageFragment;
import com.example.foodhub.fragment.MyOdrerDetails;
import com.example.foodhub.fragment.ProfileInformation;
import com.example.foodhub.fragment.SerachFragment;
import com.example.foodhub.model.CartItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DeshBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    public static TextView nav_MyOrder,text_name,nav_Profile,nav_MyAddress,nav_Home,
            nav_Logout,nav_Name,nav_MobileNo,nav_ContactUs,
            text_addressName,nav_Categogry;

    public static ImageView search;

    CircleImageView profile_image;

    ArrayList<CartItem> itms = new ArrayList<>();

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    Double latitude,longitude;
    String name,mobileNo,image,userid,addressDetails;
    HomepageFragment test;
    private Boolean exit = false;
    public static FragmentManager fragmentManager;
    RelativeLayout rle_click;

    ActivityDeshBoardBinding binding;

    SessionManager sessionManager;

    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_desh_board);

        binding = ActivityDeshBoardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(DeshBoard.this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomepageFragment homepageFragment = new HomepageFragment();
        fragmentTransaction.replace(R.id.framLayout,homepageFragment,"HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        //getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,new Homepage(),).commit();
        test = (HomepageFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");

        //loc = findViewById(R.id.loc);
        //logo = findViewById(R.id.logo);
        search = findViewById(R.id.image_search);
        //img_Cart = findViewById(R.id.img_Cart);
        //text_ItemCount = findViewById(R.id.text_ItemCount);
        //text_address = findViewById(R.id.text_address);
        rle_click = findViewById(R.id.rle_click);
        text_name = findViewById(R.id.text_AddressName);

        search.setVisibility(View.VISIBLE);


        binding.navigationview.setNavigationItemSelectedListener(this);
        View header = binding.navigationview.getHeaderView(0);

        nav_MyOrder = header.findViewById(R.id.nav_MyOrder);
        nav_Profile = header.findViewById(R.id.nav_Profile);
        nav_MyAddress = header.findViewById(R.id.nav_MyAddress);
        nav_Logout = header.findViewById(R.id.nav_Logout);
        profile_image = header.findViewById(R.id.nav_profile_image);
        nav_Name = header.findViewById(R.id.nav_Name);
        nav_MobileNo = header.findViewById(R.id.nav_MobileNo);
        nav_ContactUs = header.findViewById(R.id.nav_ContactUs);
        nav_Home = header.findViewById(R.id.nav_Home);
        //nav_Categogry = header.findViewById(R.id.nav_Categogry);

        String name = sessionManager.getUserName();
        String mobile = sessionManager.getUserPhonenumber();

        nav_Name.setText(name);
        nav_MobileNo.setText("+91 "+ mobile);

        sharedPreference = new SharedPreference();

      /*  itms = sharedPreference.loadFavorites(DeshBoard.this);

        int cartcount = itms.size();

        String count = String.valueOf(cartcount);

        Log.d("hsagi",count);
        text_ItemCount.setText(count);*/

        nav_MyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //logo.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                MyOdrerDetails myOrder = new MyOdrerDetails();
                ft.replace(R.id.framLayout, myOrder);
                ft.commit();

                text_name.setText("My Order");
            }
        });

        nav_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ProfileInformation personalInformation = new ProfileInformation();
                ft.replace(R.id.framLayout, personalInformation);
                ft.commit();

                text_name.setTextSize(18);
                text_name.setText("PersonalInformation");


            }
        });

        nav_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(test != null && test.isVisible()){

                    binding.MyDrawer.closeDrawer(GravityCompat.START);
                    text_name.setVisibility(View.VISIBLE);
                    text_name.setTextSize(15);
                    text_name.setText("Home");

                }else{

                    binding.MyDrawer.closeDrawer(GravityCompat.START);
                    //loc.setVisibility(View.VISIBLE);
                    //logo.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    //text_address.setVisibility(View.VISIBLE);
                    text_name.setVisibility(View.VISIBLE);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    HomepageFragment homepage = new HomepageFragment();
                    ft.replace(R.id.framLayout, homepage,"HomeFragment");
                    ft.commit();
                    text_name.setTextSize(15);
                    text_name.setText("Home");

                }

            }
        });

        /*img_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.MyDrawer.closeDrawer(GravityCompat.START);
                //loc.setVisibility(View.GONE);
                //text_address.setVisibility(View.GONE);
                //logo.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                CartPageFragment cartPage = new CartPageFragment();
                ft.replace(R.id.framLayout, cartPage);
                ft.commit();
                text_name.setTextSize(18);
                text_name.setText("My Cart");


            }
        });*/

        nav_MyAddress.setOnClickListener(view1 -> {

            binding.MyDrawer.closeDrawer(GravityCompat.START);
            //loc.setVisibility(View.GONE);
            //text_address.setVisibility(View.GONE);
            //logo.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            AddresssDetails addresssDetails = new AddresssDetails();
            ft.replace(R.id.framLayout, addresssDetails);
            ft.commit();
            text_name.setTextSize(18);
            text_name.setText("Address Deatils");

        });

        nav_ContactUs.setOnClickListener(view1 -> {

            binding.MyDrawer.closeDrawer(GravityCompat.START);
            //loc.setVisibility(View.GONE);
            //text_address.setVisibility(View.GONE);
            //logo.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ContactFragment contactFragment = new ContactFragment();
            ft.replace(R.id.framLayout, contactFragment);
            ft.commit();
            text_name.setTextSize(18);
            text_name.setText("Contact Deatils");

        });

        nav_Logout.setOnClickListener(view1 -> logout_Condition());

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SerachFragment serachFragment = new SerachFragment();
                ft.replace(R.id.framLayout, serachFragment);
                ft.commit();
                text_name.setTextSize(18);
                text_name.setText("Search Product");
            }
        });

        binding.bottomNavigation.setSelectedItemId(R.id.home);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()){

                    case R.id.myAccount:

                        selectedFragment = new ProfileInformation();

                        //loc.setVisibility(View.GONE);
                        //logo.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        text_name.setTextSize(18);
                        text_name.setText("PersonalInformation");
                        //text_address.setVisibility(View.GONE);

                        break;

                    case R.id.home:

                        selectedFragment = new HomepageFragment();
                        //loc.setVisibility(View.VISIBLE);
                        text_name.setVisibility(View.VISIBLE);
                        //text_address.setVisibility(View.VISIBLE);
                        //logo.setVisibility(View.VISIBLE);
                        search.setVisibility(View.VISIBLE);
                        text_name.setTextSize(15);
                        text_name.setText(addressDetails);


                        break;

                    case R.id.cart:

                        selectedFragment = new CartPageFragment();
                        //loc.setVisibility(View.GONE);
                        //logo.setVisibility(View.GONE);
                        search.setVisibility(View.GONE);
                        text_name.setTextSize(18);
                        text_name.setText("My Cart");
                        //text_address.setVisibility(View.GONE);

                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framLayout,selectedFragment).commit();

                return true;
            }
        });

    }

    public void logout_Condition() {

        //Show Your Another AlertDialog
        final Dialog dialog = new Dialog(DeshBoard.this);
        dialog.setContentView(R.layout.condition_logout);
        dialog.setCancelable(false);
        Button btn_No = dialog.findViewById(R.id.no);
        TextView textView = dialog.findViewById(R.id.editText);
        Button btn_Yes = dialog.findViewById(R.id.yes);

        btn_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUser();

                dialog.dismiss();

                finish();
                //System.exit(1);

            }
        });
        btn_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.drawable.homecard_back1);

    }

    public void Clickmenu(View view){

        // open drawer
        openDrawer( binding.MyDrawer);
    }

    private static void openDrawer(DrawerLayout drawerLayout){

        // opendrawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {

        binding.MyDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
}