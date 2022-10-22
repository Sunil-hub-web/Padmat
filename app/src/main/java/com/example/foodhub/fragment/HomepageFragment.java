package com.example.foodhub.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.foodhub.R;
import com.example.foodhub.adapter.CategoryAdapter;
import com.example.foodhub.adapter.HomeItemAdapter;
import com.example.foodhub.adapter.SliderAdapterExample;
import com.example.foodhub.databinding.HomepageFragmentBinding;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.extra.SharedPreference;
import com.example.foodhub.extra.ViewDialog;
import com.example.foodhub.model.Category_Model;
import com.example.foodhub.model.ItemGetSet;
import com.example.foodhub.model.SliderItem;
import com.example.foodhub.model.VariationGetSet;
import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomepageFragment extends Fragment {

    HomepageFragmentBinding binding;

    SliderView sliderView;
    private SliderAdapterExample adapter;
    List<SliderItem> sliderItemList = new ArrayList<>();
    MaterialButton addToCart;
    LinearLayout inc;

    GridLayoutManager gridLayoutManager;
    ArrayList<Category_Model> category_models;
    ArrayList<ItemGetSet> itemArraylist;
    ArrayList<VariationGetSet> variations;
    ViewDialog progressbar;
    SessionManager session;
    SharedPreference sharedPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = HomepageFragmentBinding.inflate(getLayoutInflater(),container,false);
        View view = binding.getRoot();

        sliderView = view.findViewById(R.id.imageSlider);

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        getHomeDetails();


        return view;
    }

    public void getHomeDetails(){

        progressbar.showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.home_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");
                    if (status.equalsIgnoreCase("true")) {

                        sliderItemList = new ArrayList<SliderItem>();
                        JSONArray All_banner = jsonObject.getJSONArray("All_banner");
                        for(int i = 0; i< All_banner.length(); i++){

                            JSONObject banner_json = All_banner.getJSONObject(i);
                            String banner_id = banner_json.getString("banner_id");
                            String title = banner_json.getString("title");
                            String img = banner_json.getString("img");

                            sliderItemList.add(new SliderItem(banner_id, title, img));
                        }
                        init();

                        category_models = new ArrayList<Category_Model>();

                        JSONArray All_category = jsonObject.getJSONArray("All_category");
                        for(int j = 0; j< All_category.length(); j++){

                            JSONObject category_json = All_category.getJSONObject(j);
                            String category_id = category_json.getString("category_id");
                            String cate_name = category_json.getString("cate_name");
                            String cate_img = category_json.getString("cate_img");

                            category_models.add(new Category_Model(category_id, cate_name, cate_img));
                        }
                        setCategories();

                        itemArraylist = new ArrayList<ItemGetSet>();
                        JSONArray All_products = jsonObject.getJSONArray("All_products");
                        for(int l = 0; l< All_products.length(); l++){

                            JSONObject product_json = All_products.getJSONObject(l);
                            String products_id = product_json.getString("products_id");
                            String name = product_json.getString("name");
                            String image = product_json.getString("image");
                            String description = product_json.getString("description");
                            String regular_price = product_json.getString("regular_price");
                            String sales_price = product_json.getString("sales_price");


                            JSONArray variationss = product_json.getJSONArray("variations");
                            if(variationss.length()==0){
                                variations = new ArrayList<VariationGetSet>();
                            }else {
                                variations = new ArrayList<VariationGetSet>();
                                for (int m = 0; m < variationss.length(); m++) {
                                    JSONObject variation_json = variationss.getJSONObject(m);
                                    String price_id = variation_json.getString("price_id");
                                    String price = variation_json.getString("price");
                                    String varations = variation_json.getString("varations");

                                    variations.add(new VariationGetSet(price_id, price, varations));
                                }
                            }
                            Log.d("error_response", name);
                            itemArraylist.add(new ItemGetSet(products_id, name, image, description, regular_price, sales_price, "0", "0", "0", variations));
                        }
                        setData();

                        progressbar.hideDialog();

                    } else {
                        String msg = jsonObject.getString("message");


                        progressbar.hideDialog();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

//
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void init() {

        adapter = new SliderAdapterExample(getActivity(),sliderItemList);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    public void setCategories(){

        CategoryAdapter restaurantAdapter = new CategoryAdapter(category_models, getActivity());
        binding.showRecyclerCategory.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.showRecyclerCategory.setLayoutManager(gridLayoutManager);
        binding.showRecyclerCategory.setAdapter(restaurantAdapter);
        binding.showRecyclerCategory.setVisibility(View.VISIBLE);

    }

    public void setData(){

        HomeItemAdapter adpater = new HomeItemAdapter(itemArraylist, getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recyclerproduct.setLayoutManager(gridLayoutManager);
        binding.recyclerproduct.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerproduct.setAdapter(adpater);
    }
}
