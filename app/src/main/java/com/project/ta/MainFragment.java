package com.project.ta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements AdapterData2.OnArticleListener {

    private ProgressDialog progress;

    //    ListView listView;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterData2 adapterData2;
    List<DataModel> listData;

    String names[] = {"osi", "cozy", "dawi"};

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    private Object findViewById(int listView) {
        return listView;
    }

    private void loadListData() {
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //        get token
        SharedPreferences mSettings = getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseUrl.base_url + "/artikel?token=" + token, new Response.Listener<String>() {
            public void onResponse(String response) {
//                Log.d("response", new String(response));

                try {
                    JSONObject obj = new JSONObject(response);


                    JSONArray artikelArray = obj.getJSONArray("artikel");
//                    System.out.println(artikelArray.getJSONObject(0).getString("konten"));
                    for (int i=0; i < artikelArray.length(); i++) {
                        JSONObject artikelObject = artikelArray.getJSONObject(i);
                        DataModel dataModel = new DataModel();
                        dataModel.setId(artikelObject.getString("id"));
                        dataModel.setTitle(artikelObject.getString("judul"));
                        dataModel.setKonten(artikelObject.getString("konten"));
                        dataModel.setImage(artikelObject.getString("image"));
                        dataModel.setDate(artikelObject.getString("created_at"));
                        listData.add(dataModel);
                    }
//                    System.out.println(listData);
//
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterData2 = new AdapterData2(getContext() ,listData, MainFragment.this);
                    recyclerView.setAdapter(adapterData2);
//                    adapterData2.notifyDataSetChanged();

//                    AdapterData adapter = new AdapterData(getContext(), listData);
//                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse.statusCode == 401){
                    Toast.makeText(getActivity(), "Sesi anda habis, login dulu!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Log.d("Error load", String.valueOf(error));
                    Toast.makeText(getActivity(), "Koneksi Terputus", Toast.LENGTH_SHORT).show();
                }

                progress.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
//


//    @Override
//    public void onStart() {
//        super.onStart();
//        loadListData();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //2. event
        // Inflate the layout for this fragment

        listData = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, container, false);
        recyclerView = view.findViewById(R.id.rvData);


        loadListData();
        return view;
    }

    private void getDetailArticle(String idx) {
        SharedPreferences mSettings = getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseUrl.base_url + "/artikel/" + idx + "?token="+token, new Response.Listener<String>() {
            public void onResponse(String response) {
//                Log.d("response", new String(response));

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONObject obk = obj.getJSONObject("artikel");

                    String judul = obk.getString("judul");
                    String konten = obk.getString("konten");
                    String image = obk.getString("image");
                    String date = obk.getString("created_at");

                    Intent intent = new Intent(getActivity(), DetailArticle.class);

                    intent.putExtra("judul", judul);
                    intent.putExtra("konten", konten);
                    intent.putExtra("image", image);
                    intent.putExtra("date", date);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    //
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //3.event

        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onArticleClick(int position) {
        getDetailArticle(listData.get(position).getId());
    }
}