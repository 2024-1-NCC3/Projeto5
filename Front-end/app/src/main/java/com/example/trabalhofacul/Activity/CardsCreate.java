package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofacul.R;
import com.example.trabalhofacul.Util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CardsCreate extends AppCompatActivity {

    private SeekBar seekBarEscala;
    private TextView textoResultado;
    private EditText cardTitulo;
    private EditText cardConteudo;
    private Button btnCriarCard;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_create);
        inicializarComponentes();
        getSupportActionBar().hide();

        // Inicializa o SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        ImageView btvoltar = findViewById(R.id.seta);
        btvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CardsCreate.this, InicialPage.class);
                startActivity(intent);
            }
        });

        seekBarEscala = findViewById(R.id.seekBarEscala);
        textoResultado = findViewById(R.id.textoResultado);

        seekBarEscala.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textoResultado.setText("Esquecimento: " + (i + 1) + "/" + (seekBar.getMax() + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnCriarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarCard();
                Intent intent = new Intent(CardsCreate.this , InicialPage.class);
                startActivity(intent);
            }
        });
    }

    public void inicializarComponentes() {
        cardTitulo = findViewById(R.id.cardTitulo);
        cardConteudo = findViewById(R.id.cardConteudo);
        btnCriarCard = findViewById(R.id.btnCriarCard);
    }

    private void criarCard() {
        final String titulo = cardTitulo.getText().toString().trim();
        final String conteudo = cardConteudo.getText().toString().trim();
        final int nivelEsquecimento = seekBarEscala.getProgress() + 1;

        if (titulo.isEmpty() || conteudo.isEmpty()) {
            Toast.makeText(CardsCreate.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://kkg944-3000.csb.app/cards"; // Substitua pela URL do seu servidor
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("titulo", titulo);
            requestBody.put("conteudo", conteudo);
            requestBody.put("nivel_esquecimento", nivelEsquecimento);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(CardsCreate.this, "Card criado com sucesso", Toast.LENGTH_SHORT).show();
                                // Redirecionar para a tela inicial ou atualizar a lista de cards
                                Intent intent = new Intent(CardsCreate.this, InicialPage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String message = response.getString("message");
                                Toast.makeText(CardsCreate.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CardsCreate.this, "Erro ao criar card: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sessionManager.getToken();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}