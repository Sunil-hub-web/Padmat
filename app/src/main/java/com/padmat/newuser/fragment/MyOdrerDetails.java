package com.padmat.newuser.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.padmat.newuser.adapter.OrderHistoryAdapter;
import com.padmat.newuser.extra.RecyclerTouchListener;
import com.padmat.newuser.extra.ServerLinks;

import com.padmat.newuser.R;

import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.ViewDialog;
import com.padmat.newuser.model.OrderGetSet;
import com.padmat.newuser.model.OrderItemGetSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyOdrerDetails extends Fragment {

    RecyclerView orderhist;
    ImageView iv_add;
    ArrayList<OrderGetSet> orderarray;
    ArrayList<OrderItemGetSet> orderitemarray;
    ViewDialog progressbar;
    SessionManager session;
    SwipeRefreshLayout swipeLayout;
    ImageView noconnection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.orderdetails,container,false);
        orderhist = view.findViewById(R.id.orderhist);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        initiateView();

        orderhist.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), orderhist, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                OrderGetSet parenting = orderarray.get(position);

//                Constants.orderitem = new ArrayList<OrderItemGetSet>();
//                Constants.orderitem = parenting.getItemsarray();
//
//                Intent i = new Intent(getApplicationContext(), OrderDetails.class);
//                i.putExtra("orderid",parenting.getOrder_id());
//                i.putExtra("itemtotal",parenting.getTotal());
//                i.putExtra("delivery",parenting.getShipping_char());
//                i.putExtra("discount",parenting.getCoupon_amnt());
//                i.putExtra("wallet",parenting.getWallet());
//                i.putExtra("totalamt",parenting.getGrand_total());
//                i.putExtra("address",parenting.getName()+"\n"+parenting.getAddress()+"\n"+parenting.getCity()+"("+parenting.getPin()+")"+"\n"+parenting.getContact()+"\n"+parenting.getEmail());
//                i.putExtra("date",parenting.getOrder_date());
//                i.putExtra("paymode",parenting.getPay_mode());
//                i.putExtra("status",parenting.getStatus());
//                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        noconnection = view.findViewById(R.id.noconnection);
        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeLayout.setRefreshing(true);
                initiateView();
                swipeLayout.setRefreshing(false);

            }
        });

        return view;
    }

    public void getAddress() {


        OrderHistoryAdapter adpater = new OrderHistoryAdapter(getActivity(), orderarray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        orderhist.setLayoutManager(layoutManager);
        orderhist.setItemAnimator(new DefaultItemAnimator());
        orderhist.setAdapter(adpater);

    }

    public void initiateView() {
        getOrderHistory();
    }

    public void getOrderHistory() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.GetOrders_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {



                                orderarray = new ArrayList<OrderGetSet>();
                                JSONArray All_address = jsonObject.getJSONArray("All_Orders");
                                for(int n = 0; n< All_address.length(); n++){

                                    JSONObject productt_json = All_address.getJSONObject(n);
                                    String order_id = productt_json.getString("order_id");
                                    String order_status = productt_json.getString("order_status");
                                    String shiping_type = productt_json.getString("shiping_type");
                                    String shipping_charge = productt_json.getString("shipping_charge");
                                    String payment_mode = productt_json.getString("payment_mode");
                                    String subtotal = productt_json.getString("subtotal");
                                    String total = productt_json.getString("total");
                                    String delivery_date = productt_json.getString("delivery_date");

                                    Log.d("error_response", delivery_date);

                                    if(!delivery_date.equalsIgnoreCase("null") && !delivery_date.equalsIgnoreCase("")) {

                                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-d");
                                        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        Date date = null;
                                        try {
                                            date = inputFormat.parse(delivery_date);
                                            delivery_date = outputFormat.format(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }



                                        delivery_date = delivery_date + "(" + productt_json.getString("timeSlot") + ")";
                                    }

                                    orderitemarray = new ArrayList<OrderItemGetSet>();
                                    JSONArray Order_details = productt_json.getJSONArray("Order_details");
                                    for(int m = 0; m< Order_details.length(); m++) {

                                        JSONObject item_details = Order_details.getJSONObject(m);

                                        String id = item_details.getString("id");
                                        String name = item_details.getString("name");
                                        String qty = item_details.getString("qty");
                                        String img = item_details.getString("img");
                                        String price = item_details.getString("price");
                                        String weight = item_details.getString("weight");


                                        orderitemarray.add(new OrderItemGetSet(id, name, qty, img, price, weight));
                                    }

                                    String addressdet = productt_json.getString("Address");
                                    JSONObject jsonObject_address = new JSONObject(addressdet);

                                    String nameAdd = jsonObject_address.getString("name");
                                    String state = jsonObject_address.getString("state");
                                    String city = jsonObject_address.getString("city");
                                    String pincode = jsonObject_address.getString("pincode");
                                    String address = jsonObject_address.getString("address");
                                    String phone = jsonObject_address.getString("phone");

                                    String addressDetails = nameAdd+", "+state+", "+city+", "+pincode+", "+address+", "+phone;

                                    orderarray.add(new OrderGetSet(order_id, order_status, shiping_type, shipping_charge, payment_mode, subtotal, total, delivery_date, orderitemarray,addressDetails));

                                }
//                                AddressAdapter adpater = new AddressAdapter(addressarray, AddressList.this);
//                                address_list.setHasFixedSize(true);
//                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AddressList.this);
//                                address_list.setLayoutManager(linearLayoutManager);
//                                address_list.setItemAnimator(new DefaultItemAnimator());
//                                address_list.setAdapter(adpater);
                                getAddress();

                                noconnection.setVisibility(View.GONE);
                                orderhist.setVisibility(View.VISIBLE);

                                progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                                progressbar.hideDialog();
                                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

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

                            noconnection.setVisibility(View.VISIBLE);
                            orderhist.setVisibility(View.GONE);
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
}
