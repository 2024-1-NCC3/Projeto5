package com.example.trabalhofacul.Activity;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofacul.R;
import com.example.trabalhofacul.model.Usuario;

import java.util.HashMap;
import java.util.Map;

public class CadastroPage extends AppCompatActivity {

    Usuario usuario;
//    FirebaseAuth autenticacao;
    EditText campoNome, campoEmail, campoSenha;
    Button btnCadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_page);
        inicializar();

        TextView btvoltar = findViewById(R.id.btn_voltar);
        btvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();
    }


    private void inicializar(){
        campoNome = findViewById(R.id.nomeUsuario);
        campoEmail = findViewById(R.id.emailUsuario);
        campoSenha = findViewById(R.id.senhaUsuario);
        btnCadastrar = findViewById(R.id.btn_cadastrar);
    }
    public void validarCampos(View view){
        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){

                     usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    //cadastro do usuario
                    cadastrarUsuario(usuario);

                }else{
                    Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Preencha o Email.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT).show();
        }
    }


    private void cadastrarUsuario(final Usuario usuario) {
        String url = "https://kkg944-3000.csb.app/user";

        // Cria uma solicitação POST usando Volley
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(CadastroPage.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        // Você pode redirecionar para outra atividade ou realizar outras ações após o cadastro
                        Intent intentLogin = new Intent(CadastroPage.this, LoginPage.class);
                        startActivity(intentLogin);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Lida com erros de solicitação aqui
                        Toast.makeText(CadastroPage.this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
                        Log.e("CadastroPage", "Erro ao cadastrar usuário: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parâmetros a serem enviados para o servidor
                Map<String, String> params = new HashMap<>();
                params.put("email", usuario.getEmail());
                params.put("senha", usuario.getSenha());
                return params;
            }
        };


        Volley.newRequestQueue(this).add(request);
    }



}