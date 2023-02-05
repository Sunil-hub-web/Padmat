package com.example.foodhub.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.example.foodhub.adapter.AddressAdapter;
import com.example.foodhub.databinding.AddaddressdetailsBinding;
import com.example.foodhub.extra.ServerLinks;
import com.example.foodhub.extra.SessionManager;
import com.example.foodhub.extra.ViewDialog;
import com.example.foodhub.model.AddressGetSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddresssDetails extends Fragment {

    AddaddressdetailsBinding binding;
    Dialog dialog;
    Spinner spinner_City, spinner_Pincode, spinner_State;

    SessionManager session;
    public static ViewDialog progressbar;
    ImageView backicon;
    ArrayList<String> statelist = new ArrayList<String>();
    ArrayList<String> citylist = new ArrayList<String>();
    ArrayList<String> pinlist = new ArrayList<String>();
    HashMap<String, String> stateid = new HashMap<String, String>();
    HashMap<String, String> cityid = new HashMap<String, String>();
    HashMap<String, String> pinid = new HashMap<String, String>();
    String stid = "", ctid = "", pnid = "";
    Button save_proceed;
    EditText edit_userName, edit_MobileNo, edit_Address,edit_Landmark;

    public static int pos;
    public static ArrayList<AddressGetSet> addressarray = new ArrayList<AddressGetSet>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = AddaddressdetailsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        binding.btnAddAddress.setOnClickListener(view1 -> addAddress());

        progressbar = new ViewDialog(getActivity());
        session = new SessionManager(getActivity());

        getAddressData();

        return view;
    }

    public void addAddress() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.addressdetails);
        //dialog.setCancelable(false);
        spinner_City = dialog.findViewById(R.id.spinner_City);
        spinner_Pincode = dialog.findViewById(R.id.spinner_Pincode);
        spinner_State = dialog.findViewById(R.id.spinner_State);

        edit_userName = dialog.findViewById(R.id.edit_userName);
        edit_MobileNo = dialog.findViewById(R.id.edit_MobileNo);
        edit_Address = dialog.findViewById(R.id.edit_Address);
        edit_Landmark = dialog.findViewById(R.id.edit_Landmark);
        Button btn_Save = dialog.findViewById(R.id.btn_Save);
        Button btn_cancle = dialog.findViewById(R.id.btn_cancle);

        GetLocationsState_url();

        String name = session.getUserName();
        String mobile = session.getUserPhonenumber();

        edit_userName.setText(name);
        edit_MobileNo.setText(mobile);


        spinner_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del = spinner_State.getItemAtPosition(spinner_State.getSelectedItemPosition()).toString();

                stid = stateid.get(del);

                GetLocationsCity_url();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del = spinner_City.getItemAtPosition(spinner_City.getSelectedItemPosition()).toString();

                ctid = cityid.get(del);

                GetLocationsPin_url();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_Pincode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String del = spinner_Pincode.getItemAtPosition(spinner_Pincode.getSelectedItemPosition()).toString();

                pnid = pinid.get(del);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
                Toast.makeText(getActivity().getApplicationContext(), "Select Your City", Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_userName.getText().toString().trim().length() == 0) {
                    edit_userName.setError("enter name");
                    edit_userName.requestFocus();

                } else if (edit_Address.getText().toString().trim().length() == 0) {
                    edit_Address.setError("enter address");
                    edit_Address.requestFocus();

                } else if (stid.length() == 0) {
                    Toast.makeText(getActivity(), "Select State", Toast.LENGTH_SHORT).show();

                } else if (ctid.length() == 0) {
                    Toast.makeText(getActivity(), "Select City", Toast.LENGTH_SHORT).show();

                } else if (pnid.length() == 0) {
                    Toast.makeText(getActivity(), "Select Pincode", Toast.LENGTH_SHORT).show();

                } else if (edit_MobileNo.getText().toString().trim().length() == 0) {
                    edit_MobileNo.setError("enter phone number");
                    edit_MobileNo.requestFocus();

                } else if (edit_MobileNo.getText().toString().trim().length() != 10) {
                    edit_MobileNo.setError("invalid number");
                    edit_MobileNo.requestFocus();

                }else if (edit_Landmark.getText().toString().trim().equals("")) {
                    edit_Landmark.setError("enter Landmark");
                    edit_Landmark.requestFocus();

                } else {

                    AddAddress_url();

                    dialog.dismiss();
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public void GetLocationsState_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.GetLocations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {
                                citylist = new ArrayList<>();
                                cityid = new HashMap<>();
                                pinlist = new ArrayList<>();
                                pinid = new HashMap<>();
                                statelist = new ArrayList<String>();
                                JSONArray All_loc = jsonObject.getJSONArray("All_loc");
                                for (int n = 0; n < All_loc.length(); n++) {

                                    JSONObject productt_json = All_loc.getJSONObject(n);
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");

                                    statelist.add(state_name);
                                    stateid.put(state_name, state_id);
                                }

                                ArrayAdapter<String> dataAdapterVehicle = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, statelist);
                                dataAdapterVehicle.setDropDownViewResource(R.layout.spinneritem);
                                spinner_State.setAdapter(dataAdapterVehicle);

                                ArrayAdapter<String> dataAdapterVehicless = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, citylist);
                                dataAdapterVehicless.setDropDownViewResource(R.layout.spinneritem);
                                spinner_City.setAdapter(dataAdapterVehicless);

                                ArrayAdapter<String> dataAdapterVehiclesse = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, pinlist);
                                dataAdapterVehiclesse.setDropDownViewResource(R.layout.spinneritem);
                                spinner_Pincode.setAdapter(dataAdapterVehiclesse);

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

    public void GetLocationsCity_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.GetLocations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                citylist = new ArrayList<>();
                                cityid = new HashMap<>();

                                pinlist = new ArrayList<>();
                                pinid = new HashMap<>();
                                statelist = new ArrayList<String>();
                                JSONArray All_loc = jsonObject.getJSONArray("All_loc");
                                for (int n = 0; n < All_loc.length(); n++) {

                                    JSONObject productt_json = All_loc.getJSONObject(n);
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");

                                    if (state_id.equalsIgnoreCase(stid)) {
                                        JSONArray city_list = productt_json.getJSONArray("city_list");
                                        for (int o = 0; o < city_list.length(); o++) {

                                            JSONObject ct_json = city_list.getJSONObject(o);
                                            String city_id = ct_json.getString("city_id");
                                            String city_name = ct_json.getString("city_name");

                                            citylist.add(city_name);
                                            cityid.put(city_name, city_id);
                                        }
                                    }


                                }

                                ArrayAdapter<String> dataAdapterVehicless = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, citylist);
                                dataAdapterVehicless.setDropDownViewResource(R.layout.spinneritem);
                                spinner_City.setAdapter(dataAdapterVehicless);

                                ArrayAdapter<String> dataAdapterVehiclesse = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, pinlist);
                                dataAdapterVehiclesse.setDropDownViewResource(R.layout.spinneritem);
                                spinner_Pincode.setAdapter(dataAdapterVehiclesse);

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

    public void GetLocationsPin_url() {
        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ServerLinks.GetLocations_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                pinlist = new ArrayList<>();
                                pinid = new HashMap<>();

                                statelist = new ArrayList<String>();
                                JSONArray All_loc = jsonObject.getJSONArray("All_loc");
                                for (int n = 0; n < All_loc.length(); n++) {

                                    JSONObject productt_json = All_loc.getJSONObject(n);
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");

                                    if (state_id.equalsIgnoreCase(stid)) {
                                        JSONArray city_list = productt_json.getJSONArray("city_list");
                                        for (int o = 0; o < city_list.length(); o++) {

                                            JSONObject ct_json = city_list.getJSONObject(o);
                                            String city_id = ct_json.getString("city_id");
                                            String city_name = ct_json.getString("city_name");

                                            if (city_id.equalsIgnoreCase(ctid)) {
                                                JSONArray pincode_list = ct_json.getJSONArray("pincode_list");
                                                for (int p = 0; p < pincode_list.length(); p++) {

                                                    JSONObject pn_json = pincode_list.getJSONObject(p);
                                                    String pin_id = pn_json.getString("pin_id");
                                                    String pincode = pn_json.getString("pincode");

                                                    pinlist.add(pincode);
                                                    pinid.put(pincode, pin_id);

                                                }

                                            }


                                        }
                                    }
                                }
                                ArrayAdapter<String> dataAdapterVehiclesse = new ArrayAdapter<String>(getActivity(),
                                        R.layout.spinnerdropdownitem, pinlist);
                                dataAdapterVehiclesse.setDropDownViewResource(R.layout.spinneritem);
                                spinner_Pincode.setAdapter(dataAdapterVehiclesse);

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

    public void AddAddress_url() {

        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.AddAddress_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {

                                //getActivity().finish();

                                progressbar.hideDialog();

                                getAddressData();

                            } else {
                                String msg = jsonObject.getString("message");

                                getAddressData();

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
                params.put("name", edit_userName.getText().toString());
                params.put("state_id", stid);
                params.put("city_id", ctid);
                params.put("pincode", pnid);
                params.put("number", edit_MobileNo.getText().toString());
                params.put("address", edit_Address.getText().toString());
                params.put("landmark", edit_Landmark.getText().toString());

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

    public void getAddressData() {

        Log.d("error_response", session.getUserID());
        progressbar.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerLinks.addresslist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cityget", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("success");
                            if (status.equalsIgnoreCase("true")) {


                                addressarray = new ArrayList<AddressGetSet>();
                                JSONArray All_address = jsonObject.getJSONArray("All_address");
                                for(int n = 0; n< All_address.length(); n++){

                                    JSONObject productt_json = All_address.getJSONObject(n);
                                    String addres_id = productt_json.getString("addres_id");
                                    String name = productt_json.getString("name");
                                    String state_id = productt_json.getString("state_id");
                                    String state_name = productt_json.getString("state_name");
                                    String city_id = productt_json.getString("city_id");
                                    String city_name = productt_json.getString("city_name");
                                    String pin_id = productt_json.getString("pin_id");
                                    String pincode = productt_json.getString("pincode");
                                    String number = productt_json.getString("number");
                                    String address = productt_json.getString("address");


                                    addressarray.add(new AddressGetSet(addres_id, name, state_id, state_name, city_id, city_name, pin_id, pincode, number, address));
                                }
                                AddressAdapter adpater = new AddressAdapter(addressarray, getActivity());
                                binding.recyclerAddressDetails.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                                binding.recyclerAddressDetails.setLayoutManager(linearLayoutManager);
                                binding.recyclerAddressDetails.setItemAnimator(new DefaultItemAnimator());
                                binding.recyclerAddressDetails.setAdapter(adpater);

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
