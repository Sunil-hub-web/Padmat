package com.padmat.newuser.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.badge.BadgeDrawable;
import com.padmat.newuser.DeshBoard;
import com.padmat.newuser.OrderSuccessFully;
import com.padmat.newuser.R;
import com.padmat.newuser.adapter.CartAdapter;
import com.padmat.newuser.databinding.CartpageBinding;
import com.padmat.newuser.extra.ServerLinks;
import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.SharedPreference;
import com.padmat.newuser.extra.ViewDialog;
import com.padmat.newuser.model.CartItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartPageFragment extends Fragment {

    CartpageBinding binding;
    RecyclerView recycle_rest_cats;
    ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
    ArrayList<String> deliverychargesarray = new ArrayList<String>();
    HashMap<String, String> deliverychargesid = new HashMap<String, String>();
    HashMap<String, String> deliverychargesprice = new HashMap<String, String>();
    SharedPreference sharedPreference;
    public static TextView paybleamount, tv_change, itemtotal, tv_ship_price, tv_checkout;
    public static String deliverych;
    ViewDialog progressbar;
    SessionManager session;
    Spinner shippingchargename;
    public static double sum = 0.00, delivery_ch = 0.00, coupon_amt = 0.00,deliveryprice = 0.00;
    String deliveryid, slotname = "", dateselected = "", delivery_price;
    RelativeLayout noitem_layout;
    LinearLayout cartlayout, datetimeslot_layout;
    TextView name_txt, address_txt, pincode_txt, phoneno_txt, date_txt;
    //    public ArrayList<CartItem> cartfinalarray;
    Spinner timeslot;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int i;
    int dayOfMonth;
    Calendar calendar;
    CardView addresslayout;
    long timeInMilliseconds;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = CartpageBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        addresslayout = view.findViewById(R.id.addresslayout);
        datetimeslot_layout = view.findViewById(R.id.datetimeslot_layout);
        cartlayout = view.findViewById(R.id.cartlayout);
        noitem_layout = view.findViewById(R.id.noitem_layout);
        recycle_rest_cats = view.findViewById(R.id.recycle_rest_cats);
        paybleamount = view.findViewById(R.id.paybleamount);
        tv_change = view.findViewById(R.id.tv_change);
        date_txt = view.findViewById(R.id.date_txt);
        itemtotal = view.findViewById(R.id.itemtotal);
        tv_ship_price = view.findViewById(R.id.tv_ship_price);
        shippingchargename = view.findViewById(R.id.shippingchargename);
        tv_checkout = view.findViewById(R.id.tv_checkout);
        timeslot = view.findViewById(R.id.timeslot);

        name_txt = view.findViewById(R.id.name_txt);
        address_txt = view.findViewById(R.id.address_txt);
        pincode_txt = view.findViewById(R.id.pincode_txt);
        phoneno_txt = view.findViewById(R.id.phoneno_txt);

        sharedPreference = new SharedPreference();

        favouritesBeanSampleList = sharedPreference.loadFavorites(getActivity());
        int int_total_cart = favouritesBeanSampleList.size();
        BadgeDrawable badge = DeshBoard.bottomNavigation.getOrCreateBadge(R.id.cart);//R.id.action_add is menu id
        badge.setNumber(int_total_cart);
        badge.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.some_color1));

       // Log.d("hgjbsz",favouritesBeanSampleList.toString());

/*        int cartcount = favouritesBeanSampleList.size();

        String count = String.valueOf(cartcount);

        Log.d("hsagi",count);*/
        //DeshBoard.text_ItemCount.setText(count);

        try {
            if (favouritesBeanSampleList.size() != 0) {

                CartAdapter productListAdapter = new CartAdapter(getActivity(), favouritesBeanSampleList);
                binding.recycleRestCats.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                binding.recycleRestCats.setItemAnimator(new DefaultItemAnimator());
                binding.recycleRestCats.setAdapter(productListAdapter);
                noitem_layout.setVisibility(View.GONE);
                cartlayout.setVisibility(View.VISIBLE);

            } else {
                noitem_layout.setVisibility(View.VISIBLE);
                cartlayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            binding.noitemLayout.setVisibility(View.VISIBLE);
            cartlayout.setVisibility(View.GONE);
        }

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DeshBoard.search.setVisibility(View.GONE);
                AddresssDetails addresssDetails = new AddresssDetails();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, addresssDetails);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                DeshBoard.text_name.setTextSize(18);
                DeshBoard.text_name.setText("Address Deatils");

            }
        });

        if (session.getBillFirstANme().equalsIgnoreCase("First Name")) {

            name_txt.setVisibility(View.GONE);
            address_txt.setVisibility(View.GONE);
            pincode_txt.setVisibility(View.GONE);
            phoneno_txt.setVisibility(View.GONE);
            tv_change.setText("Select");

        } else {

            name_txt.setText(session.getBillFirstANme());
            address_txt.setText(session.getBillAddres2());
            pincode_txt.setText(session.getBillPostCode());
            phoneno_txt.setText(session.getBillPhone());

            name_txt.setVisibility(View.VISIBLE);
            address_txt.setVisibility(View.VISIBLE);
            pincode_txt.setVisibility(View.VISIBLE);
            phoneno_txt.setVisibility(View.VISIBLE);
            tv_change.setText("Change");
        }

        getDeliveryCharges();


        //tv_ship_price.setText("₹" + "50");

        getTotal();
        deliveryid = deliverychargesid.get(0);

        shippingchargename.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del = shippingchargename.getItemAtPosition(shippingchargename.getSelectedItemPosition()).toString();

                deliveryid = deliverychargesid.get(del);
                delivery_ch = Double.parseDouble(deliverychargesprice.get(del));

                tv_ship_price.setText(""+delivery_ch);

                getTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                                edit_date.setText(year + "-" + (month + 1) + "-" + day);

                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                String inputDateStr = year + "-" + (month + 1) + "-" + day;
                                Log.d("sufifn", inputDateStr);
                                dateselected = inputDateStr;
                                Date date = null;
                                try {
                                    date = inputFormat.parse(inputDateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String outputDateStr = outputFormat.format(date);

                                date_txt.setText(outputDateStr);
                                setSpinnerData();
//                                fragHome_Date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!session.getUserID().equalsIgnoreCase("0")) {

                    if(binding.tvChange.getText().toString().trim().equals("")){

                        Toast.makeText(getActivity(), "Please select your Address", Toast.LENGTH_SHORT).show();

                    }else{

                        if (session.getBillFirstANme().equalsIgnoreCase("First Name")) {
                            Toast.makeText(getActivity(), "Selct Address", Toast.LENGTH_SHORT).show();
                        } else if (datetimeslot_layout.getVisibility() == View.GONE) {
                            datetimeslot_layout.setVisibility(View.VISIBLE);
                            cartlayout.setVisibility(View.GONE);
                        }else if (dateselected.equalsIgnoreCase("")) {
                            Toast.makeText(getActivity(), "Select Delivary Date", Toast.LENGTH_SHORT).show();
                        }else if (slotname.trim().length()==0 || slotname.equalsIgnoreCase("Select time slot")) {
                            Toast.makeText(getActivity(), "Select Time Slot", Toast.LENGTH_SHORT).show();
                        } else {
                            CreateProductArray();
                        }

                    }

                } else {

                    Toast.makeText(getActivity(), "Login to place order", Toast.LENGTH_SHORT).show();
                    session.logoutUser();
                }
            }
        });

        timeslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                slotname = timeslot.getItemAtPosition(timeslot.getSelectedItemPosition()).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here

            }
        });

        if (!session.getUserID().equalsIgnoreCase("0")) {
            addresslayout.setVisibility(View.VISIBLE);
        } else {
            addresslayout.setVisibility(View.GONE);
        }

       /* binding.textGotoCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckOutFragment checkOutFragment = new CheckOutFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framLayout, checkOutFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
*/

        return view;
    }

    ArrayAdapter<CharSequence> adapter;

    public void setSpinnerData() {

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.timearray1, R.layout.spinnerfront2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        timeslot.setAdapter(adapter);

       /* Date currentTime = Calendar.getInstance().getTime();

        timeInMilliseconds = getMilliFromDate(dateselected);
        if (DateUtils.isToday(timeInMilliseconds)) {

            try {

                Date time1 = new SimpleDateFormat("HH:mm:ss").parse("07:00:00");
                Calendar calendar1 = Calendar.getInstance();
<<<<<<<< HEAD:app/src/main/java/com/padmat/newuser/fragment/CartPageFragment.java
                //  calendar1.setTime(time1);
                //   calendar1.add(Calendar.DATE, 1);
========
              //  calendar1.setTime(time1);
             //   calendar1.add(Calendar.DATE, 1);
>>>>>>>> origin/master:app/src/main/java/in/fudhub/co/fragment/CartPageFragment.java

                calendar1.set(Calendar.HOUR_OF_DAY, 16);
                calendar1.set(Calendar.MINUTE, 0);
                calendar1.set(Calendar.SECOND, 0);

                Date time2 = new SimpleDateFormat("HH:mm:ss").parse("09:00:00");
                Calendar calendar2 = Calendar.getInstance();
                //calendar2.setTime(time2);
<<<<<<<< HEAD:app/src/main/java/com/padmat/newuser/fragment/CartPageFragment.java
                //  calendar2.add(Calendar.DATE, 1);
========
              //  calendar2.add(Calendar.DATE, 1);
>>>>>>>> origin/master:app/src/main/java/in/fudhub/co/fragment/CartPageFragment.java

                calendar2.set(Calendar.HOUR_OF_DAY, 17);
                calendar2.set(Calendar.MINUTE, 0);
                calendar2.set(Calendar.SECOND, 0);

                Date time3 = new SimpleDateFormat("HH:mm:ss").parse("11:00:00");
                Calendar calendar3 = Calendar.getInstance();
<<<<<<<< HEAD:app/src/main/java/com/padmat/newuser/fragment/CartPageFragment.java
                // calendar3.setTime(time3);
                // calendar3.add(Calendar.DATE, 1);
========
               // calendar3.setTime(time3);
               // calendar3.add(Calendar.DATE, 1);
>>>>>>>> origin/master:app/src/main/java/in/fudhub/co/fragment/CartPageFragment.java

                calendar3.set(Calendar.HOUR_OF_DAY, 18);
                calendar3.set(Calendar.MINUTE, 0);
                calendar3.set(Calendar.SECOND, 0);

                Date time4 = new SimpleDateFormat("HH:mm:ss").parse("13:00:00");
                Calendar calendar4 = Calendar.getInstance();
                //calendar4.setTime(time4);
                //calendar4.add(Calendar.DATE, 1);

                calendar4.set(Calendar.HOUR_OF_DAY, 19);
                calendar4.set(Calendar.MINUTE, 0);
                calendar4.set(Calendar.SECOND, 0);

                Date time5 = new SimpleDateFormat("HH:mm:ss").parse("15:00:00");
                Calendar calendar5 = Calendar.getInstance();
                //calendar5.setTime(time5);
<<<<<<<< HEAD:app/src/main/java/com/padmat/newuser/fragment/CartPageFragment.java
                // calendar5.add(Calendar.DATE, 1);
========
               // calendar5.add(Calendar.DATE, 1);
>>>>>>>> origin/master:app/src/main/java/in/fudhub/co/fragment/CartPageFragment.java

                calendar5.set(Calendar.HOUR_OF_DAY, 20);
                calendar5.set(Calendar.MINUTE, 0);
                calendar5.set(Calendar.SECOND, 0);

                Date time6 = new SimpleDateFormat("HH:mm:ss").parse("17:00:00");
                Calendar calendar6 = Calendar.getInstance();
<<<<<<<< HEAD:app/src/main/java/com/padmat/newuser/fragment/CartPageFragment.java
                // calendar6.setTime(time6);
                // calendar6.add(Calendar.DATE, 1);
========
               // calendar6.setTime(time6);
               // calendar6.add(Calendar.DATE, 1);
>>>>>>>> origin/master:app/src/main/java/in/fudhub/co/fragment/CartPageFragment.java

                calendar6.set(Calendar.HOUR_OF_DAY, 21);
                calendar6.set(Calendar.MINUTE, 0);
                calendar6.set(Calendar.SECOND, 0);

                Date time7 = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
                Calendar calendar7 = Calendar.getInstance();
<<<<<<<< HEAD:app/src/main/java/com/padmat/newuser/fragment/CartPageFragment.java
                // calendar7.setTime(time7);
========
               // calendar7.setTime(time7);
>>>>>>>> origin/master:app/src/main/java/in/fudhub/co/fragment/CartPageFragment.java
                //calendar7.add(Calendar.DATE, 1);

                calendar7.set(Calendar.HOUR_OF_DAY, 22);
                calendar7.set(Calendar.MINUTE, 0);
                calendar7.set(Calendar.SECOND, 0);

                Date time8 = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
                Calendar calendar8 = Calendar.getInstance();
                // calendar7.setTime(time7);
                //calendar7.add(Calendar.DATE, 1);

                calendar8.set(Calendar.HOUR_OF_DAY, 23);
                calendar8.set(Calendar.MINUTE, 0);
                calendar8.set(Calendar.SECOND, 0);

                Date time9 = new SimpleDateFormat("HH:mm:ss").parse("18:00:00");
                Calendar calendar9 = Calendar.getInstance();
                // calendar7.setTime(time7);
                //calendar7.add(Calendar.DATE, 1);

                calendar8.set(Calendar.HOUR_OF_DAY, 24);
                calendar8.set(Calendar.MINUTE, 0);
                calendar8.set(Calendar.SECOND, 0);


                Date x = Calendar.getInstance().getTime();
//                final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//                Date x = dateFormat.format(new Date());

                Log.d("mkfgud_1", "" + calendar1.getTime());
                Log.d("mkfgud_2", "" + calendar2.getTime());
                Log.d("mkfgud_3", "" + calendar3.getTime());
                Log.d("mkfgud_4", "" + calendar4.getTime());
                Log.d("mkfgud_5", "" + calendar5.getTime());
                Log.d("mkfgud_6", "" + calendar6.getTime());
                Log.d("mkfgud_7", "" + calendar7.getTime());
                Log.d("mkfgud_8", "" + x);

                if (x.before(calendar1.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray1, R.layout.spinnerfront2);

                } else if (x.before(calendar2.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray2, R.layout.spinnerfront2);

                } else if (x.before(calendar3.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray3, R.layout.spinnerfront2);

                } else if (x.before(calendar4.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray4, R.layout.spinnerfront2);

                } else if (x.before(calendar5.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray5, R.layout.spinnerfront2);

                } else if (x.before(calendar6.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray6, R.layout.spinnerfront2);

                } else if (x.before(calendar7.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray7, R.layout.spinnerfront2);

                } else if (x.before(calendar8.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray8, R.layout.spinnerfront2);

                }else if (x.before(calendar9.getTime())) {
                    adapter = ArrayAdapter.createFromResource(getActivity(),
                            R.array.timearray9, R.layout.spinnerfront2);

                }

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                timeslot.setAdapter(adapter);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.timearray1, R.layout.spinnerfront2);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            timeslot.setAdapter(adapter);
        }*/


    }

    public long getMilliFromDate(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-d");
        try {
            date = formatter.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " + date);
        return date.getTime();
    }

    public void getTotal() {

        favouritesBeanSampleList = sharedPreference.loadFavorites(getActivity());

//        Log.d("RanjeetCartPage", String.valueOf(favouritesBeanSampleList.size()));
        CartPageFragment.sum = 0.00;
        if (favouritesBeanSampleList==null){

        }else {
            if (favouritesBeanSampleList.size() == 0) {

            } else {

                for (int j = 0; j < favouritesBeanSampleList.size(); j++) {


                    CartItem cartIt = favouritesBeanSampleList.get(j);

                    sum = sum + Double.parseDouble(cartIt.getSales_price()) * Double.parseDouble(cartIt.getQuantity());
                }

                itemtotal.setText("₹" + sum);

               // deliveryprice = Double.valueOf(delivery_price);

                if(sum >= deliveryprice){

                   // delivery_ch = 0.0;
                    Log.d("Frfbw_2", String.valueOf(sum));
                    Log.d("Frfbw_2.1", String.valueOf(delivery_ch));
                    tv_ship_price.setText("0");
                    double totsm = sum - coupon_amt;
                    paybleamount.setText("₹" + totsm);
                    Log.d("Frfbw_3", String.valueOf(totsm));


                }else{

                   // delivery_ch = 50.0;

                    Log.d("Frfbw_2", String.valueOf(sum));
                    Log.d("Frfbw_2.1", String.valueOf(delivery_ch));
                    String str_delivery_ch = String.valueOf(delivery_ch);
                    tv_ship_price.setText(str_delivery_ch);
                    double totsm = sum + delivery_ch - coupon_amt;

                    paybleamount.setText("₹" + totsm);
                    Log.d("Frfbw_3", String.valueOf(totsm));


                }

            }
        }
    }
    public void getDeliveryCharges() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.ShippingChr_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);
                        Log.d("cityget", session.getUserID());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                deliverychargesarray = new ArrayList<String>();


                                JSONArray latestarr = jsonObject.getJSONArray("All_shipping_chr");

                                for (int j = 0; j < latestarr.length(); j++) {

                                    JSONObject latjo = latestarr.getJSONObject(0);

                                    deliveryid = latjo.getString("shipping_id");
                                    String price = latjo.getString("price");
                                    delivery_price = latjo.getString("delivery_price");
                                    String name = latjo.getString("name");


                                    deliverychargesarray.add(name);
                                    deliverychargesid.put(name, deliveryid);
                                    deliverychargesprice.put(name, price);

                                }

                                ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, deliverychargesarray);
                                dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                shippingchargename.setAdapter(dataAdapterVehicle);

                                deliveryprice = Double.valueOf(delivery_price);
//
//                                SetRecent();

                                progressbar.hideDialog();
                                binding.cartlayout.setVisibility(View.VISIBLE);

                            } else {
                                binding.cartlayout.setVisibility(View.GONE);
                                final CharSequence[] items = {"Reload", "Close"};

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("Something went wrong, reload app..");
                                builder.setCancelable(false);
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {


                                        if (items[item].equals("Reload")) {

                                            dialog.dismiss();
                                            getDeliveryCharges();

                                        } else if (items[item].equals("Close")) {
                                            dialog.dismiss();
                                            getActivity().finish();
                                        }
                                    }
                                });
                                builder.show();

                                String error = jsonObject.getString("msg");
                                progressbar.hideDialog();
                                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //cartlayout.setVisibility(View.GONE);
                            final CharSequence[] items = {"Reload", "Close"};

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Something went wrong, reload app..");
                            builder.setCancelable(false);
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {

                                    if (items[item].equals("Reload")) {

                                        dialog.dismiss();
                                        getDeliveryCharges();

                                    } else if (items[item].equals("Close")) {
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                }
                            });
                            builder.show();

                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.hideDialog();
                        //cartlayout.setVisibility(View.GONE);
                        final CharSequence[] items = {"Reload", "Close"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Something went wrong, reload app..");
                        builder.setCancelable(false);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {


                                if (items[item].equals("Reload")) {

                                    dialog.dismiss();
                                    getDeliveryCharges();

                                } else if (items[item].equals("Close")) {
                                    dialog.dismiss();
                                    getActivity().finish();
                                }
                            }
                        });
                        builder.show();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getActivity().getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
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
    public void PlaceOrder() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.Placeorder_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);
                        Log.d("cityget", session.getUserID());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("success").equals("true")) {
                                deliverychargesarray = new ArrayList<String>();


                                String order_id = jsonObject.getString("order_id");
                                String msg = jsonObject.getString("msg");

                                progressbar.hideDialog();
                                sharedPreference.clearDate(getActivity());
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                               /* MyOdrerDetails checkOutFragment = new MyOdrerDetails();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.framLayout, checkOutFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                DeshBoard.text_name.setTextSize(18);
                                DeshBoard.text_name.setText("MyOrder Deatils");*/

                                Intent intent = new Intent(getActivity(), OrderSuccessFully.class);
                                startActivity(intent);

                            } else {

                                String error = jsonObject.getString("msg");
                                progressbar.hideDialog();
                                Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

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
                            // ...
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", session.getUserID());
                params.put("product_id", product_id);
                params.put("qty", qty);
                params.put("price_id", price_variation_id);
                params.put("shiping_id", deliveryid);
                params.put("address_id", session.getBillAddress1());
                params.put("pay_mode", "cod");
                params.put("delivery_date", dateselected);
                params.put("AvalTimeSlot", slotname);
                params.put("shipping_chargr", tv_ship_price.getText().toString().trim());

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
    String product_id = "", qty = "", price_variation_id = "";

    public void CreateProductArray() {

//        cartfinalarray = new ArrayList<CartItem>();
//        cartfinalarray = sharedPreference.loadFavorites(Cart.this);

        sharedPreference = new SharedPreference();

        favouritesBeanSampleList = sharedPreference.loadFavorites(getActivity());

        product_id = "";
        qty = "";
        price_variation_id = "";

        if (favouritesBeanSampleList.size() == 0) {
            Toast.makeText(getActivity(), "Add item to proceed", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < favouritesBeanSampleList.size(); i++) {

                CartItem cartItem = favouritesBeanSampleList.get(i);

                if (product_id.length() == 0) {
                    product_id = cartItem.getProduct_id();
                } else {
                    product_id = product_id + "," + cartItem.getProduct_id();
                }

                if (qty.length() == 0) {
                    qty = cartItem.getQuantity();
                } else {
                    qty = qty + "," + cartItem.getQuantity();
                }

                if (price_variation_id.length() == 0) {
                    price_variation_id = cartItem.getVarient_id();
                } else {
                    price_variation_id = price_variation_id + "," + cartItem.getVarient_id();
                }

                Log.d("iteskndfb", "CreateProductArray 2: " + product_id);
                Log.d("iteskndfb", "CreateProductArray 3: " + qty);
                Log.d("iteskndfb", "CreateProductArray 4: " + price_variation_id);
            }

            PlaceOrder();
        }

    }

}
