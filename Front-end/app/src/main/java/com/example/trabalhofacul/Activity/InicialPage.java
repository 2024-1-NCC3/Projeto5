package com.example.trabalhofacul.Activity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofacul.R;
import com.example.trabalhofacul.Util.CardAdapter;
import com.example.trabalhofacul.Util.SessionManager;
import com.example.trabalhofacul.model.Card;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicialPage extends AppCompatActivity {
    private ListView listViewCards;
    private CardAdapter cardAdapter;
    private List<Card> cardList;
    private ImageView btnEdit;
    private SessionManager sessionManager;
    public static final int REQUEST_CODE_DETAIL = 1;
    private static final String TAG = "InicialPage";
    private Card selectedCard;  // Variável de instância para manter o card selecionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial_page);

        listViewCards = findViewById(R.id.listViewCards);
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(this, cardList);
        listViewCards.setAdapter(cardAdapter);
        btnEdit = findViewById(R.id.imageViewBtnEdit);

        sessionManager = new SessionManager(getApplicationContext());

        btnEdit.setVisibility(View.INVISIBLE);

        RelativeLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (btnEdit.getVisibility() == View.VISIBLE) {
                    animateButtonFadeOut();
                }
                return false;
            }
        });
        ImageView btperfil = findViewById(R.id.btperfil);

        btperfil.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(InicialPage.this,InfoPage.class);

                startActivity(intent);

            }

        });

        listViewCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card card = cardList.get(position);
                Intent intent = new Intent(InicialPage.this, CardDetailActivity.class);
                intent.putExtra("titulo", card.getTitle());
                intent.putExtra("conteudo", card.getContent());
                intent.putExtra("cardId", card.getId());
                startActivity(intent);
            }
        });
        listViewCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCard = cardList.get(position);
                animateButtonFadeIn();
                return true;
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCard != null) {
                    Intent intent = new Intent(InicialPage.this, EditCard.class);
                    intent.putExtra("id", selectedCard.getId());
                    intent.putExtra("title", selectedCard.getTitle());
                    intent.putExtra("content", selectedCard.getContent());
                    intent.putExtra("levelOfForgetting", selectedCard.getForgettingLevel());
                    int REQUEST_CODE_EDIT = 0;
                    startActivityForResult(intent, REQUEST_CODE_EDIT);
                }
            }
        });

        loadCards();

        Button btcriar = findViewById(R.id.btndecks);
        btcriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InicialPage.this, CardsCreate.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadCards(); // Recarrega os cards sempre que a atividade for retomada
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {
            loadCards(); // Recarrega os cards quando a atividade retornar um resultado OK
        }
    }

    private void loadCards() {
        if (sessionManager.isLoggedIn()) {
            String token = sessionManager.getToken();

            if (token != null && !token.isEmpty()) {
                String url = "https://kkg944-3000.csb.app/getcard";

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, "Response received: " + response.toString()); // Log de resposta recebida
                                try {
                                    cardList.clear();

                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject cardObject = response.getJSONObject(i);
                                        int id = cardObject.getInt("id");
                                        String titulo = cardObject.getString("titulo");
                                        String conteudo = cardObject.getString("conteudo");
                                        int nivelEsquecimento = cardObject.getInt("nivel_esquecimento");
                                        Card card = new Card(id, titulo, conteudo, nivelEsquecimento);
                                        cardList.add(card);
                                    }

                                    cardAdapter.notifyDataSetChanged();
                                    Log.d(TAG, "Cards loaded successfully"); // Log de sucesso ao carregar os cards
                                } catch (JSONException e) {
                                    Log.e(TAG, "Error processing response: " + e.getMessage(), e);
                                    Toast.makeText(InicialPage.this, "Erro ao processar os cards", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error loading cards: " + error.getMessage(), error);
                        Toast.makeText(InicialPage.this, "Erro ao carregar cards: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };

                Volley.newRequestQueue(this).add(jsonArrayRequest);
            } else {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            // Redirecionar para a tela de login se necessário
        }
    }

    public void showEditButton(Card card) {
        selectedCard = card;
        btnEdit.setVisibility(View.VISIBLE);
    }
    private void animateButtonFadeIn() {
        btnEdit.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        btnEdit.startAnimation(fadeIn);
    }

    private void animateButtonFadeOut() {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(500);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                btnEdit.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationStart(Animation animation) {}
        });
        btnEdit.startAnimation(fadeOut);
    }


}