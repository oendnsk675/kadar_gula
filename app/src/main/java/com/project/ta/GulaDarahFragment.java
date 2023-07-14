package com.project.ta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GulaDarahFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GulaDarahFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressDialog progress;

    List<GulaDarahModel> listData;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public GulaDarahFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KaloriFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GulaDarahFragment newInstance(String param1, String param2) {
        GulaDarahFragment fragment = new GulaDarahFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listData = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_kalori, container, false);
        recyclerView = view.findViewById(R.id.rvDataGulaDarah);

        return  view;
    }

    public void onStart() {
        super.onStart();
        load_data();
    }

    private void load_data() {
        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        SharedPreferences mSettings = getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");
//        Log.d("Token", BaseUrl.base_url + "/gula_darah?token="+token);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BaseUrl.base_url + "/gula_darah?token="+token, new Response.Listener<String>() {
            public void onResponse(String response) {
//                Log.d("response", new String(response));

                try {
                    JSONObject obj = new JSONObject(response);


                    JSONArray artikelArray = obj.getJSONArray("data");
//                    System.out.println(artikelArray.getJSONObject(0).getString("konten"));
                    Log.d("Data", String.valueOf(obj));
                    for (int i=0; i < artikelArray.length(); i++) {
                        JSONObject artikelObject = artikelArray.getJSONObject(i);
                        GulaDarahModel dataModel = new GulaDarahModel();
                        dataModel.setData(artikelObject.getString("kadar_gula"));
                        dataModel.setDate(artikelObject.getString("date"));
//                        Log.d("Data log", String.valueOf());
                        String status = "Normal";
                        if (Float.valueOf(dataModel.getData()) < 100){
                            status = "Normal";
                        }else if(Float.valueOf(dataModel.getData()) >= 100 && Float.valueOf(dataModel.getData()) <= 199){
                            status = "Pra-diabetes";
                        }else if(Float.valueOf(dataModel.getData()) >= 200){
                            status = "Diabetes";
                        }else{
                            status = "Unknown";
                        }

                        dataModel.setStatus(status);
//                        Log.d("Data model", String.valueOf(n));
                        listData.add(dataModel);
                    }

////
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    GulaDarahAdapter gulaDarahAdapter = new GulaDarahAdapter(listData, getActivity());
                    recyclerView.setAdapter(gulaDarahAdapter);
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
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}