package com.example.foodhub.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.foodhub.R;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.fragment.AddresssDetails;
import com.example.foodhub.fragment.CartPageFragment;
import com.example.foodhub.fragment.SubCategoryProduct;
import com.example.foodhub.model.AddressGetSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ProgramViewHolder> {
    int count = 0;
    private ArrayList<AddressGetSet> fooditem;
    Context context;
    String qnttty, addrid;
    int indx, noit;
    double sum = 0.00;
    SessionManager session;

    public AddressAdapter(ArrayList<AddressGetSet> fooditem, Context context) {
        this.fooditem = fooditem;
        this.context = context;

    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.address_item_layout, viewGroup, false);

        return new ProgramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgramViewHolder programViewHolder, @SuppressLint("RecyclerView") final int i) {
        final AddressGetSet My_list = fooditem.get(i);

        try {


            programViewHolder.name.setText(My_list.getName());
            programViewHolder.address.setText(My_list.getAddress());
            programViewHolder.phone.setText("Ph : "+My_list.getNumber());
            programViewHolder.statecitypin.setText(My_list.getCity_name()+", "+My_list.getState_name()+" ("+My_list.getPincode()+")");


            programViewHolder.addresslayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session = new SessionManager(context);


                    session.setBillFirstANme(fooditem.get(i).getName());
                    session.setBillAddress1(fooditem.get(i).getAddres_id());
                    session.setBillAddres2(fooditem.get(i).getAddress()+", "+fooditem.get(i).getCity_name());
                    session.setBillPostCode(fooditem.get(i).getState_name()+", "+fooditem.get(i).getPincode());
                    session.setBillPhone(fooditem.get(i).getNumber());

                    CartPageFragment cartPageFragment = new CartPageFragment();
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.framLayout, cartPageFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                }
            });

            programViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    session = new SessionManager(context);


                    session.setBillFirstANme(fooditem.get(i).getName());
                    session.setBillAddress1(fooditem.get(i).getAddres_id());
                    session.setBillAddres2(fooditem.get(i).getAddress()+", "+fooditem.get(i).getCity_name());
                    session.setBillPostCode(fooditem.get(i).getState_name()+", "+fooditem.get(i).getPincode());
                    session.setBillPhone(fooditem.get(i).getNumber());

                    CartPageFragment cartPageFragment = new CartPageFragment();
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = manager.beginTransaction();
                    fragmentTransaction.replace(R.id.framLayout, cartPageFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });


            programViewHolder.delete_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                session = new SessionManager(context);
                addrid = fooditem.get(i).getAddres_id();

                new AlertDialog.Builder(context)
                        .setTitle("Delete Address")
                        .setMessage("Are you sure you want to remove this address?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                RemoveAddressData();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // user doesn't want to logout
                            }
                        })
                        .show();
                }
            });


        } catch (Exception e) {
            Log.d("wedfd", String.valueOf(e));
        }
    }

    @Override
    public int getItemCount() {

        return fooditem.size();
    }

    public class ProgramViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, statecitypin, phone, edit, delete_address;
        LinearLayout addresslayout;

        public ProgramViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            statecitypin = (TextView) itemView.findViewById(R.id.statecitypin);
            phone = (TextView) itemView.findViewById(R.id.phone);
            //edit = (TextView) itemView.findViewById(R.id.edit);
            delete_address = (TextView) itemView.findViewById(R.id.delete_address);
            addresslayout = (LinearLayout) itemView.findViewById(R.id.addresslayout);


        }
    }

    public void RemoveAddressData() {
        Log.d("error_response", session.getUserID());
        //AddresssDetails.progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.removeaddress_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                removeItem(AddresssDetails.pos);
                                notifyDataSetChanged();
                                fooditem.remove(AddresssDetails.pos);
                                //AddresssDetails.progressbar.hideDialog();

                            } else {
                                String msg = jsonObject.getString("message");


                               // AddresssDetails.progressbar.hideDialog();
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

//
                            }
                        } catch (JSONException e) {
                            //AddresssDetails.progressbar.hideDialog();
                            Log.d("error_response", String.valueOf(e));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //AddresssDetails.progressbar.hideDialog();
                        Log.d("error_response", error.toString());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(context, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                        }
                        ;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("address_id", addrid);
                Log.d("paramsforhomeapi", "" + params);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

       public void removeItem(int position) {
       // AddresssDetails.addressarray.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

}