package com.project.ta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONObject user_info = new JSONObject();

    TextView  nama_user, btn_logout, btn_detail_profile;
    ImageView image_user;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
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

        load_data();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image_user = view.findViewById(R.id.image_user);
        nama_user = view.findViewById(R.id.nama_user);
        btn_detail_profile = view.findViewById(R.id.btn_detail_profile);
        btn_logout = view.findViewById(R.id.btn_logout);
//        Fragment selectedFragment = null;

        btn_detail_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailProfileFragment()).commit();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        load_data();
    }

    private void logout() {
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        SharedPreferences mSettings = getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                BaseUrl.base_url + "/logout?token="+token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        progress.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void load_data() {
        JSONObject object = new JSONObject();
        //        get token
        SharedPreferences mSettings = getActivity().getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");


        System.out.println(object);

        JsonObjectRequest  stringRequest = new JsonObjectRequest(Request.Method.GET,
                BaseUrl.base_url + "/profile?token=" + token,
                object ,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            nama_user.setText(data.getString("name"));
                            if(!data.getString("image").equals("null"))
                                if (getActivity() != null)
                                    Glide.with(getActivity()).load(data.getString("image")).into(image_user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
                        if(error.networkResponse.statusCode == 401){
                            Toast.makeText(getActivity(), "Sesi anda habis, login dulu!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d("Error load", String.valueOf(error));
                            Toast.makeText(getActivity(), "Koneksi Terputus", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        load_data();
    }
}