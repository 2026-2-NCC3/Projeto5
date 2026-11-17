package com.example.nextstepapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class OportunidadesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oportunidades);

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setClickable(true);
            btnBack.setFocusable(true);
            btnBack.setOnClickListener(v -> finish());
        }

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        }

        findViewById(R.id.btnEnem).setOnClickListener(v ->
                abrirLink("https://www.gov.br/inep/pt-br/areas-de-atuacao/avaliacao-e-exames-educacionais/enem"));

        findViewById(R.id.btnEtec).setOnClickListener(v ->
                abrirLink("https://www.vestibulinhoetec.com.br/"));

        findViewById(R.id.btnFatec).setOnClickListener(v ->
                abrirLink("https://www.vestibularfatec.com.br/"));

        findViewById(R.id.btnJovemAprendiz).setOnClickListener(v ->
                abrirLink("https://portal.ciee.org.br/"));

        // Bottom Navigation
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.nav_agenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
            finish();
        });

        findViewById(R.id.nav_presencas).setOnClickListener(v -> {
            startActivity(new Intent(this, PresencaActivity.class));
            finish();
        });

        findViewById(R.id.nav_notificacoes).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificacoesActivity.class));
            finish();
        });
    }

    private void abrirLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Não foi possível abrir o link no navegador.", Toast.LENGTH_SHORT).show();
        }
    }
}
