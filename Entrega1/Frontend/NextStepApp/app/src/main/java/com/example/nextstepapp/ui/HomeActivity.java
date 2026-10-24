package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.MainActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.session.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        // Configuração dos botões do grid
        findViewById(R.id.cardAgenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
        });

        findViewById(R.id.cardCursos).setOnClickListener(v -> {
            startActivity(new Intent(this, CursosActivity.class));
        });

        // Configuração da Bottom Navigation
        findViewById(R.id.nav_agenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
        });

        findViewById(R.id.nav_presencas).setOnClickListener(v -> {
            startActivity(new Intent(this, PresencaActivity.class));
        });

        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            // Já estamos no Início, opcionalmente rolar para o topo
        });
    }
}