package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class DetalhesEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);

        // Botão Voltar (Barra Superior)
        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
            });
        }

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                startActivity(new Intent(this, PerfilActivity.class));
            });
        }

        // Botão Inscrever-se
        View btnInscrever = findViewById(R.id.btnInscrever);
        if (btnInscrever != null) {
            btnInscrever.setOnClickListener(v -> {
                Toast.makeText(this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
            });
        }

        // Seções de Detalhes do Curso (Cards clicáveis)
        setupCardClickListener(R.id.cardDescricao, "Exibindo Descrição do Curso");
        setupCardClickListener(R.id.cardConteudo, "Exibindo Conteúdo Programático");
        setupCardClickListener(R.id.cardPublico, "Exibindo Público-alvo");
        setupCardClickListener(R.id.cardPontos, "Exibindo Pontos do Curso");
        setupCardClickListener(R.id.cardCertificado, "Exibindo Informações de Certificado");

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
            navAgenda.setOnClickListener(v -> {
                Intent intent = new Intent(this, AgendaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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

    private void setupCardClickListener(int cardId, String message) {
        View card = findViewById(cardId);
        if (card != null) {
            card.setOnClickListener(v -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
        }
    }
}
