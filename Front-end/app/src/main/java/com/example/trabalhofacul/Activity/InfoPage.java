package com.example.trabalhofacul.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofacul.Util.SessionManager;




import com.example.trabalhofacul.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoPage extends AppCompatActivity {
SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_page);
        getSupportActionBar().hide();
        ImageView btfechar = findViewById(R.id.btfechar);
        sessionManager  = new SessionManager(getApplicationContext());

        TextView deletarUsuario = findViewById(R.id.deleteUser);
        deletarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirUsuario(view);
            }
        });

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
    public void excluirUsuario(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Usuário")
                .setMessage("Tem certeza de que deseja excluir sua conta? Esta ação não pode ser desfeita.")
                .setPositiveButton("Sim", (dialog, which) -> {
                    String token = sessionManager.getToken();
                    int userId = sessionManager.getUserId(); // Recupera o ID do usuário do token

                    if (token == null || token.isEmpty() || userId == -1) {
                        Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String url = "https://kkg944-3000.csb.app/user/" + userId;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // Verifica se a resposta possui a chave "message"
                                        if (response.has("message") && response.getString("message").equals("Usuário excluído com sucesso")) {
                                            Toast.makeText(InfoPage.this, "Usuário excluído com sucesso", Toast.LENGTH_SHORT).show();
                                            sessionManager.logout();
                                            Intent intent = new Intent(InfoPage.this, LoginPage.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(InfoPage.this, "Erro ao excluir usuário", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(InfoPage.this, "Erro ao excluir usuário", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(InfoPage.this, "Erro ao excluir usuário: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + token);
                            return headers;
                        }
                    };

                    Volley.newRequestQueue(this).add(jsonObjectRequest);
                })
                .setNegativeButton("Não", null)
                .show();
    }

}