package com.example.nextstepapp.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFERENCES = "proxima_etapa_session";

    private static final String TOKEN_KEY = "jwt_token";
    private static final String USER_NAME_KEY = "user_name";
    private static final String USER_ID_KEY = "user_id";
    private static final String USER_ROLE_KEY = "user_role";

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

    public void salvarUsuario(String nome) {
        preferences.edit().putString(USER_NAME_KEY, nome).apply();
    }

    public String obterNomeUsuario() {
        return preferences.getString(USER_NAME_KEY, "Estudante");
    }

    public void salvarUserId(String id) {
        preferences.edit().putString(USER_ID_KEY, id).apply();
    }

    public String obterUserId() {
        return preferences.getString(USER_ID_KEY, "aluno@teste.com");
    }

    public void salvarUserRole(String role) {
        preferences.edit().putString(USER_ROLE_KEY, role).apply();
    }

    public String obterUserRole() {
        return preferences.getString(USER_ROLE_KEY, "student");
    }

    public void limparSessao() {
        preferences.edit().clear().apply();
    }
}
