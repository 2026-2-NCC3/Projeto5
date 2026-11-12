package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class CertificadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificados);

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

        findViewById(R.id.btnDownloadCert1).setOnClickListener(v -> {
            Toast.makeText(this, "Abrindo certificado: Habilidades Profissionais.pdf", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnDownloadCert2).setOnClickListener(v -> {
            Toast.makeText(this, "Abrindo certificado: Programação Mobile.pdf", Toast.LENGTH_SHORT).show();
        });

        // Navegação Bottom Bar
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
}
