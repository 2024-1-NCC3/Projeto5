package com.example.ni_jglb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.txtInfo); // Substitua por id do seu TextView

        Button button = findViewById(R.id.search); // Substitua por id do seu Button
        button.setOnClickListener(v -> ApiManager.getUsuarios(getApplicationContext(), new ApiManager.VolleyCallback() {
            @Override
            public void onSuccess(Object response) {
                List<Usuario> usuarios = (List<Usuario>) response;
                StringBuilder builder = new StringBuilder();
                for (Usuario usuario : usuarios) {
                    builder.append("Nome: ").append(usuario.getNome()).append("\n");
                    builder.append("CPF: ").append(usuario.getCpf()).append("\n\n");
                }
                textView.setText(builder.toString());
            }

            @Override
            public void onFailure(String message) {
                textView.setText("Erro: " + message);
            }
        }));
    }
}
