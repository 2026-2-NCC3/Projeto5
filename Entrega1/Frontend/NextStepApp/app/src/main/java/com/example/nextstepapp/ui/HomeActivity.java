package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.session.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private ScrollView scrollViewHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        scrollViewHome = findViewById(R.id.scrollViewHome);

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, PerfilActivity.class));
            });
        }

        // Card do Grid: Agenda
        View cardAgenda = findViewById(R.id.cardAgenda);
        if (cardAgenda != null) {
            cardAgenda.setOnClickListener(v -> {
                startActivity(new Intent(this, AgendaActivity.class));
            });
        }

        // Card do Grid: Cursos
        View cardCursos = findViewById(R.id.cardCursos);
        if (cardCursos != null) {
            cardCursos.setOnClickListener(v -> {
                startActivity(new Intent(this, CursosActivity.class));
            });
        }

        // Card do Grid: Certificados
        View cardCertificados = findViewById(R.id.cardCertificados);
        if (cardCertificados != null) {
            cardCertificados.setOnClickListener(v -> {
                startActivity(new Intent(this, CursosActivity.class));
            });
        }

        // Card do Grid: Notificações
        View cardNotificacoesGrid = findViewById(R.id.cardNotificacoesGrid);
        if (cardNotificacoesGrid != null) {
            cardNotificacoesGrid.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
            });
        }

        // Card do Próximo Evento (Inteiro e Botão Ver Detalhes)
        View cardEvent = findViewById(R.id.cardEvent);
        if (cardEvent != null) {
            cardEvent.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        View btnVerDetalhes = findViewById(R.id.btnVerDetalhes);
        if (btnVerDetalhes != null) {
            btnVerDetalhes.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        // Card e Botão Dúvidas Frequentes (Abre o Chat com IA)
        View cardFAQ = findViewById(R.id.cardFAQ);
        if (cardFAQ != null) {
            cardFAQ.setOnClickListener(v -> {
                startActivity(new Intent(this, ChatIAActivity.class));
            });
        }

        View btnFAQ = findViewById(R.id.btnFAQ);
        if (btnFAQ != null) {
            btnFAQ.setOnClickListener(v -> {
                startActivity(new Intent(this, ChatIAActivity.class));
            });
        }

        // Bottom Navigation
        View navInicio = findViewById(R.id.nav_inicio);
        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                if (scrollViewHome != null) {
                    scrollViewHome.smoothScrollTo(0, 0);
                }
            });
        }

        View navAgenda = findViewById(R.id.nav_agenda);
        if (navAgenda != null) {
            navAgenda.setOnClickListener(v -> {
                startActivity(new Intent(this, AgendaActivity.class));
            });
        }

        View navPresencas = findViewById(R.id.nav_presencas);
        if (navPresencas != null) {
            navPresencas.setOnClickListener(v -> {
                startActivity(new Intent(this, PresencaActivity.class));
            });
        }

        View navNotificacoes = findViewById(R.id.nav_notificacoes);
        if (navNotificacoes != null) {
            navNotificacoes.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
            });
        }
    }
}
