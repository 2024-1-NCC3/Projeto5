package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.trabalhofacul.R;

public class CardsPage extends AppCompatActivity {

    private SeekBar     seekBarEscala;
    private TextView    textoResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_page);

        ImageView btvoltar = findViewById(R.id.seta);
        btvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardsPage.this,InicialPage.class);
                startActivity(intent);
            }
        });

        seekBarEscala = findViewById(R.id.seekBarEscala);
        textoResultado = findViewById(R.id.textoResultado);

        seekBarEscala.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textoResultado.setText("Esquecimento: " + i + "/" + seekBar.getMax());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }
}