package com.example.trabalhofacul.Util;

import static com.example.trabalhofacul.Activity.InicialPage.REQUEST_CODE_DETAIL;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trabalhofacul.Activity.CardDetailActivity;
import com.example.trabalhofacul.Activity.InicialPage;
import com.example.trabalhofacul.R;
import com.example.trabalhofacul.model.Card;

import java.util.List;

public class CardAdapter extends ArrayAdapter<Card> {
    private Context context;
    private List<Card> cards;

    public CardAdapter(Context context, List<Card> cards) {
        super(context, 0, cards);
        this.context = context;
        this.cards = cards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtém o card na posição atual
        Card card = getItem(position);

        // Verifica se a View está sendo reutilizada, caso contrário, infla a View
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_card, parent, false);
        }

        // Inicializa os elementos da View
        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewContent = convertView.findViewById(R.id.textViewContent);
        TextView textViewEsquecimento= convertView.findViewById(R.id.textViewEsquecimento);

        // Define os valores dos elementos da View
        textViewTitle.setText(card.getTitle());
        textViewContent.setText(TextUtils.substring(card.getContent(), 0, Math.min(card.getContent().length(), 15)) + "...");



        // Adiciona o clique no item para abrir os detalhes do card
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CardDetailActivity.class);
                intent.putExtra("id", card.getId());
                intent.putExtra("title", card.getTitle());
                intent.putExtra("content", card.getContent());
                intent.putExtra("nivel_esquecimento",card.getForgettingLevel());
                ((InicialPage) getContext()).startActivityForResult(intent, REQUEST_CODE_DETAIL); // Usa startActivityForResult
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((InicialPage) context).showEditButton(card);
                return true;
            }
        });


        // Retorna a View para ser exibida na ListView
        return convertView;
    }
}
