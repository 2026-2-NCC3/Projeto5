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

import java.util.ArrayList;
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

        setupTopBarAndNavigation();
        carregarAgenda();
    }

    private void setupTopBarAndNavigation() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
        }

        // Configuração da Bottom Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        View navEvolucao = findViewById(R.id.nav_evolucao);
        if (navEvolucao != null) {
            navEvolucao.setOnClickListener(v -> {
                startActivity(new Intent(this, PresencaActivity.class));
                finish();
            });
        }

        View navMensagens = findViewById(R.id.nav_mensagens);
        if (navMensagens != null) {
            navMensagens.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
                finish();
            });
        }
    }

    private void carregarAgenda() {
        String token = sessionManager.obterToken();
        if (token == null || token.trim().isEmpty()) {
            carregarAgendaFallback();
            return;
        }

        ApiClient.getApiService().getAgenda("Bearer " + token).enqueue(new Callback<ApiResponse<List<Curso>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Curso>>> call, Response<ApiResponse<List<Curso>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    List<Curso> cursos = response.body().getData();
                    exibirCursosNaAgenda(cursos);
                } else {
                    carregarAgendaFallback();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                carregarAgendaFallback();
            }
        });
    }

    private void carregarAgendaFallback() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso(
                "curso-mobile-1",
                "Introdução à Programação Mobile",
                "Aprenda os conceitos fundamentais de arquitetura mobile e desenvolva seu primeiro aplicativo Android com Java e APIs REST.",
                "USP - Campus Butantã (Laboratório de Informática 3)",
                "2026-10-15T10:00:00.000Z",
                "2026-10-15T13:00:00.000Z",
                35, 28, 100, 1, "Tecnologia"
        ));

        cursos.add(new Curso(
                "curso-dados-2",
                "Análise e Ciência de Dados com Python",
                "Aprenda a analisar dados, criar gráficos e identificar padrões utilizando ferramentas modernas e casos reais de empresas.",
                "FECAP - Sala Multimídia B",
                "2026-10-22T14:00:00.000Z",
                "2026-10-22T17:00:00.000Z",
                30, 20, 80, 1, "Tecnologia"
        ));

        cursos.add(new Curso(
                "curso-softskills-3",
                "Habilidades Profissionais e Inteligência Emocional",
                "Desenvolva comunicação interpessoal, oratória, liderança e resolução colaborativa de problemas para se destacar no mercado.",
                "FECAP - Auditório Principal",
                "2026-10-29T09:00:00.000Z",
                "2026-10-29T12:00:00.000Z",
                40, 25, 60, 1, "Soft Skills"
        ));

        exibirCursosNaAgenda(cursos);
    }

    private void exibirCursosNaAgenda(List<Curso> cursos) {
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
                intent.putExtra("category", curso.getCategory());
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
        return "10:00 — 13:00";
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
