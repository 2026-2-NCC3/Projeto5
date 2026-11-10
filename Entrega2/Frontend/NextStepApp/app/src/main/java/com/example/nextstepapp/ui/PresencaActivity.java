package com.example.nextstepapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresencaActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenca);

        sessionManager = new SessionManager(this);

        Button btnRegistrarPresenca = findViewById(R.id.btnRegistrarPresenca);
        Button btnVoltarPresenca = findViewById(R.id.btnVoltarPresenca);

        btnRegistrarPresenca.setOnClickListener(v -> registrarPresenca());
        btnVoltarPresenca.setOnClickListener(v -> finish());
    }

    private void registrarPresenca() {
        String token = sessionManager.obterToken();
        if (token == null || token.trim().isEmpty()) {
            Toast.makeText(this, "Faça login para registrar presença.", Toast.LENGTH_SHORT).show();
            return;
        }

        String courseId = getIntent().getStringExtra("course_id");
        if (courseId != null && !courseId.trim().isEmpty()) {
            enviarCheckIn(token, courseId);
            return;
        }

        // Se não houver course_id específico no Intent, busca o curso da agenda do aluno
        ApiClient.getApiService().getAgenda("Bearer " + token).enqueue(new Callback<ApiResponse<List<Curso>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Curso>>> call, Response<ApiResponse<List<Curso>>> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getData() != null
                        && !response.body().getData().isEmpty()) {
                    String id = response.body().getData().get(0).getId();
                    enviarCheckIn(token, id);
                } else {
                    Toast.makeText(PresencaActivity.this, "Nenhum curso na sua agenda encontrado para check-in.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                Toast.makeText(PresencaActivity.this, "Erro ao consultar agenda: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarCheckIn(String token, String courseId) {
        Map<String, String> body = new HashMap<>();
        body.put("course_id", courseId);

        ApiClient.getApiService().checkIn("Bearer " + token, body).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String msg = response.body().getMessage() != null ? response.body().getMessage() : "Presença registrada com sucesso!";
                    Toast.makeText(PresencaActivity.this, msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PresencaActivity.this, "Não foi possível registrar presença.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                Toast.makeText(PresencaActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
