package com.example.trabalhofacul.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofacul.R;
import com.example.trabalhofacul.Util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {
    Button btnEntrar;
    EditText campoEmail, campoSenha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        inicializarComponentes();

        // Inicializar SessionManager
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        // Verificar se o usuário já está autenticado
        if (sessionManager.isLoggedIn()) {
            // Se o usuário já estiver logado, vá para a tela principal
            startActivity(new Intent(LoginPage.this, InicialPage.class));
            finish(); // Encerrar a atividade de login para que o usuário não possa voltar para ela
        }
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString().trim();
                String password = campoSenha.getText().toString().trim();

                // Verificar se os campos de e-mail e senha estão preenchidos
                if (!email.isEmpty() && !password.isEmpty()) {

                    logar(email, password);
                } else {
                    // Exibir mensagem de erro se os campos estiverem vazios
                    Toast.makeText(LoginPage.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView voltar = findViewById(R.id.btnvoltar);
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();
    }


    private void logar(String email, String senha) {
        String url = "https://kkg944-3000.csb.app/login";

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", email);
            jsonBody.put("senha",senha);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final Context context = this;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            String token = response.getString("token");
                            SessionManager sessionManager = new SessionManager(context);
                            sessionManager.saveToken(token);

                            startActivity(new Intent(LoginPage.this, InicialPage.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tratamentos de erro do servidor
                        Toast.makeText(LoginPage.this, "Erro ao fazer login: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Login", "Erro ao fazer login", error);
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }


    private void inicializarComponentes(){
        campoEmail = findViewById(R.id.campoEmailLogin);
        campoSenha = findViewById(R.id.campoSenhaLogin);
        btnEntrar = findViewById(R.id.btn_entrar);



    }
}