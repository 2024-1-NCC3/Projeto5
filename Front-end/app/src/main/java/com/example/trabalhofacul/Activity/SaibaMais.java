package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.trabalhofacul.R;

public class SaibaMais extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saiba_mais);
        getSupportActionBar().hide();

        ImageView btvoltar = findViewById(R.id.setasaiba);
        btvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaibaMais.this,InfoPage.class);
                startActivity(intent);
            }
        });
    }
}