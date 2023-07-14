package com.project.ta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password;
    Button btn_register;
    private ProgressDialog progress;

    @BindView(R.id.name) EditText sname;
    @BindView(R.id.email) EditText semail;
    @BindView(R.id.password) EditText spassword;

    @OnClick(R.id.btn_register) void daftar() {
        //membuat progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        String name_l = sname.getText().toString();
        String email_l = semail.getText().toString();
        String password_l = spassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.base_url+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegiisterAPI api = retrofit.create(RegiisterAPI.class);
        Call<RegisterValue> call = api.register(name_l, email_l, password_l);

        call.enqueue(new Callback<RegisterValue>() {
            @Override
            public void onResponse(Call<RegisterValue> call, Response<RegisterValue> response) {
                if (response.isSuccessful()) {
                    // use response data and do some fancy stuff :)

//                    String token = response.body().getToken();
//                    SharedPreferences mSettings = getSharedPreferences("jwt", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = mSettings.edit();
//                    editor.putString("token", token);
//                    editor.apply();


//                    String tokenGet = mSettings.getString("token", "");
                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "Ada kesalahan", Toast.LENGTH_SHORT).show();
                }
                progress.dismiss();
            }


            @Override
            public void onFailure(Call<RegisterValue> call, Throwable t) {
//                t.printStackTrace();
//                System.out.println("error: "+ t);
                progress.dismiss();
                Toast.makeText(RegisterActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
    }

    public void Login(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}