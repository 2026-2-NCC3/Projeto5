package com.example.nextstepapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.MainActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.session.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosActivity extends AppCompatActivity {

    private ProgressBar progressCursos;
    private TextView textInfo;
    private LinearLayout containerCursos;
    private ScrollView scrollCursos;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        sessionManager = new SessionManager(this);

        progressCursos = findViewById(
                R.id.progressCursos
        );

        textInfo = findViewById(
                R.id.textInfo
        );

        containerCursos = findViewById(
                R.id.containerCursos
        );

        scrollCursos = findViewById(
                R.id.scrollCursos
        );

        Button btnAbrirAgenda = findViewById(
                R.id.btnAbrirAgenda
        );

        Button btnAbrirPresenca = findViewById(
                R.id.btnAbrirPresenca
        );

        Button btnSair = findViewById(
                R.id.btnSair
        );

        scrollCursos.setSmoothScrollingEnabled(true);

        btnAbrirAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CursosActivity.this,
                    AgendaActivity.class
            );

            startActivity(intent);
        });

        btnAbrirPresenca.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CursosActivity.this,
                    PresencaActivity.class
            );

            startActivity(intent);
        });

        btnSair.setOnClickListener(v -> {
            sessionManager.limparSessao();

            Intent intent = new Intent(
                    CursosActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });

        carregarCursos();
    }

    private void carregarCursos() {
        progressCursos.setVisibility(View.VISIBLE);

        textInfo.setText(
                "Carregando cursos..."
        );

        containerCursos.removeAllViews();

        ApiClient
                .getApiService()
                .getCursos()
                .enqueue(
                        new Callback<ApiResponse<List<Curso>>>() {

                            @Override
                            public void onResponse(
                                    Call<ApiResponse<List<Curso>>> call,
                                    Response<ApiResponse<List<Curso>>> response
                            ) {
                                progressCursos.setVisibility(
                                        View.GONE
                                );

                                ApiResponse<List<Curso>> body =
                                        response.body();

                                if (response.isSuccessful()
                                        && body != null
                                        && body.isSuccess()) {

                                    List<Curso> cursos =
                                            body.getData();

                                    int quantidade =
                                            cursos == null
                                                    ? 0
                                                    : cursos.size();

                                    textInfo.setText(
                                            quantidade
                                                    + " curso(s) disponível(is)"
                                    );

                                    exibirCursos(cursos);

                                    return;
                                }

                                textInfo.setText(
                                        "Não foi possível carregar os cursos."
                                );
                            }

                            @Override
                            public void onFailure(
                                    Call<ApiResponse<List<Curso>>> call,
                                    Throwable throwable
                            ) {
                                progressCursos.setVisibility(
                                        View.GONE
                                );

                                textInfo.setText(
                                        "Falha de conexão com o servidor."
                                );
                            }
                        }
                );
    }

    private void exibirCursos(List<Curso> cursos) {
        containerCursos.removeAllViews();

        if (cursos == null || cursos.isEmpty()) {
            TextView mensagem = criarTexto(
                    "Nenhum curso disponível.",
                    16,
                    false,
                    "#3F3745"
            );

            mensagem.setTextAlignment(
                    View.TEXT_ALIGNMENT_CENTER
            );

            containerCursos.addView(mensagem);

            return;
        }

        for (Curso curso : cursos) {
            LinearLayout cardCurso =
                    criarCardCurso();

            TextView titulo = criarTexto(
                    valorOuPadrao(
                            curso.getTitle(),
                            "Curso sem título"
                    ),
                    18,
                    true,
                    "#3F3745"
            );

            TextView descricao = criarTexto(
                    valorOuPadrao(
                            curso.getDescription(),
                            "Sem descrição."
                    ),
                    14,
                    false,
                    "#3F3745"
            );

            TextView data = criarTexto(
                    "Início: "
                            + formatarDataHora(
                            curso.getCourseDate()
                    ),
                    14,
                    true,
                    "#3F3745"
            );

            TextView local = criarTexto(
                    "Local: "
                            + valorOuPadrao(
                            curso.getLocation(),
                            "Não informado"
                    ),
                    14,
                    false,
                    "#3F3745"
            );

            TextView vagas = criarTexto(
                    "Vagas disponíveis: "
                            + curso.getAvailableSpots(),
                    14,
                    false,
                    "#6741C1"
            );

            TextView pontos = criarTexto(
                    "Pontos: "
                            + curso.getPointsAwarded(),
                    14,
                    false,
                    "#6741C1"
            );

            cardCurso.addView(titulo);
            cardCurso.addView(descricao);
            cardCurso.addView(data);
            cardCurso.addView(local);
            cardCurso.addView(vagas);
            cardCurso.addView(pontos);

            // Esta linha é essencial.
            // Ela adiciona cada card à tela.
            containerCursos.addView(cardCurso);
        }

        containerCursos.requestLayout();
        containerCursos.invalidate();

        scrollCursos.post(() -> {
            scrollCursos.requestLayout();
            scrollCursos.invalidate();

            // Mantém a tela inicialmente no topo.
            scrollCursos.fullScroll(
                    View.FOCUS_UP
            );
        });
    }

    private LinearLayout criarCardCurso() {
        LinearLayout cardCurso =
                new LinearLayout(this);

        cardCurso.setOrientation(
                LinearLayout.VERTICAL
        );

        cardCurso.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        GradientDrawable fundo =
                new GradientDrawable();

        fundo.setColor(
                Color.parseColor("#F2E9FA")
        );

        fundo.setCornerRadius(
                dp(16)
        );

        fundo.setStroke(
                dp(1),
                Color.parseColor("#D8C9EA")
        );

        cardCurso.setBackground(fundo);

        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametros.setMargins(
                0,
                0,
                0,
                dp(12)
        );

        cardCurso.setLayoutParams(parametros);

        return cardCurso;
    }

    private TextView criarTexto(
            String texto,
            float tamanho,
            boolean negrito,
            String cor
    ) {
        TextView textView =
                new TextView(this);

        textView.setText(texto);

        textView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                tamanho
        );

        textView.setTextColor(
                Color.parseColor(cor)
        );

        if (negrito) {
            textView.setTypeface(
                    null,
                    Typeface.BOLD
            );
        }

        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametros.setMargins(
                0,
                0,
                0,
                dp(6)
        );

        textView.setLayoutParams(parametros);

        return textView;
    }

    private String valorOuPadrao(
            String valor,
            String padrao
    ) {
        if (valor == null
                || valor.trim().isEmpty()) {

            return padrao;
        }

        return valor;
    }

    private String formatarDataHora(
            String valor
    ) {
        if (valor == null
                || valor.isEmpty()) {

            return "Não informada";
        }

        return valor
                .replace("T", " ")
                .replace(".000Z", "");
    }

    private int dp(int valor) {
        return Math.round(
                valor
                        * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }
}