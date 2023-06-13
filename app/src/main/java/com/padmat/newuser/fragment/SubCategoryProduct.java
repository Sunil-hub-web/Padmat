package com.padmat.newuser.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.padmat.newuser.adapter.ComboListAdapter;
import com.padmat.newuser.databinding.FragmentSubCategoryProductBinding;
import com.padmat.newuser.extra.Constants;
import com.padmat.newuser.extra.ServerLinks;

import com.padmat.newuser.R;

import com.padmat.newuser.extra.SessionManager;
import com.padmat.newuser.extra.SharedPreference;
import com.padmat.newuser.extra.ViewDialog;
import com.padmat.newuser.model.CartItem;
import com.padmat.newuser.model.ComboGetSet;
import com.padmat.newuser.model.VariationGetSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategoryProduct extends Fragment {

    FragmentSubCategoryProductBinding binding;
    RecyclerView recyle_iem;

    ArrayList<VariationGetSet> variations;
    ArrayList<ComboGetSet> comboArraylist;
    public static TextView itemcounter;
    SessionManager session;
    ViewDialog progressbar;
    ImageView backicon;
    String sub_cate_id;
    RelativeLayout cart_layout;
    SwipeRefreshLayout swipeLayout;
    ImageView noconnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSubCategoryProductBinding.inflate(getLayoutInflater(),container,false);

        View view = binding.getRoot();

        session = new SessionManager(getActivity());
        progressbar = new ViewDialog(getActivity());

        recyle_iem = view.findViewById(R.id.recyle_iem);

        Bundle arguments = getArguments();

        if (arguments!=null){

            String name = arguments.get("title").toString();
            DeshBoard.text_name.setText(name);
            sub_cate_id = arguments.get("id").toString();

           }

        initiateView();

       /* title.setText(getActivity().getIntent().getStringExtra("title"));
        sub_cate_id = getActivity().getIntent().getStringExtra("id");*/

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

    public void initiateView() {
        getHomeData();
        ItemCounter();
    }

    public void ItemCounter() {

        try {

            ArrayList<CartItem> favouritesBeanSampleList = new ArrayList<CartItem>();
            SharedPreference sharedPreference = new SharedPreference();
            favouritesBeanSampleList = sharedPreference.loadFavorites(getActivity());

            String ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;

            itemcounter.setText(Constants.cartitems);

        } catch (Exception e) {

            Log.d("Bw3fev", "" + e);

        }

    }

    public void getHomeData() {
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.productlist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                comboArraylist = new ArrayList<ComboGetSet>();
                                JSONArray All_products = jsonObject.getJSONArray("All_Products");
                                for (int l = 0; l < All_products.length(); l++) {

                                    JSONObject product_json = All_products.getJSONObject(l);
                                    String products_id = product_json.getString("products_id");
                                    String name = product_json.getString("name");
                                    String image = product_json.getString("image");
                                    String description = product_json.getString("description");
                                    String regular_price = product_json.getString("regular_price");
                                    String sales_price = product_json.getString("sales_price");

                                    variations = new ArrayList<VariationGetSet>();
                                    JSONArray variationss = product_json.getJSONArray("variations");
                                    if (variationss.length() == 0) {

                                    } else {
                                        for (int m = 0; m < variationss.length(); m++) {
                                            JSONObject variation_json = variationss.getJSONObject(m);
                                            String price_id = variation_json.getString("price_id");
                                            String price = variation_json.getString("price");
                                            String varations = variation_json.getString("varations");

                                            variations.add(new VariationGetSet(price_id, price, varations));
                                        }
                                    }
                                    Log.d("error_response", name);
                                    Log.d("error_response", ""+variations.size());
                                    if (!products_id.equalsIgnoreCase("null")) {
                                        comboArraylist.add(new ComboGetSet(products_id, name, image, description, regular_price, sales_price, "", "", "", variations));
                                    }
                                }
                                setComboData();

                                noconnection.setVisibility(View.GONE);
                                recyle_iem.setVisibility(View.VISIBLE);

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


                            noconnection.setVisibility(View.VISIBLE);
                            recyle_iem.setVisibility(View.GONE);
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
                params.put("sub_cate_id", sub_cate_id);
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

    public void setComboData(){

        ComboListAdapter adpater = new ComboListAdapter(comboArraylist, getActivity());
        recyle_iem.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyle_iem.setLayoutManager(linearLayoutManager);
        recyle_iem.setItemAnimator(new DefaultItemAnimator());
        recyle_iem.setAdapter(adpater);


    }
}