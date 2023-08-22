package com.padmat.newuser.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.padmat.newuser.DeshBoard;
import com.padmat.newuser.extra.Constants;

import com.padmat.newuser.R;

import com.padmat.newuser.extra.RecyclerTouchListener;
import com.padmat.newuser.extra.SharedPreference;
import com.padmat.newuser.fragment.SubCategoryProduct;
import com.padmat.newuser.model.CartItem;
import com.padmat.newuser.model.ComboGetSet;
import com.padmat.newuser.model.VariationGetSet;

import java.util.ArrayList;
import java.util.List;


public class ComboListAdapter extends RecyclerView.Adapter<ComboListAdapter.ProgramViewHolder> {
    int count = 0;
    private ArrayList<ComboGetSet> fooditem;
    Context context;
    String qnttty;
    int indx, noit;
    double sum = 0.00;
    SharedPreference sharedPreference = new SharedPreference();
    ArrayList<CartItem> favouritesBeanSampleList;
    String ct, qnty, productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price;
    ArrayList<VariationGetSet> variations;
    Dialog dialogMenu;

    public ComboListAdapter(ArrayList<ComboGetSet> fooditem, Context context) {
        this.fooditem = fooditem;
        this.context = context;

    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.combolist_layout, viewGroup, false);

        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgramViewHolder programViewHolder, @SuppressLint("RecyclerView") final int i) {
        final ComboGetSet My_list = fooditem.get(i);

        try {


            programViewHolder.restrurant_food_name.setText(My_list.getName());
            programViewHolder.restt_price.setText("₹ " +My_list.getSales_price());
            programViewHolder.discount.setText(My_list.getRegular_price());
            programViewHolder.restrurant_food_descript.setText(My_list.getDescription());
            programViewHolder.discount.setPaintFlags(programViewHolder.discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            Glide.with(context).load(
                    My_list.getImage()).into(programViewHolder.imag_food);

            programViewHolder.addtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(fooditem.get(i).getVariations().isEmpty()){
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = "";
                        vsubmit_price = fooditem.get(i).getSales_price();
                        productimage = fooditem.get(i).getImage();
                    }else {
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = fooditem.get(i).getVar_name();
                        vsubmit_price = fooditem.get(i).getVar_price();
                        productimage = fooditem.get(i).getImage();
                    }

                    favouritesBeanSampleList = sharedPreference.loadFavorites(context);

                    if (favouritesBeanSampleList == null) {
                        Log.d("gbgvdw_9", vsubmit_id);
                        CartItem cartitem = new CartItem(productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price, "1", vsubmit_price);
                        sharedPreference.addFavorite(context, cartitem);

                        //Toast.makeText(context, "Item Added ", Toast.LENGTH_SHORT).show();

                        programViewHolder.l_add.setVisibility(View.GONE);
                        programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                        programViewHolder.tv_count.setText("1");

                        ItemCounter();

                    } else if (favouritesBeanSampleList.size() == 0) {

                        CartItem cartitem = new CartItem(productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price,"1", vsubmit_price);
                        sharedPreference.addFavorite(context, cartitem);

                        //Toast.makeText(context, "Item Added ", Toast.LENGTH_SHORT).show();

                        programViewHolder.l_add.setVisibility(View.GONE);
                        programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                        programViewHolder.tv_count.setText("1");

                        ItemCounter();

                    } else {

                        CartItem cartitem = new CartItem(productid, product_name, productimage, vsubmit_id, attributevalue, vsubmit_price, "1", vsubmit_price);
                        sharedPreference.addFavorite(context, cartitem);

                        //Toast.makeText(context, "Item Added ", Toast.LENGTH_SHORT).show();

                        programViewHolder.l_add.setVisibility(View.GONE);
                        programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                        programViewHolder.tv_count.setText("1");

                        ItemCounter();

                    }


                }
            });
            programViewHolder.restt_price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent in = new Intent(context, ProductDetails.class);
//                    in.putExtra("name", fooditem.get(i).getName());
//                    in.putExtra("price", fooditem.get(i).getSales_price());
//                    in.putExtra("image", fooditem.get(i).getImage());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
                }
            });
            programViewHolder.imag_food.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent in = new Intent(context, ProductDetails.class);
//                    in.putExtra("name", fooditem.get(i).getName());
//                    in.putExtra("price", fooditem.get(i).getSales_price());
//                    in.putExtra("image", fooditem.get(i).getImage());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
                }
            });

            programViewHolder.restrurant_food_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent in = new Intent(context, ProductDetails.class);
//                    in.putExtra("name", fooditem.get(i).getName());
//                    in.putExtra("price", fooditem.get(i).getSales_price());
//                    in.putExtra("image", fooditem.get(i).getImage());
//                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(in);
                }
            });

            programViewHolder.spinertext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    variations = new ArrayList<VariationGetSet>();

                    if(fooditem.get(i).getVariations().isEmpty()){

                    }else {
                        variations = fooditem.get(i).getVariations();

                        dialogMenu = new Dialog(context, android.R.style.Theme_Light_NoTitleBar);
                        dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogMenu.setContentView(R.layout.variationrecycler_layout);
                        dialogMenu.setCancelable(true);
                        dialogMenu.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogMenu.setCanceledOnTouchOutside(true);

                        RecyclerView rv_vars = dialogMenu.findViewById(R.id.rv_vars);

                        rv_vars.setLayoutManager(new LinearLayoutManager(context));
                        rv_vars.setNestedScrollingEnabled(false);
                        VariationAdapterforProductlist varad = new VariationAdapterforProductlist(variations, context);
                        rv_vars.setAdapter(varad);

                        rv_vars.addOnItemTouchListener(new RecyclerTouchListener(context, rv_vars, new RecyclerTouchListener.ClickListener() {

                            @Override
                            public void onClick(View view, int post) {

                                Log.d("gbrdsfbfbvdz", "clicked");

                                VariationGetSet parenting = variations.get(post);

                                fooditem.get(i).setVar_id(parenting.getPrice_id());
                                fooditem.get(i).setVar_price(parenting.getPrice());
                                fooditem.get(i).setVar_name(parenting.getVarations());


                                programViewHolder.restt_price.setText("₹ " + parenting.getPrice());
//                            programViewHolder.discount.setText(parenting.getWeighname());
                                programViewHolder.spinertext.setText(parenting.getVarations());

                                if (checkIfSameItem("", parenting.getPrice_id())) {
                                    programViewHolder.l_add.setVisibility(View.GONE);
                                    programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                                    programViewHolder.tv_count.setText(qnty);
                                } else {
                                    programViewHolder.l_add.setVisibility(View.VISIBLE);
                                    programViewHolder.l_add_cart.setVisibility(View.GONE);
                                    programViewHolder.tv_count.setText(qnty);
                                }
                                dialogMenu.dismiss();
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }


                        }));

                        dialogMenu.show();
                    }

                }
            });

            programViewHolder.tv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(fooditem.get(i).getVariations().isEmpty()){
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = "";
                        vsubmit_price = fooditem.get(i).getSales_price();
                        productimage = fooditem.get(i).getImage();
                    }else {
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = fooditem.get(i).getVar_name();
                        vsubmit_price = fooditem.get(i).getVar_price();
                        productimage = fooditem.get(i).getImage();
                    }

                    int count_value = Integer.valueOf(programViewHolder.tv_count.getText().toString());
                    int count = count_value - 1;
                    if (count == 0) {
                        programViewHolder.l_add.setVisibility(View.VISIBLE);
                        programViewHolder.l_add_cart.setVisibility(View.GONE);
                        getindex(vsubmit_id);
                        sharedPreference.removeFavorite(context, indx);
                       // ItemCounter();
                    } else {

                        getindex(vsubmit_id);

                        double t = Double.parseDouble(vsubmit_price) * count;

                        sharedPreference.editFavorite(context, indx, String.valueOf(t), String.valueOf(count));

                      //  ItemCounter();
                        programViewHolder.tv_count.setText(count + "");
                    }

                }
            });
            programViewHolder.tv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count_value = Integer.valueOf(programViewHolder.tv_count.getText().toString());
                    int count = count_value + 1;
//                programViewHolder.tv_count.setText(count + "");
                    if(fooditem.get(i).getVariations().isEmpty()){
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = "";
                        vsubmit_price = fooditem.get(i).getSales_price();
                        productimage = fooditem.get(i).getImage();
                    }else {
                        productid = fooditem.get(i).getProducts_id();
                        product_name = fooditem.get(i).getName();
                        vsubmit_id = fooditem.get(i).getVar_id();
                        attributevalue = fooditem.get(i).getVar_name();
                        vsubmit_price = fooditem.get(i).getVar_price();
                        productimage = fooditem.get(i).getImage();
                    }

                    Log.d("gvrtgv", vsubmit_id);

                    getindex(vsubmit_id);

                    int plusValue = count_value + 1;

                    double t = Double.parseDouble(vsubmit_price) * plusValue;

                    sharedPreference.editFavorite(context, indx, String.valueOf(t), String.valueOf(plusValue));

                    //ItemCounter();
                    programViewHolder.tv_count.setText(plusValue + "");

                }
            });

            if (checkIfSameItem(My_list.getProducts_id(), "")) {
                programViewHolder.l_add.setVisibility(View.GONE);
                programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                programViewHolder.tv_count.setText(qnty);
            } else {
                programViewHolder.l_add.setVisibility(View.VISIBLE);
                programViewHolder.l_add_cart.setVisibility(View.GONE);
                programViewHolder.tv_count.setText(qnty);
            }

            if(My_list.getVariations().size()==0){
                programViewHolder.varlayout.setVisibility(View.INVISIBLE);
                programViewHolder.restt_price.setText("₹"+My_list.getSales_price());
            }
            else{
                programViewHolder.varlayout.setVisibility(View.VISIBLE);
                programViewHolder.restt_price.setText("₹ " + My_list.getVariations().get(0).getPrice());
                programViewHolder.spinertext.setText(My_list.getVariations().get(0).getVarations());

                fooditem.get(i).setVar_id(My_list.getVariations().get(0).getPrice_id());
                fooditem.get(i).setVar_name(My_list.getVariations().get(0).getVarations());
                fooditem.get(i).setVar_price(My_list.getVariations().get(0).getPrice());
            }

            if(My_list.getVariations().size()==0){

                if (checkIfSameItem(My_list.getProducts_id(), "")) {
                    programViewHolder.l_add.setVisibility(View.GONE);
                    programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                    programViewHolder.tv_count.setText(qnty);
                } else {
                    programViewHolder.l_add.setVisibility(View.VISIBLE);
                    programViewHolder.l_add_cart.setVisibility(View.GONE);
                    programViewHolder.tv_count.setText(qnty);
                }
            }
            else {
                if (checkIfSameItem(My_list.getProducts_id(), My_list.getVariations().get(0).getPrice_id())) {
                    programViewHolder.l_add.setVisibility(View.GONE);
                    programViewHolder.l_add_cart.setVisibility(View.VISIBLE);
                    programViewHolder.tv_count.setText(qnty);
                } else {
                    programViewHolder.l_add.setVisibility(View.VISIBLE);
                    programViewHolder.l_add_cart.setVisibility(View.GONE);
                    programViewHolder.tv_count.setText(qnty);
                }
            }

        } catch (Exception e) {
            Log.d("wedfd", String.valueOf(e));
        }
    }

    @Override
    public int getItemCount() {

        return fooditem.size();
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView restrurant_food_name, restt_price, tv_minus, tv_plus, tv_count, addtext, discount,spinertext,restrurant_food_descript;
        LinearLayout l_add, l_add_cart, lay;
        Button add;
        ImageView imag_food;
        RelativeLayout varlayout;
        LinearLayout foodItemMain;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            restrurant_food_name = (TextView) itemView.findViewById(R.id.restrurant_food_name);
            restt_price = (TextView) itemView.findViewById(R.id.restt_price);
            tv_minus = (TextView) itemView.findViewById(R.id.tv_minus);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_plus = (TextView) itemView.findViewById(R.id.tv_plus);
            addtext = (TextView) itemView.findViewById(R.id.addtext);
            discount = (TextView) itemView.findViewById(R.id.discount);
            imag_food = (ImageView) itemView.findViewById(R.id.imag_food);
            l_add = (LinearLayout) itemView.findViewById(R.id.l_add);
            l_add_cart = (LinearLayout) itemView.findViewById(R.id.l_add_cart);
            spinertext = (TextView) itemView.findViewById(R.id.spinertext);
            varlayout = (RelativeLayout) itemView.findViewById(R.id.varlayout);
            restrurant_food_descript = (TextView) itemView.findViewById(R.id.restrurant_food_descript);


        }
    }

    public boolean getindex(String checkProduct) {
        boolean check = false;
        List<CartItem> favorites = sharedPreference.loadFavorites(context);
        if (favorites != null) {
            for (CartItem product : favorites) {

                if (product.getVarient_id().equals(checkProduct)) {

                    indx = favorites.indexOf(product);

                    Log.d("indx", String.valueOf(indx));

                    check = true;
                    break;

                }

            }
        }
        return check;


    }

    public boolean checkIfSameItem(String productid, String variationid) {
        boolean check = false;
        List<CartItem> favorites = sharedPreference.loadFavorites(context);
        if (favorites != null) {
            for (CartItem product : favorites) {

                if(variationid.length()==0){
                    if (product.getProduct_id().equals(productid)) {

                        qnty = product.getQuantity();

                        Log.d("oapsd", qnty);

                        check = true;
                        break;

                    }
                }else {

                    if (product.getVarient_id().equals(variationid)) {

                        qnty = product.getQuantity();

                        Log.d("oapsd", qnty);

                        check = true;
                        break;

                    }
                }

            }
        }
        return check;

    }

    public void ItemCounter() {

        try {
            favouritesBeanSampleList = sharedPreference.loadFavorites(context);
//        Log.d("fbfds", String.valueOf(favouritesBeanSampleList.size()));
//        ct=String.valueOf(favouritesBeanSampleList.size());

            int int_total_cart = favouritesBeanSampleList.size();
            BadgeDrawable badge = DeshBoard.bottomNavigation.getOrCreateBadge(R.id.cart);//R.id.action_add is menu id
            badge.setNumber(int_total_cart);
            badge.setBackgroundColor(ContextCompat.getColor(context,R.color.some_color1));

            ct = String.valueOf(favouritesBeanSampleList.size());
            Constants.cartitems = ct;


            SubCategoryProduct.itemcounter.setText(Constants.cartitems);




        } catch (Exception e) {


        }

    }

}