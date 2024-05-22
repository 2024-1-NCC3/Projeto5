package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.trabalhofacul.Util.SessionManager;


import com.example.trabalhofacul.R;

public class InfoPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);
        getSupportActionBar().hide();
        ImageView btfechar = findViewById(R.id.btfechar);

        btfechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoPage.this,InicialPage.class);
                startActivity(intent);
            }
        });
        TextView btVesti = findViewById(R.id.txtVesti);
        btVesti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoPage.this,Vestibular.class);
                startActivity(intent);
            }
        });
        TextView btCurso = findViewById(R.id.txtCurso);
        btCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoPage.this,Cursinho.class);
                startActivity(intent);
            }
        });
        TextView btSaibaM = findViewById(R.id.txtSaibaM);
        btSaibaM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoPage.this,SaibaMais.class);
                startActivity(intent);
            }
        });
        TextView btLogout = findViewById(R.id.LOGOUT);
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logout();
                Intent intent = new Intent(InfoPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}