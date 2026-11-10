package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
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

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        if (tvGreeting != null) {
            String nome = sessionManager.obterNomeUsuario();
            if (nome != null && !nome.trim().isEmpty()) {
                tvGreeting.setText("Olá, " + nome + "!");
            }
            tvGreeting.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        }

        View cvEventAvatar = findViewById(R.id.cvEventAvatar);
        if (cvEventAvatar != null) {
            cvEventAvatar.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        }

        // Botão Ver Detalhes do Próximo Evento (Introdução à Programação Mobile - curso-mobile-1)
        View btnVerDetalhes = findViewById(R.id.btnVerDetalhes);
        if (btnVerDetalhes != null) {
            btnVerDetalhes.setOnClickListener(v -> {
                Intent intent = new Intent(this, DetalhesEventoActivity.class);
                intent.putExtra("course_id", "curso-mobile-1");
                intent.putExtra("title", "Introdução à Programação Mobile");
                intent.putExtra("description", "Aprenda os conceitos fundamentais de arquitetura mobile e desenvolva seu primeiro aplicativo Android com Java e APIs REST.");
                intent.putExtra("location", "USP - Campus Butantã (Laboratório de Informática 3)");
                intent.putExtra("course_date", "2026-10-15T10:00:00.000Z");
                intent.putExtra("course_time_end", "2026-10-15T13:00:00.000Z");
                startActivity(intent);
            });
        }

        // Configuração dos botões do grid principal
        findViewById(R.id.cardAgenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
        });

        findViewById(R.id.cardCursos).setOnClickListener(v -> {
            startActivity(new Intent(this, CursosActivity.class));
        });

        View cardCertificados = findViewById(R.id.cardCertificados);
        if (cardCertificados != null) {
            cardCertificados.setOnClickListener(v -> {
                startActivity(new Intent(this, CertificadosActivity.class));
            });
        }

        View cardNotificacoes = findViewById(R.id.cardNotificacoesGrid);
        if (cardNotificacoes != null) {
            cardNotificacoes.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
            });
        }

        // Botão Dúvidas Frequentes (IA Chat)
        View btnFAQ = findViewById(R.id.btnFAQ);
        if (btnFAQ != null) {
            btnFAQ.setOnClickListener(v -> startActivity(new Intent(this, ChatIaActivity.class)));
        }

        // Configuração da Bottom Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            if (scrollViewHome != null) {
                scrollViewHome.smoothScrollTo(0, 0);
            }
        });

        findViewById(R.id.nav_agenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
        });

        findViewById(R.id.nav_presencas).setOnClickListener(v -> {
            startActivity(new Intent(this, PresencaActivity.class));
        });

        findViewById(R.id.nav_notificacoes).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificacoesActivity.class));
        });
    }
}
