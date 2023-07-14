package com.project.ta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailArticle extends AppCompatActivity {

    TextView judul, date, konten;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_article);

        judul = findViewById(R.id.judul);
        date = findViewById(R.id.date);
        konten = findViewById(R.id.konten);
        img = findViewById(R.id.img);

        judul.setText(getIntent().getStringExtra("judul"));
        date.setText(getIntent().getStringExtra("date"));
        konten.setText(getIntent().getStringExtra("konten"));
        Glide.with(this).load(getIntent().getStringExtra("image")).into(img);
    }
}