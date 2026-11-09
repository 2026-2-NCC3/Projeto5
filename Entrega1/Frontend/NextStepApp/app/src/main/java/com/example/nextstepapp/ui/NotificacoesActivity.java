package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class NotificacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                // Já está nas notificações
            });
        }

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, PerfilActivity.class));
            });
        }

        View cardNotif1 = findViewById(R.id.cardNotif1);
        if (cardNotif1 != null) {
            cardNotif1.setOnClickListener(v -> startActivity(new Intent(this, DetalhesEventoActivity.class)));
        }

        View cardNotif2 = findViewById(R.id.cardNotif2);
        if (cardNotif2 != null) {
            cardNotif2.setOnClickListener(v -> startActivity(new Intent(this, PresencaActivity.class)));
        }

        View cardNotif3 = findViewById(R.id.cardNotif3);
        if (cardNotif3 != null) {
            cardNotif3.setOnClickListener(v -> startActivity(new Intent(this, ChatIAActivity.class)));
        }

        // Bottom Navigation
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
            navAgenda.setOnClickListener(v -> startActivity(new Intent(this, AgendaActivity.class)));
        }

        View navPresencas = findViewById(R.id.nav_presencas);
        if (navPresencas != null) {
            navPresencas.setOnClickListener(v -> startActivity(new Intent(this, PresencaActivity.class)));
        }

        View navNotificacoes = findViewById(R.id.nav_notificacoes);
        if (navNotificacoes != null) {
            navNotificacoes.setOnClickListener(v -> {
                // Já está na tela de notificações
            });
        }
    }
}
