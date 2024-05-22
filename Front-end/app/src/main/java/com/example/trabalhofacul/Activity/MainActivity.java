package com.example.trabalhofacul.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabalhofacul.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button btlogin = findViewById(R.id.btn_login);
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginPage.class);
                startActivity(intent);
            }
        });

        TextView btcadastro = findViewById(R.id.btn_cadastro);
        btcadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CadastroPage.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();

    }


}