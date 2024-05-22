package com.example.trabalhofacul.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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

public class EditCard extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextContent;
    private SeekBar seekBarLevelOfForgetting;
    private Button btnSave;
    private int cardId;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        getSupportActionBar().hide();

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        seekBarLevelOfForgetting = findViewById(R.id.seekBarLevelOfForgetting);
        btnSave = findViewById(R.id.btnSave);

        sessionManager = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            cardId = intent.getIntExtra("id", -1);
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            int levelOfForgetting = intent.getIntExtra("levelOfForgetting", 1);

            editTextTitle.setText(title);
            editTextContent.setText(content);
            seekBarLevelOfForgetting.setProgress(levelOfForgetting);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCard();
            }
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveCard() {
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        int levelOfForgetting = seekBarLevelOfForgetting.getProgress();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://kkg944-3000.csb.app/cards/" + cardId;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("titulo", title);
            jsonBody.put("conteudo", content);
            jsonBody.put("nivel_esquecimento", levelOfForgetting);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditCard.this, "Card atualizado com sucesso", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCard.this, "Erro ao atualizar card: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
