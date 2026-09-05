package com.example.nextstepapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.Atividade;

public class AgendaActivity extends AppCompatActivity {

    private Atividade evento1, evento2, evento3;
    private TextView tvInfoAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);


        evento1 = new Atividade("Boas-vindas e Introdução", "15/09/2026", "09:00 - 11:00", "Auditório Online (Zoom)");
        evento2 = new Atividade("Oficina de Currículo", "22/09/2026", "14:00 - 16:00", "Sala de Informática 2");
        evento3 = new Atividade("Mentoria Profissional", "29/09/2026", "10:00 - 12:00", "Laboratório Central");

        tvInfoAgenda = findViewById(R.id.tvInfoAgenda);

        Button btnEvento1 = findViewById(R.id.btnEvento1);
        Button btnEvento2 = findViewById(R.id.btnEvento2);
        Button btnEvento3 = findViewById(R.id.btnEvento3);
        Button btnVoltarAgenda = findViewById(R.id.btnVoltarAgenda);

        btnEvento1.setOnClickListener(v -> tvInfoAgenda.setText(evento1.getInfo()));
        btnEvento2.setOnClickListener(v -> tvInfoAgenda.setText(evento2.getInfo()));
        btnEvento3.setOnClickListener(v -> tvInfoAgenda.setText(evento3.getInfo()));

        btnVoltarAgenda.setOnClickListener(v -> finish());
    }
}
