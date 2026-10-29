package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.google.android.material.button.MaterialButton;

public class AgendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Botão Voltar (Barra Superior)
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Botão Ver Todos os Cursos (Botão Roxo)
        MaterialButton btnVerTodosCursos = findViewById(R.id.btnVerTodosCursos);
        if (btnVerTodosCursos != null) {
            btnVerTodosCursos.setOnClickListener(v -> {
                startActivity(new Intent(this, CursosActivity.class));
            });
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

        // Configuração da Bottom Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            // Volta para a Home e limpa a pilha
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // Configuração do Calendário (Opcional: lógica de seleção de data)
        CalendarView calendarView = findViewById(R.id.calendarView);
        TextView tvDataSelecionada = findViewById(R.id.tvDataSelecionada);
        if (calendarView != null && tvDataSelecionada != null) {
            calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                String data = dayOfMonth + "/" + (month + 1) + "/" + year;
                // Apenas exemplo visual
                tvDataSelecionada.setText("Data selecionada: " + data);
            });
        }
    }
}