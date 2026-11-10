package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class AgendaSemanalActivity extends AppCompatActivity {

    private TextView tvDataSelecionada, tvTituloEventoSemanal, tvHorarioEventoSemanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_semanal);

        tvDataSelecionada = findViewById(R.id.tvDataSelecionada);
        tvTituloEventoSemanal = findViewById(R.id.tvTituloEventoSemanal);
        tvHorarioEventoSemanal = findViewById(R.id.tvHorarioEventoSemanal);
        CalendarView calendarWeekly = findViewById(R.id.calendarWeekly);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
        }

        if (calendarWeekly != null) {
            calendarWeekly.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                String dataStr = dayOfMonth + "/" + (month + 1) + "/" + year;
                if (tvDataSelecionada != null) {
                    tvDataSelecionada.setText("Eventos para " + dataStr + ":");
                }

                if (dayOfMonth % 2 == 0) {
                    tvTituloEventoSemanal.setText("Análise e Ciência de Dados com Python");
                    tvHorarioEventoSemanal.setText("Horário: 14:00 — 17:00\nLocal: FECAP - Sala Multimídia B");
                } else {
                    tvTituloEventoSemanal.setText("Introdução à Programação Mobile");
                    tvHorarioEventoSemanal.setText("Horário: 10:00 — 13:00\nLocal: USP - Campus Butantã");
                }
            });
        }

        View cardEvento = findViewById(R.id.cardEventoSemanal);
        if (cardEvento != null) {
            cardEvento.setOnClickListener(v -> {
                Intent intent = new Intent(this, DetalhesEventoActivity.class);
                intent.putExtra("course_id", "curso-mobile-1");
                intent.putExtra("title", tvTituloEventoSemanal.getText().toString());
                startActivity(intent);
            });
        }

        // Bottom Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
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
}
