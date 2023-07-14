package com.project.ta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email) EditText semail;
    @BindView(R.id.password) EditText spassword;
    private ProgressDialog progress;

    @OnClick(R.id.login) void masuk() {
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        String email_l = semail.getText().toString();
        String password_l = spassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.base_url+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginAPI api = retrofit.create(LoginAPI.class);
        Call<LoginValue> call = api.login(email_l, password_l);

        call.enqueue(new Callback<LoginValue>() {
            @Override
            public void onResponse(Call<LoginValue> call, Response<LoginValue> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    SharedPreferences mSettings = getSharedPreferences("jwt", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString("token", token);
                    editor.apply();

                    Log.d("Token", token);

                    Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this, "Ada kesalahan", Toast.LENGTH_SHORT).show();
                }

                progress.dismiss();
            }

            @Override
            public void onFailure(Call<LoginValue> call, Throwable t) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    public void Register(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}