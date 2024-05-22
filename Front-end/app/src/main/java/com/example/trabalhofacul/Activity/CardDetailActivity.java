package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofacul.R;
import com.example.trabalhofacul.Util.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class CardDetailActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewContent;
    private TextView textViewEsquecimento;
    private Button buttonDelete;
    private SessionManager sessionManager;
    private int cardId;
    private ImageView btVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        getSupportActionBar().hide();

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewContent = findViewById(R.id.textViewContent);
        buttonDelete = findViewById(R.id.buttonDelete);
        textViewEsquecimento = findViewById(R.id.textViewEsquecimento);
        btVoltar = findViewById(R.id.seta2);
        sessionManager = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int esquecimento = intent.getIntExtra("nivel_esquecimento", -1);
        cardId = intent.getIntExtra("id", -1);
        Log.d("CardDetailActivity", "Received Card ID: " + cardId); // Adiciona o log do ID recebido

        if (cardId == -1) {
            Toast.makeText(this, "Erro ao carregar o card", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textViewTitle.setText(title);
        textViewContent.setText(content);
        textViewEsquecimento.setText("Nível de esquecimento: " + esquecimento);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard();
            }
        });
        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentInicial = new Intent(CardDetailActivity.this, InicialPage.class);
                startActivity(intentInicial);
            }
        });
    }

    private void deleteCard() {
        String url = "https://kkg944-3000.csb.app/cards/" + cardId; // Substitua pela URL do seu servidor

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CardDetailActivity.this, "Card excluído com sucesso", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish(); // Fecha a atividade e retorna para a anterior
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CardDetailActivity.this, "Erro ao excluir card: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
