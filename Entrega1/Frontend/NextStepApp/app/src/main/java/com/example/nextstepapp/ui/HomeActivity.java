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

        // Botão Ver Detalhes do Próximo Evento
        View btnVerDetalhes = findViewById(R.id.btnVerDetalhes);
        if (btnVerDetalhes != null) {
            btnVerDetalhes.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        // Configuração dos botões do grid
        findViewById(R.id.cardAgenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
        });

        findViewById(R.id.cardCursos).setOnClickListener(v -> {
            startActivity(new Intent(this, CursosActivity.class));
        });

        // Botão Certificados (Exemplo de navegação, ajuste se houver a activity)
        View cardCertificados = findViewById(R.id.cardCertificados);
        if (cardCertificados != null) {
            cardCertificados.setOnClickListener(v -> {
                // startActivity(new Intent(this, CertificadosActivity.class));
            });
        }

        // Botão Notificações no Grid
        View cardNotificacoes = findViewById(R.id.cardNotificacoesGrid);
        if (cardNotificacoes != null) {
            cardNotificacoes.setOnClickListener(v -> {
                // startActivity(new Intent(this, NotificacoesActivity.class));
            });
        }

        // Configuração da Bottom Navigation
        findViewById(R.id.nav_agenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
        });

        findViewById(R.id.nav_presencas).setOnClickListener(v -> {
            startActivity(new Intent(this, PresencaActivity.class));
        });

        findViewById(R.id.nav_notificacoes).setOnClickListener(v -> {
            // startActivity(new Intent(this, NotificacoesActivity.class));
        });

        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            // Volta para o topo da tela inicial
            if (scrollViewHome != null) {
                scrollViewHome.smoothScrollTo(0, 0);
            }
        });
    }
}