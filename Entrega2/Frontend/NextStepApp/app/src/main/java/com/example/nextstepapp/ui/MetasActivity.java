package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;
import com.google.android.material.card.MaterialCardView;

public class MetasActivity extends AppCompatActivity {

    private LinearLayout containerMetas;
    private EditText etNovaMeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

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

        containerMetas = findViewById(R.id.containerMetas);
        etNovaMeta = findViewById(R.id.etNovaMeta);

        setupCheckbox(findViewById(R.id.chkMeta1));
        setupCheckbox(findViewById(R.id.chkMeta2));
        setupCheckbox(findViewById(R.id.chkMeta3));
        setupCheckbox(findViewById(R.id.chkMeta4));

        findViewById(R.id.btnAdicionarMeta).setOnClickListener(v -> adicionarNovaMeta());

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

    private void setupCheckbox(CheckBox checkBox) {
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Toast.makeText(this, "Parabéns! Meta concluída 🎉", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void adicionarNovaMeta() {
        if (etNovaMeta == null) return;
        String textoMeta = etNovaMeta.getText().toString().trim();
        if (TextUtils.isEmpty(textoMeta)) {
            Toast.makeText(this, "Digite o texto da meta.", Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialCardView cardView = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.bottomMargin = 10;
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(12f);
        cardView.setCardElevation(2f);
        cardView.setStrokeWidth(1);
        cardView.setStrokeColor(0xFFF0F0F0);

        CheckBox checkBox = new CheckBox(this);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        checkBox.setPadding(16, 16, 16, 16);
        checkBox.setText(textoMeta);
        checkBox.setTextSize(14f);
        checkBox.setTextColor(getResources().getColor(android.R.color.black));

        setupCheckbox(checkBox);

        cardView.addView(checkBox);
        containerMetas.addView(cardView);

        etNovaMeta.setText("");
        Toast.makeText(this, "Nova meta adicionada com sucesso!", Toast.LENGTH_SHORT).show();
    }
}
