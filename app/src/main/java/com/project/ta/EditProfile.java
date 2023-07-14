package com.project.ta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditProfile extends AppCompatActivity {

//    private static final String URL = "http://192.168.1.2/monitoring/api/profile";
    final Calendar myCalendar= Calendar.getInstance();
    final int CODE_GALERY = 999;
    private ProgressDialog progress;

    Button btn_save_edit, btn_change;
    TextView et_nama_user, et_email, et_alamat, et_jk;
    EditText et_tanggal_lahir;
    ImageView image_user;
    RadioButton laki_laki, perempuan;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        image_user = findViewById(R.id.image_user);
        et_nama_user = findViewById(R.id.et_nama_user);
        et_tanggal_lahir = findViewById(R.id.et_tanggal_lahir);
        et_email = findViewById(R.id.et_email);
        et_alamat = findViewById(R.id.et_alamat);
        laki_laki = findViewById(R.id.laki_laki);
        perempuan = findViewById(R.id.perempuan);
        btn_save_edit = findViewById(R.id.btn_save_edit);
        btn_change = findViewById(R.id.btn_change);

        et_tanggal_lahir.setText(getIntent().getStringExtra("tanggal_lahir"));
        et_nama_user.setText(getIntent().getStringExtra("nama_user"));
        et_email.setText(getIntent().getStringExtra("email"));
        et_alamat.setText(getIntent().getStringExtra("alamat"));
        if(!getIntent().getStringExtra("image").equals("null"))
            Glide.with(this).load(getIntent().getStringExtra("image")).into(image_user);
        if(getIntent().getStringExtra("jk").equalsIgnoreCase("Laki-laki")){
            laki_laki.setChecked(true);
        }else if(getIntent().getStringExtra("jk").equalsIgnoreCase("perempuan")){
            perempuan.setChecked(true);
        }

        btn_save_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                update_profile();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditProfile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALERY
                );
            }
        });

//        date picker
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        et_tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditProfile.this ,
                        date ,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == CODE_GALERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select image"), CODE_GALERY);
            }else{
                Toast.makeText(this, "You dont have permission!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALERY && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();

            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                image_user.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateLabel(){
        String myFormat="y-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        Log.d("tanggal", dateFormat.format(myCalendar.getTime()));
        et_tanggal_lahir.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void update_profile() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        JSONObject data_user = new JSONObject();
        SharedPreferences mSettings = getSharedPreferences("jwt", Context.MODE_PRIVATE);
        String token = mSettings.getString("token", "");
        try {
            String gender;
            if(laki_laki.isChecked()){
                gender = "Laki-laki";
            }else if(perempuan.isChecked()){
                gender = "Perempuan";
            }else{
                gender = null;
            }


            data_user.put("token", token);
            data_user.put("name", et_nama_user.getText().toString().equalsIgnoreCase("null") ? null : et_nama_user.getText().toString());
            data_user.put("date_birth", et_tanggal_lahir.getText().toString().equalsIgnoreCase("null") ? null : et_tanggal_lahir.getText().toString());
            data_user.put("gender", gender);
            if(bitmap != null){
                data_user.put("image", imageToString(bitmap));
            }
            data_user.put("address", et_alamat.getText().toString().equalsIgnoreCase("null") ? null : et_alamat.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                BaseUrl.base_url + "/profile",
                data_user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            JSONObject data = response.getJSONObject("data");
                            if (status.equals("success")){
                                Toast.makeText(EditProfile.this, "Edit Profile Berhasil", Toast.LENGTH_SHORT).show();
//                                load data
//                                load_data();
                            }

                            Log.d("LOF PROFILE", String.valueOf(data));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 401){
                            Toast.makeText(EditProfile.this, "Sesi anda habis, login dulu!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfile.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(EditProfile.this, "Koneksi Terputus", Toast.LENGTH_SHORT).show();
                        }

                        progress.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(jsonObjectRequest);

    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encode  = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encode;
    }

}