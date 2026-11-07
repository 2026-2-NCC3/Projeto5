package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.ApiResponse;
import com.example.nextstepapp.model.Curso;
import com.example.nextstepapp.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursosActivity extends AppCompatActivity {

    private RecyclerView rvCursos;
    private CursoAdapter adapter;
    private List<Curso> listaCompleta = new ArrayList<>();
    private TextView filterTodos, filterTecnologia, filterSoftSkills, filterNegocios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        // Inicializar Views
        rvCursos = findViewById(R.id.rvCursos);
        filterTodos = findViewById(R.id.filterTodos);
        filterTecnologia = findViewById(R.id.filterTecnologia);
        filterSoftSkills = findViewById(R.id.filterSoftSkills);
        filterNegocios = findViewById(R.id.filterNegocios);

        // Configurar RecyclerView
        rvCursos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CursoAdapter();
        rvCursos.setAdapter(adapter);

        // Clique no curso abre os detalhes (envia o id para a próxima tela)
        adapter.setOnCursoClickListener(curso -> {
            Intent intent = new Intent(this, DetalhesEventoActivity.class);
            intent.putExtra("course_id", curso.getId());
            startActivity(intent);
        });

        // Filtros
        setupFilters();

        // Botão Voltar e Top Bar
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // A tela de notificações não existe na Entrega2 (ver NotificacoesActivity na Entrega1)
        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v ->
                    Toast.makeText(this, "Notificações em breve.", Toast.LENGTH_SHORT).show());
        }

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
            navAgenda.setOnClickListener(v ->
                    startActivity(new Intent(this, AgendaActivity.class)));
        }

        View navEvolucao = findViewById(R.id.nav_evolucao);
        if (navEvolucao != null) {
            navEvolucao.setOnClickListener(v ->
                    startActivity(new Intent(this, PresencaActivity.class)));
        }

        View navMensagens = findViewById(R.id.nav_mensagens);
        if (navMensagens != null) {
            navMensagens.setOnClickListener(v ->
                    Toast.makeText(this, "Nenhuma mensagem recente.", Toast.LENGTH_SHORT).show());
        }

        // Dados reais do servidor
        carregarCursos();
    }

    private void carregarCursos() {
        ApiClient.getApiService().getCursos().enqueue(new Callback<ApiResponse<List<Curso>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Curso>>> call,
                                   Response<ApiResponse<List<Curso>>> response) {
                ApiResponse<List<Curso>> body = response.body();

                if (response.isSuccessful() && body != null
                        && body.isSuccess() && body.getData() != null) {
                    listaCompleta = body.getData();
                    adapter.atualizarCursos(listaCompleta);
                } else {
                    Toast.makeText(CursosActivity.this,
                            "Não foi possível carregar os cursos.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                Toast.makeText(CursosActivity.this,
                        "Erro de conexão. Tente novamente.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFilters() {
        View.OnClickListener filterClick = v -> {
            resetFilterColors();
            TextView tv = (TextView) v;
            tv.setBackgroundResource(R.drawable.bg_filter_selected);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));

            filtrarLista(tv.getText().toString());
        };

        filterTodos.setOnClickListener(filterClick);
        filterTecnologia.setOnClickListener(filterClick);
        filterSoftSkills.setOnClickListener(filterClick);
        filterNegocios.setOnClickListener(filterClick);
    }

    private void resetFilterColors() {
        TextView[] filters = {filterTodos, filterTecnologia, filterSoftSkills, filterNegocios};
        for (TextView f : filters) {
            f.setBackgroundResource(R.drawable.bg_filter_unselected);
            f.setTextColor(ContextCompat.getColor(this, R.color.purple_primary));
        }
    }

    private void filtrarLista(String categoria) {
        if (categoria.equalsIgnoreCase("Todos")) {
            adapter.atualizarCursos(listaCompleta);
            return;
        }

        List<Curso> filtrados = new ArrayList<>();
        for (Curso c : listaCompleta) {
            if (c.getCategory() != null && c.getCategory().equalsIgnoreCase(categoria)) {
                filtrados.add(c);
            }
        }
        adapter.atualizarCursos(filtrados);
    }
}