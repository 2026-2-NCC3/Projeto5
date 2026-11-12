package com.example.nextstepapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private TextView tvSelectedDateFull;
    private LinearLayout daySeg20, dayTer21, dayQua22, dayQui23, daySex24, daySab25, dayDom26;
    private LinearLayout[] allDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        sessionManager = new SessionManager(this);
        tvSelectedDateFull = findViewById(R.id.tvSelectedDateFull);

        setupDaysSelector();
        setupTopBarAndNavigation();
        carregarAgenda();
    }

    private void setupDaysSelector() {
        daySeg20 = findViewById(R.id.daySeg20);
        dayTer21 = findViewById(R.id.dayTer21);
        dayQua22 = findViewById(R.id.dayQua22);
        dayQui23 = findViewById(R.id.dayQui23);
        daySex24 = findViewById(R.id.daySex24);
        daySab25 = findViewById(R.id.daySab25);
        dayDom26 = findViewById(R.id.dayDom26);

        allDays = new LinearLayout[]{daySeg20, dayTer21, dayQua22, dayQui23, daySex24, daySab25, dayDom26};

        if (daySeg20 != null) daySeg20.setOnClickListener(v -> selecionarDia(daySeg20, "Segunda-feira, 20 de Outubro de 2026", 20));
        if (dayTer21 != null) dayTer21.setOnClickListener(v -> selecionarDia(dayTer21, "Terça-feira, 21 de Outubro de 2026", 21));
        if (dayQua22 != null) dayQua22.setOnClickListener(v -> selecionarDia(dayQua22, "Quarta-feira, 22 de Outubro de 2026", 22));
        if (dayQui23 != null) dayQui23.setOnClickListener(v -> selecionarDia(dayQui23, "Quinta-feira, 23 de Outubro de 2026", 23));
        if (daySex24 != null) daySex24.setOnClickListener(v -> selecionarDia(daySex24, "Sexta-feira, 24 de Outubro de 2026", 24));
        if (daySab25 != null) daySab25.setOnClickListener(v -> selecionarDia(daySab25, "Sábado, 25 de Outubro de 2026", 25));
        if (dayDom26 != null) dayDom26.setOnClickListener(v -> selecionarDia(dayDom26, "Domingo, 26 de Outubro de 2026", 26));
    }

    private void selecionarDia(LinearLayout selectedContainer, String dataExtenso, int dia) {
        for (LinearLayout day : allDays) {
            if (day != null) {
                day.setBackground(null);
                if (day.getChildCount() >= 2) {
                    TextView tvName = (TextView) day.getChildAt(0);
                    TextView tvNum = (TextView) day.getChildAt(1);
                    if (tvName != null) tvName.setTextColor(Color.parseColor("#BDBDBD"));
                    if (tvNum != null) tvNum.setTextColor(Color.parseColor("#555555"));
                }
            }
        }

        if (selectedContainer != null) {
            selectedContainer.setBackgroundResource(R.drawable.bg_day_selected);
            if (selectedContainer.getChildCount() >= 2) {
                TextView tvName = (TextView) selectedContainer.getChildAt(0);
                TextView tvNum = (TextView) selectedContainer.getChildAt(1);
                if (tvName != null) tvName.setTextColor(ContextCompat.getColor(this, R.color.white));
                if (tvNum != null) tvNum.setTextColor(ContextCompat.getColor(this, R.color.white));
            }
        }

        if (tvSelectedDateFull != null) {
            tvSelectedDateFull.setText(dataExtenso);
        }

        filtrarAgendaPorDia(dia);
    }

    private void filtrarAgendaPorDia(int dia) {
        List<Curso> cursosDoDia = new ArrayList<>();
        if (dia == 20 || dia == 25) {
            cursosDoDia.add(new Curso(
                    "curso-mobile-1", "Introdução à Programação Mobile",
                    "Aprenda a criar seu aplicativo Android com Java e REST.",
                    "USP – Butantã", "2026-10-15T10:00:00.000Z", "2026-10-15T13:00:00.000Z",
                    35, 28, 100, 1, "Tecnologia"
            ));
            cursosDoDia.add(new Curso(
                    "curso-dados-2", "Análise e Ciência de Dados com Python",
                    "Aprenda a analisar dados e gráficos com Python.",
                    "FECAP", "2026-10-22T14:00:00.000Z", "2026-10-22T17:00:00.000Z",
                    30, 20, 80, 1, "Tecnologia"
            ));
        } else if (dia == 21 || dia == 23) {
            cursosDoDia.add(new Curso(
                    "curso-softskills-3", "Habilidades Profissionais e Oratória",
                    "Desenvolva comunicação e inteligência emocional.",
                    "FECAP - Auditório", "2026-10-29T09:00:00.000Z", "2026-10-29T12:00:00.000Z",
                    40, 25, 60, 1, "Soft Skills"
            ));
        } else {
            cursosDoDia.add(new Curso(
                    "curso-empreendedorismo-4", "Inovação, Criatividade e Empreendedorismo",
                    "Aprenda como transformar ideias em modelos de negócios.",
                    "UNICAMP - Hub de Inovação", "2026-11-05T18:00:00.000Z", "2026-11-05T21:00:00.000Z",
                    25, 15, 90, 1, "Negócios"
            ));
        }

        exibirCursosNaAgenda(cursosDoDia);
    }

    private void setupTopBarAndNavigation() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setClickable(true);
            btnBack.setFocusable(true);
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
        }

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        }

        View btnCalendarioCompleto = findViewById(R.id.btnCalendarioCompleto);
        if (btnCalendarioCompleto != null) {
            btnCalendarioCompleto.setOnClickListener(v -> startActivity(new Intent(this, AgendaSemanalActivity.class)));
        }

        // Bottom Navigation
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
        filtrarAgendaPorDia(25);
    }

    private void exibirCursosNaAgenda(List<Curso> cursos) {
        View card1 = findViewById(R.id.cardEvento1);
        View card2 = findViewById(R.id.cardEvento2);
        View card3 = findViewById(R.id.cardEvento3);

        if (card1 != null) card1.setVisibility(View.GONE);
        if (card2 != null) card2.setVisibility(View.GONE);
        if (card3 != null) card3.setVisibility(View.GONE);

        if (!cursos.isEmpty()) {
            if (card1 != null) card1.setVisibility(View.VISIBLE);
            configurarCard(R.id.cardEvento1, R.id.tvTitulo1, R.id.tvHora1, R.id.tvLocal1, cursos.get(0));
        }
        if (cursos.size() > 1) {
            if (card2 != null) card2.setVisibility(View.VISIBLE);
            configurarCard(R.id.cardEvento2, R.id.tvTitulo2, R.id.tvHora2, R.id.tvLocal2, cursos.get(1));
        }
        if (cursos.size() > 2) {
            if (card3 != null) card3.setVisibility(View.VISIBLE);
            configurarCard(R.id.cardEvento3, R.id.tvTitulo3, R.id.tvHora3, R.id.tvLocal3, cursos.get(2));
        }
    }

    private void configurarCard(int cardId, int tituloId, int horaId, int localId, Curso curso) {
        View card = findViewById(cardId);
        TextView tvTitulo = findViewById(tituloId);
        TextView tvHora = findViewById(horaId);
        TextView tvLocal = findViewById(localId);

        if (card != null && curso != null) {
            if (tvTitulo != null && curso.getTitle() != null) {
                tvTitulo.setText(curso.getTitle());
            }
            if (tvHora != null && curso.getCourseDate() != null) {
                tvHora.setText(formatarHorario(curso.getCourseDate(), curso.getCourseTimeEnd()));
            }
            if (tvLocal != null && curso.getLocation() != null) {
                tvLocal.setText(curso.getLocation());
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
