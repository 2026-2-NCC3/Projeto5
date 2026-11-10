package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        sessionManager = new SessionManager(this);

        // Botão Voltar (Barra Superior)
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Cards de Eventos (Levam para Detalhes)
        View cardEvento1 = findViewById(R.id.cardEvento1);
        if (cardEvento1 != null) {
            cardEvento1.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        View cardEvento2 = findViewById(R.id.cardEvento2);
        if (cardEvento2 != null) {
            cardEvento2.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        View cardEvento3 = findViewById(R.id.cardEvento3);
        if (cardEvento3 != null) {
            cardEvento3.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        // Configuração da Bottom Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        carregarAgenda();
    }

    private void carregarAgenda() {
        String token = sessionManager.obterToken();
        if (token == null || token.trim().isEmpty()) {
            return;
        }

        ApiClient.getApiService().getAgenda("Bearer " + token).enqueue(new Callback<ApiResponse<List<Curso>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Curso>>> call, Response<ApiResponse<List<Curso>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Curso> cursos = response.body().getData();
                    if (!cursos.isEmpty()) {
                        configurarCard(R.id.cardEvento1, R.id.tvTitulo1, R.id.tvHora1, cursos.get(0));
                    }
                    if (cursos.size() > 1) {
                        configurarCard(R.id.cardEvento2, R.id.tvTitulo2, R.id.tvHora2, cursos.get(1));
                    }
                    if (cursos.size() > 2) {
                        configurarCard(R.id.cardEvento3, R.id.tvTitulo3, R.id.tvHora3, cursos.get(2));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                // Mantém visual padrão em caso de offline/falha
            }
        });
    }

    private void configurarCard(int cardId, int tituloId, int horaId, Curso curso) {
        View card = findViewById(cardId);
        TextView tvTitulo = findViewById(tituloId);
        TextView tvHora = findViewById(horaId);

        if (card != null && curso != null) {
            if (tvTitulo != null && curso.getTitle() != null) {
                tvTitulo.setText(curso.getTitle());
            }
            if (tvHora != null && curso.getCourseDate() != null) {
                tvHora.setText(formatarHorario(curso.getCourseDate(), curso.getCourseTimeEnd()));
            }
            card.setOnClickListener(v -> {
                Intent intent = new Intent(this, DetalhesEventoActivity.class);
                intent.putExtra("course_id", curso.getId());
                intent.putExtra("title", curso.getTitle());
                intent.putExtra("description", curso.getDescription());
                intent.putExtra("location", curso.getLocation());
                intent.putExtra("course_date", curso.getCourseDate());
                intent.putExtra("course_time_end", curso.getCourseTimeEnd());
                startActivity(intent);
            });
        }
    }

    private String formatarHorario(String inicio, String fim) {
        try {
            String horaInicio = extrairHora(inicio);
            String horaFim = extrairHora(fim);
            if (!horaInicio.isEmpty() && !horaFim.isEmpty()) {
                return horaInicio + " — " + horaFim;
            } else if (!horaInicio.isEmpty()) {
                return horaInicio;
            }
        } catch (Exception ignored) {
        }
        return "Horário a definir";
    }

    private String extrairHora(String isoDate) {
        if (isoDate == null || !isoDate.contains("T")) {
            return "";
        }
        String[] partes = isoDate.split("T");
        if (partes.length > 1) {
            String hora = partes[1].replace(".000Z", "").replace("Z", "");
            if (hora.length() >= 5) {
                return hora.substring(0, 5);
            }
        }
        return "";
    }
}
