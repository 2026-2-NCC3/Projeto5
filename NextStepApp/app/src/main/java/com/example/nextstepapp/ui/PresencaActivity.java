package com.example.nextstepapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.R;

public class PresencaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenca);

        Button btnRegistrarPresenca = findViewById(R.id.btnRegistrarPresenca);
        Button btnVoltarPresenca = findViewById(R.id.btnVoltarPresenca);

        // Ação do botão para simular a validação de presença no servidor
        btnRegistrarPresenca.setOnClickListener(v -> {
            Toast.makeText(PresencaActivity.this, "Presença confirmada com sucesso!", Toast.LENGTH_LONG).show();
        });

        // Retorna à tela anterior
        btnVoltarPresenca.setOnClickListener(v -> finish());
    }
}