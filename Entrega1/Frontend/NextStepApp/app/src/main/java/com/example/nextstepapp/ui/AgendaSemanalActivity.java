package com.example.nextstepapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nextstepapp.R;

public class AgendaSemanalActivity extends AppCompatActivity {

    private TextView tvSelectedDateFull;
    private View cardEvento1, cardEvento2, cardEvento3;
    private TextView tvHora1, tvTitulo1, tvLocal1;
    private TextView tvHora2, tvTitulo2, tvLocal2;
    private TextView tvHora3, tvTitulo3, tvLocal3;
    private TextView tvEmptyEventsSemanal;

    private LinearLayout daySeg20, dayTer21, dayQua22, dayQui23, daySex24, daySab25, dayDom26;
    private TextView tvLabelSeg20, tvNumSeg20, tvLabelTer21, tvNumTer21, tvLabelQua22, tvNumQua22;
    private TextView tvLabelQui23, tvNumQui23, tvLabelSex24, tvNumSex24, tvLabelSab25, tvNumSab25;
    private TextView tvLabelDom26, tvNumDom26;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_semanal);

        // Inicializar Views
        tvSelectedDateFull = findViewById(R.id.tvSelectedDateFull);
        cardEvento1 = findViewById(R.id.cardEvento1);
        cardEvento2 = findViewById(R.id.cardEvento2);
        cardEvento3 = findViewById(R.id.cardEvento3);
        tvEmptyEventsSemanal = findViewById(R.id.tvEmptyEventsSemanal);

        tvHora1 = findViewById(R.id.tvHora1);
        tvTitulo1 = findViewById(R.id.tvTitulo1);
        tvLocal1 = findViewById(R.id.tvLocal1);

        tvHora2 = findViewById(R.id.tvHora2);
        tvTitulo2 = findViewById(R.id.tvTitulo2);
        tvLocal2 = findViewById(R.id.tvLocal2);

        tvHora3 = findViewById(R.id.tvHora3);
        tvTitulo3 = findViewById(R.id.tvTitulo3);
        tvLocal3 = findViewById(R.id.tvLocal3);

        // Referências dos Dias do Seletor
        daySeg20 = findViewById(R.id.daySeg20);
        dayTer21 = findViewById(R.id.dayTer21);
        dayQua22 = findViewById(R.id.dayQua22);
        dayQui23 = findViewById(R.id.dayQui23);
        daySex24 = findViewById(R.id.daySex24);
        daySab25 = findViewById(R.id.daySab25);
        dayDom26 = findViewById(R.id.dayDom26);

        tvLabelSeg20 = findViewById(R.id.tvLabelSeg20);
        tvNumSeg20 = findViewById(R.id.tvNumSeg20);
        tvLabelTer21 = findViewById(R.id.tvLabelTer21);
        tvNumTer21 = findViewById(R.id.tvNumTer21);
        tvLabelQua22 = findViewById(R.id.tvLabelQua22);
        tvNumQua22 = findViewById(R.id.tvNumQua22);
        tvLabelQui23 = findViewById(R.id.tvLabelQui23);
        tvNumQui23 = findViewById(R.id.tvNumQui23);
        tvLabelSex24 = findViewById(R.id.tvLabelSex24);
        tvNumSex24 = findViewById(R.id.tvNumSex24);
        tvLabelSab25 = findViewById(R.id.tvLabelSab25);
        tvNumSab25 = findViewById(R.id.tvNumSab25);
        tvLabelDom26 = findViewById(R.id.tvLabelDom26);
        tvNumDom26 = findViewById(R.id.tvNumDom26);

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

        // Configurar Cliques nos Dias do Seletor
        setupDayClickListeners();

        // Cards de Eventos (Levam para Detalhes do Curso)
        if (cardEvento1 != null) {
            cardEvento1.setOnClickListener(v -> startActivity(new Intent(this, DetalhesEventoActivity.class)));
        }

        if (cardEvento2 != null) {
            cardEvento2.setOnClickListener(v -> startActivity(new Intent(this, DetalhesEventoActivity.class)));
        }

        if (cardEvento3 != null) {
            cardEvento3.setOnClickListener(v -> startActivity(new Intent(this, DetalhesEventoActivity.class)));
        }

        // Configuração da Bottom Navigation
        setupBottomNavigation();
    }

    private void setupDayClickListeners() {
        if (daySeg20 != null) daySeg20.setOnClickListener(v -> selecionarDia(20, "Segunda-feira, 20 de Abril de 2026"));
        if (dayTer21 != null) dayTer21.setOnClickListener(v -> selecionarDia(21, "Terça-feira, 21 de Abril de 2026"));
        if (dayQua22 != null) dayQua22.setOnClickListener(v -> selecionarDia(22, "Quarta-feira, 22 de Abril de 2026"));
        if (dayQui23 != null) dayQui23.setOnClickListener(v -> selecionarDia(23, "Quinta-feira, 23 de Abril de 2026"));
        if (daySex24 != null) daySex24.setOnClickListener(v -> selecionarDia(24, "Sexta-feira, 24 de Abril de 2026"));
        if (daySab25 != null) daySab25.setOnClickListener(v -> selecionarDia(25, "Sábado, 25 de Abril de 2026"));
        if (dayDom26 != null) dayDom26.setOnClickListener(v -> selecionarDia(26, "Domingo, 26 de Abril de 2026"));
    }

    private void selecionarDia(int dia, String dataExtenso) {
        tvSelectedDateFull.setText(dataExtenso);
        resetAllDayBackgrounds();

        // Destacar o dia clicado
        switch (dia) {
            case 20:
                destacarDia(daySeg20, tvLabelSeg20, tvNumSeg20);
                break;
            case 21:
                destacarDia(dayTer21, tvLabelTer21, tvNumTer21);
                break;
            case 22:
                destacarDia(dayQua22, tvLabelQua22, tvNumQua22);
                break;
            case 23:
                destacarDia(dayQui23, tvLabelQui23, tvNumQui23);
                break;
            case 24:
                destacarDia(daySex24, tvLabelSex24, tvNumSex24);
                break;
            case 25:
                destacarDia(daySab25, tvLabelSab25, tvNumSab25);
                break;
            case 26:
                destacarDia(dayDom26, tvLabelDom26, tvNumDom26);
                break;
        }

        // Exibir eventos correspondentes ao dia
        atualizarEventosPorDia(dia);
    }

    private void resetAllDayBackgrounds() {
        LinearLayout[] days = {daySeg20, dayTer21, dayQua22, dayQui23, daySex24, daySab25, dayDom26};
        TextView[] labels = {tvLabelSeg20, tvLabelTer21, tvLabelQua22, tvLabelQui23, tvLabelSex24, tvLabelSab25, tvLabelDom26};
        TextView[] nums = {tvNumSeg20, tvNumTer21, tvNumQua22, tvNumQui23, tvNumSex24, tvNumSab25, tvNumDom26};

        for (int i = 0; i < days.length; i++) {
            if (days[i] != null) {
                days[i].setBackground(null);
                if (labels[i] != null) labels[i].setTextColor(Color.parseColor("#9E9E9E"));
                if (nums[i] != null) nums[i].setTextColor(Color.parseColor("#333333"));
            }
        }
    }

    private void destacarDia(LinearLayout dayContainer, TextView tvLabel, TextView tvNum) {
        if (dayContainer != null) {
            dayContainer.setBackgroundResource(R.drawable.bg_day_selected);
            if (tvLabel != null) tvLabel.setTextColor(Color.WHITE);
            if (tvNum != null) tvNum.setTextColor(Color.WHITE);
        }
    }

    private void atualizarEventosPorDia(int dia) {
        if (dia == 25) {
            // Exibir 3 eventos do sábado
            cardEvento1.setVisibility(View.VISIBLE);
            cardEvento2.setVisibility(View.VISIBLE);
            cardEvento3.setVisibility(View.VISIBLE);
            if (tvEmptyEventsSemanal != null) tvEmptyEventsSemanal.setVisibility(View.GONE);

            tvHora1.setText("10:00 — 11:00");
            tvTitulo1.setText("Introdução à Programação Mobile");
            tvLocal1.setText("USP – Butantã");

            tvHora2.setText("14:00 — 16:00");
            tvTitulo2.setText("Inovação e Empreendedorismo");
            tvLocal2.setText("USP – Butantã");

            tvHora3.setText("18:00 — 19:00");
            tvTitulo3.setText("Habilidades Profissionais");
            tvLocal3.setText("USP – Butantã");

        } else if (dia == 24) {
            // Exibir evento da sexta-feira
            cardEvento1.setVisibility(View.VISIBLE);
            cardEvento2.setVisibility(View.VISIBLE);
            cardEvento3.setVisibility(View.GONE);
            if (tvEmptyEventsSemanal != null) tvEmptyEventsSemanal.setVisibility(View.GONE);

            tvHora1.setText("18:00 — 19:00");
            tvTitulo1.setText("Habilidades Profissionais");
            tvLocal1.setText("USP – Butantã");

            tvHora2.setText("19:30 — 21:00");
            tvTitulo2.setText("Networking & Mentoria");
            tvLocal2.setText("USP – Butantã");

        } else if (dia == 21) {
            // Exibir evento da terça-feira
            cardEvento1.setVisibility(View.VISIBLE);
            cardEvento2.setVisibility(View.GONE);
            cardEvento3.setVisibility(View.GONE);
            if (tvEmptyEventsSemanal != null) tvEmptyEventsSemanal.setVisibility(View.GONE);

            tvHora1.setText("10:00 — 14:00");
            tvTitulo1.setText("Visita Técnica - IFBP");
            tvLocal1.setText("USP – Butantã");

        } else {
            // Outros dias sem evento
            cardEvento1.setVisibility(View.GONE);
            cardEvento2.setVisibility(View.GONE);
            cardEvento3.setVisibility(View.GONE);
            if (tvEmptyEventsSemanal != null) tvEmptyEventsSemanal.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigation() {
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
