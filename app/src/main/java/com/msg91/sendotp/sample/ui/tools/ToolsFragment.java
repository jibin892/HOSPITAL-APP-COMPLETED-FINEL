package com.msg91.sendotp.sample.ui.tools;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.msg91.sendotp.sample.Cheque1;
import com.msg91.sendotp.sample.Cheque2;
import com.msg91.sendotp.sample.Chequeadapter1;
import com.msg91.sendotp.sample.Chequeadapter2;
import com.msg91.sendotp.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ToolsFragment extends Fragment {

    View root;
    List<Cheque2> productList;
    SwipeRefreshLayout s;
    //the recyclerview
    RecyclerView recyclerView;
    SwipeRefreshLayout swipe;
    SharedPreferences sh,shh;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        root= inflater.inflate(R.layout.fragment_tools, container, false);
        loadProducts();



        recyclerView = root.findViewById(R.id.recylcerViewre);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipe=root.findViewById(R.id.swiperefreshre);
        sh=getActivity().getSharedPreferences("Official1",MODE_PRIVATE);


        shh=getActivity().getSharedPreferences("data",MODE_PRIVATE);


        productList = new ArrayList<>();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();

                loadProducts();



            }
        });
        return root;
    }

    private void loadProducts() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://androidprojectstechsays.000webhostapp.com/Hospital_Manegument_system/appoment_reject_view.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        swipe.setRefreshing(false);
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = array.length()-1; i >=0; i--) {


                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                //adding the product to product list
                                productList.add(new Cheque2(
                                        product.getString("hospital"),
                                        product.getString("deparement"),
                                        product.getString("docter_name"),
                                        product.getString("date"),
                                        product.getString("note")

                                ));
                            }


                            Chequeadapter2 adapter = new Chequeadapter2(getActivity(), productList);
                            // adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }


                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("ph",shh.getString("phone",null));



                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }





}

