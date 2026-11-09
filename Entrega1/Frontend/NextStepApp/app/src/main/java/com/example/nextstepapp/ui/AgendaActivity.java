package com.example.nextstepapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.nextstepapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AgendaActivity extends AppCompatActivity {

    private TextView tvMonthYear;
    private GridLayout gridCalendarDays;
    private TextView tvDateSelectedSubtitle;
    private View cardEventoDia1;
    private View cardEventoDia2;
    private TextView tvEmptyEvents;
    private TextView tvHoraDia1, tvTituloDia1, tvHoraDia2, tvTituloDia2;

    private Calendar calendarAtual;
    private int diaSelecionado = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        tvMonthYear = findViewById(R.id.tvMonthYear);
        gridCalendarDays = findViewById(R.id.gridCalendarDays);
        tvDateSelectedSubtitle = findViewById(R.id.tvDateSelectedSubtitle);
        cardEventoDia1 = findViewById(R.id.cardEventoDia1);
        cardEventoDia2 = findViewById(R.id.cardEventoDia2);
        tvEmptyEvents = findViewById(R.id.tvEmptyEvents);
        tvHoraDia1 = findViewById(R.id.tvHoraDia1);
        tvTituloDia1 = findViewById(R.id.tvTituloDia1);
        tvHoraDia2 = findViewById(R.id.tvHoraDia2);
        tvTituloDia2 = findViewById(R.id.tvTituloDia2);

        // Inicializar Calendário no mês de Abril de 2026
        calendarAtual = Calendar.getInstance();
        calendarAtual.set(Calendar.YEAR, 2026);
        calendarAtual.set(Calendar.MONTH, Calendar.APRIL);
        calendarAtual.set(Calendar.DAY_OF_MONTH, 1);

        // Botão Voltar
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

        // Navegação de Mês (< e >)
        View btnPrevMonth = findViewById(R.id.btnPrevMonth);
        if (btnPrevMonth != null) {
            btnPrevMonth.setOnClickListener(v -> {
                calendarAtual.add(Calendar.MONTH, -1);
                diaSelecionado = 1;
                atualizarCalendario();
            });
        }

        View btnNextMonth = findViewById(R.id.btnNextMonth);
        if (btnNextMonth != null) {
            btnNextMonth.setOnClickListener(v -> {
                calendarAtual.add(Calendar.MONTH, 1);
                diaSelecionado = 1;
                atualizarCalendario();
            });
        }

        // Clicar nos cards de eventos do dia vai para Detalhes do Evento
        if (cardEventoDia1 != null) {
            cardEventoDia1.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        if (cardEventoDia2 != null) {
            cardEventoDia2.setOnClickListener(v -> {
                startActivity(new Intent(this, DetalhesEventoActivity.class));
            });
        }

        // Botão "Ver Agenda dos Eventos"
        View btnAbrirAgendaSemanal = findViewById(R.id.btnAbrirAgendaSemanal);
        if (btnAbrirAgendaSemanal != null) {
            btnAbrirAgendaSemanal.setOnClickListener(v -> {
                startActivity(new Intent(this, AgendaSemanalActivity.class));
            });
        }

        // Bottom Navigation
        setupBottomNavigation();

        // Renderizar Calendário Inicial
        atualizarCalendario();
    }

    private void atualizarCalendario() {
        // Atualizar título do Mês (ex: Abril de 2026)
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("pt", "BR"));
        String mesAno = sdf.format(calendarAtual.getTime());
        // Capitalizar primeira letra do mês
        mesAno = mesAno.substring(0, 1).toUpperCase() + mesAno.substring(1);
        tvMonthYear.setText(mesAno);

        // Limpar a grade de dias
        gridCalendarDays.removeAllViews();

        Calendar cal = (Calendar) calendarAtual.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // Obter dia da semana do 1º dia do mês (Domingo = 1, Segunda = 2, ...)
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0 para Dom, 1 para Seg...
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Obter número de dias do mês anterior para preenchimento
        Calendar prevCal = (Calendar) cal.clone();
        prevCal.add(Calendar.MONTH, -1);
        int prevMonthDays = prevCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        int totalCells = firstDayOfWeek + daysInMonth;
        int rows = (int) Math.ceil((double) totalCells / 7.0);
        int totalCellsWithPadding = rows * 7;

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int horizontalInsetPx = (int) (72 * getResources().getDisplayMetrics().density);
        int cellWidth = (screenWidth - horizontalInsetPx) / 7;
        int cellHeight = (int) (44 * getResources().getDisplayMetrics().density);

        int cellIndex = 0;

        // Preencher células do mês anterior
        for (int i = 0; i < firstDayOfWeek; i++) {
            int dayNum = prevMonthDays - firstDayOfWeek + 1 + i;
            TextView tv = criarTextViewDia(String.valueOf(dayNum));
            int row = cellIndex / 7;
            int col = cellIndex % 7;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row),
                    GridLayout.spec(col)
            );
            params.width = cellWidth;
            params.height = cellHeight;
            gridCalendarDays.addView(tv, params);
            cellIndex++;
        }

        // Preencher dias do mês atual
        for (int day = 1; day <= daysInMonth; day++) {
            final int diaAtual = day;
            boolean isSelected = (day == diaSelecionado);
            boolean isMarked = (day == 24 || day == 25 || day == 21); // dias com eventos marcados

            View cellView = criarCellView(day, isSelected, isMarked, cellWidth, cellHeight);
            cellView.setOnClickListener(v -> {
                diaSelecionado = diaAtual;
                atualizarCalendario();
                atualizarEventosDoDia(diaAtual);
            });

            int row = cellIndex / 7;
            int col = cellIndex % 7;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row),
                    GridLayout.spec(col)
            );
            params.width = cellWidth;
            params.height = cellHeight;
            gridCalendarDays.addView(cellView, params);
            cellIndex++;
        }

        // Preencher células do próximo mês para completar a última linha
        int remainingCells = totalCellsWithPadding - cellIndex;
        for (int i = 1; i <= remainingCells; i++) {
            TextView tv = criarTextViewDia(String.valueOf(i));
            int row = cellIndex / 7;
            int col = cellIndex % 7;
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row),
                    GridLayout.spec(col)
            );
            params.width = cellWidth;
            params.height = cellHeight;
            gridCalendarDays.addView(tv, params);
            cellIndex++;
        }

        // Atualizar lista de eventos para o dia selecionado
        atualizarEventosDoDia(diaSelecionado);
    }

    private View criarCellView(int dia, boolean isSelected, boolean isMarked, int cellWidth, int cellHeight) {
        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(cellWidth, cellHeight);
        frameLayout.setLayoutParams(frameParams);

        TextView tv = new TextView(this);
        int size = (int) (36 * getResources().getDisplayMetrics().density);
        FrameLayout.LayoutParams tvParams = new FrameLayout.LayoutParams(size, size);
        tvParams.gravity = Gravity.CENTER;
        tv.setLayoutParams(tvParams);
        tv.setGravity(Gravity.CENTER);
        tv.setText(String.valueOf(dia));
        tv.setTextSize(14);

        if (isSelected) {
            tv.setBackgroundResource(R.drawable.bg_calendar_day_selected);
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(null, Typeface.BOLD);
        } else if (isMarked) {
            tv.setBackgroundResource(R.drawable.bg_calendar_day_marked);
            tv.setTextColor(Color.parseColor("#5C33E6"));
            tv.setTypeface(null, Typeface.BOLD);
        } else {
            tv.setTextColor(Color.parseColor("#333333"));
        }

        frameLayout.addView(tv);

        // Se for dia marcado com ponto (ex: dia 24)
        if (isMarked && !isSelected) {
            View dot = new View(this);
            FrameLayout.LayoutParams dotParams = new FrameLayout.LayoutParams(8, 8);
            dotParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            dotParams.bottomMargin = 4;
            dot.setLayoutParams(dotParams);
            dot.setBackgroundColor(Color.parseColor("#5C33E6"));
            frameLayout.addView(dot);
        }

        return frameLayout;
    }

    private TextView criarTextViewDia(String text) {
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        tv.setTextSize(14);
        tv.setTextColor(Color.parseColor("#CCCCCC"));
        return tv;
    }

    private void atualizarEventosDoDia(int dia) {
        // Atualizar subtítulo da data selecionada
        Calendar calSel = (Calendar) calendarAtual.clone();
        calSel.set(Calendar.DAY_OF_MONTH, dia);

        SimpleDateFormat sdfFull = new SimpleDateFormat("EEEE, d 'de' MMMM", new Locale("pt", "BR"));
        String dataFormatada = sdfFull.format(calSel.getTime());
        dataFormatada = dataFormatada.substring(0, 1).toUpperCase() + dataFormatada.substring(1);
        tvDateSelectedSubtitle.setText(dataFormatada);

        // Atualizar eventos conforme o dia
        if (dia == 25) {
            cardEventoDia1.setVisibility(View.VISIBLE);
            cardEventoDia2.setVisibility(View.VISIBLE);
            tvEmptyEvents.setVisibility(View.GONE);

            tvHoraDia1.setText("10:00 — 11:00");
            tvTituloDia1.setText("Introdução à Programação Mobile");

            tvHoraDia2.setText("14:00 — 16:00");
            tvTituloDia2.setText("Inovação e Empreendedorismo");

        } else if (dia == 24) {
            cardEventoDia1.setVisibility(View.VISIBLE);
            cardEventoDia2.setVisibility(View.GONE);
            tvEmptyEvents.setVisibility(View.GONE);

            tvHoraDia1.setText("18:00 — 19:00");
            tvTituloDia1.setText("Habilidades Profissionais");

        } else if (dia == 21) {
            cardEventoDia1.setVisibility(View.VISIBLE);
            cardEventoDia2.setVisibility(View.GONE);
            tvEmptyEvents.setVisibility(View.GONE);

            tvHoraDia1.setText("10:00 — 14:00");
            tvTituloDia1.setText("Visita Técnica - IFBP");

        } else {
            // Dias sem evento
            cardEventoDia1.setVisibility(View.GONE);
            cardEventoDia2.setVisibility(View.GONE);
            tvEmptyEvents.setVisibility(View.VISIBLE);
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
