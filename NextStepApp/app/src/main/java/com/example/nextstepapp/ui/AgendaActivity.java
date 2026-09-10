package com.example.nextstepapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.Atividade;

import java.util.Locale;

public class AgendaActivity extends AppCompatActivity {

    private Atividade evento1, evento2, evento3;
    private TextView tvInfoAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Seus 3 eventos cadastrados
        evento1 = new Atividade("Boas-vindas e Introdução", "15/09/2026", "09:00 - 11:00", "Auditório Online (Zoom)");
        evento2 = new Atividade("Oficina de Currículo", "22/09/2026", "14:00 - 16:00", "Sala de Informática 2");
        evento3 = new Atividade("Mentoria Profissional", "29/09/2026", "10:00 - 12:00", "Laboratório Central");

        // Componentes do seu XML
        tvInfoAgenda = findViewById(R.id.tvInfoAgenda);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Button btnVoltarAgenda = findViewById(R.id.btnVoltarAgenda);

        // Ao clicar em uma data do Calendário
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Formata a data clicada no formato DD/MM/YYYY
            String dataSelecionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);

            if (dataSelecionada.equals("15/09/2026")) {
                tvInfoAgenda.setText(evento1.getInfo());
            } else if (dataSelecionada.equals("22/09/2026")) {
                tvInfoAgenda.setText(evento2.getInfo());
            } else if (dataSelecionada.equals("29/09/2026")) {
                tvInfoAgenda.setText(evento3.getInfo());
            } else {
                tvInfoAgenda.setText("Nenhum curso ou evento agendado para o dia " + dataSelecionada);
            }
        });

        btnVoltarAgenda.setOnClickListener(v -> finish());
    }
}