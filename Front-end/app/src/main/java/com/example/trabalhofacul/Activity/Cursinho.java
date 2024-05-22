package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.trabalhofacul.R;

public class Cursinho extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursinho);
        getSupportActionBar().hide();

        ImageView btvoltar = findViewById(R.id.setacurso);
        btvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cursinho.this,InfoPage.class);
                startActivity(intent);
            }
        });
    }
}