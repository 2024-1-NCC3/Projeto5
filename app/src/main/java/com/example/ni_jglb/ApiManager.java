package com.example.ni_jglb;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;

public class ApiManager {

    private static final String BASE_URL = "https://kvdyl5-3000.csb.app/";

    public static void getUsuarios(Context context, final VolleyCallback callback) {
        String url = BASE_URL + "tudo";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        List<Usuario> usuarios = new ArrayList<>();
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Usuario usuario = new Usuario();
                            usuario.setNome(object.getString("name"));
                            usuario.setCpf(object.getString("CPF"));
                            usuarios.add(usuario);
                        }
                        callback.onSuccess(usuarios);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onFailure(e.getMessage());
                    }
                },
                error -> callback.onFailure(error.getMessage())
        );

        AccessController MyApplication;
        Volley.newRequestQueue(context).add(request);

    }



    public interface VolleyCallback {
        void onSuccess(Object response);
        void onFailure(String message);
    }
}

