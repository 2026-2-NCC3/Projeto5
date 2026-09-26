package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nextstepapp.MainActivity;
import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.network.ApiClient;
import com.example.nextstepapp.network.OfflineManager;
import com.example.nextstepapp.session.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaActivity extends AppCompatActivity {

    private final List<Curso> agenda =
            new ArrayList<>();

    private TextView tvInfoAgenda;
    private CalendarView calendarView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_agenda
        );

        sessionManager =
                new SessionManager(this);

        tvInfoAgenda =
                findViewById(
                        R.id.tvInfoAgenda
                );

        calendarView =
                findViewById(
                        R.id.calendarView
                );

        Button btnVoltarAgenda =
                findViewById(
                        R.id.btnVoltarAgenda
                );

        calendarView.setEnabled(false);

        calendarView.setOnDateChangeListener(
                (view, year, month, dayOfMonth) -> {

                    String dataSelecionada =
                            String.format(
                                    Locale.getDefault(),
                                    "%04d-%02d-%02d",
                                    year,
                                    month + 1,
                                    dayOfMonth
                            );

                    exibirAgendaDaData(
                            dataSelecionada
                    );
                }
        );

        btnVoltarAgenda.setOnClickListener(
                v -> finish()
        );

        carregarAgenda();
    }

    private void carregarAgenda() {

        if (OfflineManager.isOffline(this)) {

            carregarAgendaOffline();

        } else {

            carregarAgendaOnline();
        }
    }

    private void carregarAgendaOnline() {

        String token =
                sessionManager.obterToken();

        if (token == null) {

            voltarParaLogin();

            return;
        }

        tvInfoAgenda.setText(
                "Carregando sua agenda..."
        );

        ApiClient
                .getApiService()
                .getAgenda(
                        "Bearer " + token
                )
                .enqueue(
                        new Callback<
                                ApiResponse<List<Curso>>
                                >() {

                            @Override
                            public void onResponse(
                                    Call<ApiResponse<List<Curso>>> call,
                                    Response<ApiResponse<List<Curso>>> response
                            ) {

                                if (response.code() == 401) {

                                    sessionManager
                                            .limparSessao();

                                    voltarParaLogin();

                                    return;
                                }

                                ApiResponse<List<Curso>> body =
                                        response.body();

                                if (response.isSuccessful()
                                        && body != null
                                        && body.isSuccess()) {

                                    agenda.clear();

                                    if (body.getData() != null) {

                                        agenda.addAll(
                                                body.getData()
                                        );
                                    }

                                    calendarView.setEnabled(true);

                                    if (agenda.isEmpty()) {

                                        tvInfoAgenda.setText(
                                                "Você ainda não possui cursos na agenda."
                                        );

                                    } else {

                                        tvInfoAgenda.setText(
                                                "Selecione uma data no calendário.\n"
                                                        + "Cursos encontrados: "
                                                        + agenda.size()
                                        );
                                    }

                                    return;
                                }

                                tvInfoAgenda.setText(
                                        "Não foi possível carregar a agenda."
                                );
                            }

                            @Override
                            public void onFailure(
                                    Call<ApiResponse<List<Curso>>> call,
                                    Throwable throwable
                            ) {

                                tvInfoAgenda.setText(
                                        "Falha de conexão com o servidor."
                                );
                            }
                        }
                );
    }

    private void carregarAgendaOffline() {

        tvInfoAgenda.setText(
                "Carregando agenda offline..."
        );

        String jsonCursos =
                "["
                        + "{"
                        + "\"id\":\"1\","
                        + "\"title\":\"Introdução à Programação\","
                        + "\"description\":\"Curso de introdução aos conceitos de programação.\","
                        + "\"location\":\"FECAP\","
                        + "\"course_date\":\"2026-09-26T19:00:00.000Z\","
                        + "\"course_time_end\":\"21:00\","
                        + "\"total_spots\":30,"
                        + "\"available_spots\":18,"
                        + "\"points_awarded\":50,"
                        + "\"has_certificate\":1,"
                        + "\"category\":\"Tecnologia\""
                        + "},"
                        + "{"
                        + "\"id\":\"2\","
                        + "\"title\":\"Banco de Dados\","
                        + "\"description\":\"Aprendizado de conceitos básicos de banco de dados.\","
                        + "\"location\":\"Laboratório de Informática\","
                        + "\"course_date\":\"2026-09-27T14:00:00.000Z\","
                        + "\"course_time_end\":\"16:00\","
                        + "\"total_spots\":25,"
                        + "\"available_spots\":12,"
                        + "\"points_awarded\":40,"
                        + "\"has_certificate\":1,"
                        + "\"category\":\"Banco de Dados\""
                        + "}"
                        + "]";

        try {

            Gson gson =
                    new Gson();

            Curso[] cursosArray =
                    gson.fromJson(
                            jsonCursos,
                            Curso[].class
                    );

            agenda.clear();

            if (cursosArray != null) {

                for (Curso curso : cursosArray) {

                    agenda.add(curso);
                }
            }

            calendarView.setEnabled(true);

            tvInfoAgenda.setText(
                    "Selecione uma data no calendário.\n"
                            + "Cursos encontrados: "
                            + agenda.size()
                            + "\nModo offline"
            );

        } catch (Exception e) {

            tvInfoAgenda.setText(
                    "Erro ao carregar a agenda offline."
            );
        }
    }

    private void exibirAgendaDaData(
            String dataSelecionada
    ) {

        StringBuilder informacoes =
                new StringBuilder();

        for (Curso curso : agenda) {

            String dataCurso =
                    extrairData(
                            curso.getCourseDate()
                    );

            if (dataSelecionada.equals(
                    dataCurso
            )) {

                if (informacoes.length() > 0) {

                    informacoes.append(
                            "\n\n----------------\n\n"
                    );
                }

                informacoes.append(
                        curso.getInfoAgenda()
                );
            }
        }

        if (informacoes.length() == 0) {

            tvInfoAgenda.setText(
                    "Nenhum curso agendado para "
                            + dataSelecionada
            );

        } else {

            tvInfoAgenda.setText(
                    informacoes.toString()
            );
        }
    }

    private String extrairData(
            String valor
    ) {

        if (valor == null
                || valor.length() < 10) {

            return "";
        }

        return valor.substring(
                0,
                10
        );
    }

    private void voltarParaLogin() {

        Intent intent =
                new Intent(
                        AgendaActivity.this,
                        MainActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}