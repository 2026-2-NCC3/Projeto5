package com.example.nextstepapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFERENCES = "proxima_etapa_session";

    private static final String TOKEN_KEY = "jwt_token";
    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void salvarToken(String token) {
        preferences.edit().putString(TOKEN_KEY, token).apply();
    }
    public String obterToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public void limparSessao() {
        preferences.edit().clear().apply();
    }
}