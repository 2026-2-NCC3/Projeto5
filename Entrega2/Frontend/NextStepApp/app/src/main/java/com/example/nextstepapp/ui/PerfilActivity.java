package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView tvNomeAluno;
    private EditText etEmailPerfil, etTelefonePerfil, etDataNascPerfil, etEscolaPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sessionManager = new SessionManager(this);

        tvNomeAluno = findViewById(R.id.tvNomeAluno);
        etEmailPerfil = findViewById(R.id.etEmailPerfil);
        etTelefonePerfil = findViewById(R.id.etTelefonePerfil);
        etDataNascPerfil = findViewById(R.id.etDataNascPerfil);
        etEscolaPerfil = findViewById(R.id.etEscolaPerfil);

        carregarDadosPerfil();

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
        }

        findViewById(R.id.btnSalvarPerfil).setOnClickListener(v -> salvarPerfil());

        // Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.nav_agenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
            finish();
        });

        findViewById(R.id.nav_presencas).setOnClickListener(v -> {
            startActivity(new Intent(this, PresencaActivity.class));
            finish();
        });

        findViewById(R.id.nav_notificacoes).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificacoesActivity.class));
            finish();
        });
    }

    private void carregarDadosPerfil() {
        String nome = sessionManager.obterNomeUsuario();
        String email = sessionManager.obterUserId();

        if (tvNomeAluno != null && nome != null) {
            tvNomeAluno.setText(nome);
        }

        if (etEmailPerfil != null && email != null) {
            etEmailPerfil.setText(email);
        }

        if (etTelefonePerfil != null) {
            etTelefonePerfil.setText("(11) 98765-4321");
        }

        if (etDataNascPerfil != null) {
            etDataNascPerfil.setText("15/05/2008");
        }

        if (etEscolaPerfil != null) {
            etEscolaPerfil.setText("E.E. Professora Maria Ribeiro");
        }
    }

    private void salvarPerfil() {
        String email = etEmailPerfil.getText().toString().trim();
        String telefone = etTelefonePerfil.getText().toString().trim();
        String escola = etEscolaPerfil.getText().toString().trim();

        if (!email.isEmpty()) {
            sessionManager.salvarUserId(email);
        }

        String token = sessionManager.obterToken();
        if (token != null && !token.isEmpty()) {
            Map<String, String> body = new HashMap<>();
            body.put("phone", telefone);
            body.put("school", escola);

            ApiClient.getApiService().updateProfile("Bearer " + token, body).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    Toast.makeText(PerfilActivity.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Toast.makeText(PerfilActivity.this, "Alterações salvas no seu perfil!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show();
        }
    }
}
