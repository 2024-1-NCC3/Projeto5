package com.example.trabalhofacul.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;

import java.util.Calendar;

public class SessionManager {
    private static final String PREF_NAME = "MyAppPref";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_LOGIN_TIME = "login_time";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }


    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.putLong(KEY_LOGIN_TIME, Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

    public String getToken() {
        // Verifica se o token expirou
        if (isTokenExpired()) {
            // Se o token expirou, limpa o token e retorna null
            clearToken();
            return null;
        } else {
            // Se o token ainda estiver válido, retorna o token armazenado
            return pref.getString(KEY_TOKEN, null);
        }
    }
    private boolean isTokenExpired() {
        long loginTime = pref.getLong(KEY_LOGIN_TIME, 0);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long elapsedTime = currentTime - loginTime;
        // Define o tempo de expiração da sessão
        long sessionDuration = 1440 * 60 * 1000; // 24 horas em milissegundos

        // Verifica se o tempo decorrido desde o login é maior do que a duração da sessão
        return elapsedTime > sessionDuration;
    }

    private void clearToken() {
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_LOGIN_TIME);
        editor.apply();
    }
    public int getUserId() {
        String token = getToken();
        if (token != null) {
            JWT jwt = new JWT(token);
            return jwt.getClaim("id").asInt();
        }
        return -1;
    }

    public void logout() {
        clearToken();
    }
}
