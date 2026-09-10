package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.Curso;

public class CursosActivity extends AppCompatActivity {

    private Curso curso1, curso2, curso3, curso4;
    private TextView textInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        curso1 = new Curso(1, "Introdução à Programação", 40);
        curso2 = new Curso(2, "Lógica de Programação", 30);
        curso3 = new Curso(3, "Banco de Dados I", 50);
        curso4 = new Curso(4, "Orientação Profissional", 20);

        textInfo = findViewById(R.id.textInfo);

        Button btnCurso1 = findViewById(R.id.btnCurso1);
        Button btnCurso2 = findViewById(R.id.btnCurso2);
        Button btnCurso3 = findViewById(R.id.btnCurso3);
        Button btnCurso4 = findViewById(R.id.btnCurso4);
        Button btnVoltar = findViewById(R.id.btnVoltar);
        Button btnAbrirAgenda = findViewById(R.id.btnAbrirAgenda);

        // 1. Mapeia o novo botão de presença
        Button btnAbrirPresenca = findViewById(R.id.btnAbrirPresenca);

        btnCurso1.setOnClickListener(v -> textInfo.setText(curso1.getInfo()));
        btnCurso2.setOnClickListener(v -> textInfo.setText(curso2.getInfo()));
        btnCurso3.setOnClickListener(v -> textInfo.setText(curso3.getInfo()));
        btnCurso4.setOnClickListener(v -> textInfo.setText(curso4.getInfo()));

        btnVoltar.setOnClickListener(v -> finish());

        btnAbrirAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(CursosActivity.this, AgendaActivity.class);
            startActivity(intent);
        });

        // 2. Evento de clique para abrir a tela de QR Code
        if (btnAbrirPresenca != null) {
            btnAbrirPresenca.setOnClickListener(v -> {
                Intent intent = new Intent(CursosActivity.this, PresencaActivity.class);
                startActivity(intent);
            });
        }
    }
}