package com.example.nextstepapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private EditText etSearch;
    private String categoriaSelecionada = "Todos";

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
        etSearch = findViewById(R.id.etSearch);

        // Configurar RecyclerView
        rvCursos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CursoAdapter();
        rvCursos.setAdapter(adapter);

        // Configurar Clique no Curso
        adapter.setOnCursoClickListener(curso -> {
            Intent intent = new Intent(this, DetalhesEventoActivity.class);
            intent.putExtra("course_id", curso.getId());
            intent.putExtra("title", curso.getTitle());
            intent.putExtra("description", curso.getDescription());
            intent.putExtra("location", curso.getLocation());
            intent.putExtra("course_date", curso.getCourseDate());
            intent.putExtra("course_time_end", curso.getCourseTimeEnd());
            intent.putExtra("category", curso.getCategory());
            startActivity(intent);
        });

        setupFilters();
        setupSearch();
        setupNavigation();

        // Carregar Cursos
        carregarCursosDaApi();
    }

    private void setupSearch() {
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    aplicarFiltros(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupNavigation() {
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setClickable(true);
            btnBack.setFocusable(true);
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> startActivity(new Intent(this, NotificacoesActivity.class)));
        }

        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));
        }

        // Bottom Bar
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.nav_agenda).setOnClickListener(v -> {
            startActivity(new Intent(this, AgendaActivity.class));
            finish();
        });

        View navEvolucao = findViewById(R.id.nav_evolucao);
        if (navEvolucao != null) {
            navEvolucao.setOnClickListener(v -> {
                startActivity(new Intent(this, PresencaActivity.class));
                finish();
            });
        }

        View navMensagens = findViewById(R.id.nav_mensagens);
        if (navMensagens != null) {
            navMensagens.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
                finish();
            });
        }
    }

    private void setupFilters() {
        View.OnClickListener filterClick = v -> {
            resetFilterColors();
            TextView tv = (TextView) v;
            tv.setBackgroundResource(R.drawable.bg_filter_selected);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));

            categoriaSelecionada = tv.getText().toString();
            String queryBusca = etSearch != null ? etSearch.getText().toString() : "";
            aplicarFiltros(queryBusca);
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

    private void aplicarFiltros(String query) {
        String cleanQuery = query == null ? "" : query.trim().toLowerCase();

        List<Curso> filtrados = new ArrayList<>();
        for (Curso curso : listaCompleta) {
            boolean bateCategoria = categoriaSelecionada.equalsIgnoreCase("Todos")
                    || (curso.getCategory() != null && curso.getCategory().equalsIgnoreCase(categoriaSelecionada));

            boolean bateQuery = cleanQuery.isEmpty()
                    || (curso.getTitle() != null && curso.getTitle().toLowerCase().contains(cleanQuery))
                    || (curso.getDescription() != null && curso.getDescription().toLowerCase().contains(cleanQuery))
                    || (curso.getLocation() != null && curso.getLocation().toLowerCase().contains(cleanQuery));

            if (bateCategoria && bateQuery) {
                filtrados.add(curso);
            }
        }
        adapter.atualizarCursos(filtrados);
    }

    private void carregarCursosDaApi() {
        ApiClient.getApiService().getCursos().enqueue(new Callback<ApiResponse<List<Curso>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Curso>>> call, Response<ApiResponse<List<Curso>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null && !response.body().getData().isEmpty()) {
                    listaCompleta = response.body().getData();
                    adapter.atualizarCursos(listaCompleta);
                } else {
                    carregarCursosSeedFallback();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                carregarCursosSeedFallback();
            }
        });
    }

    private void carregarCursosSeedFallback() {
        // Cursos idênticos ao cadastrado em seed.js no backend
        listaCompleta.clear();
        listaCompleta.add(new Curso(
                "curso-mobile-1",
                "Introdução à Programação Mobile",
                "Aprenda os conceitos fundamentais de arquitetura mobile e desenvolva seu primeiro aplicativo Android com Java e APIs REST.",
                "USP - Campus Butantã (Laboratório de Informática 3)",
                "2026-10-15T10:00:00.000Z",
                "2026-10-15T13:00:00.000Z",
                35, 28, 100, 1, "Tecnologia"
        ));

        listaCompleta.add(new Curso(
                "curso-dados-2",
                "Análise e Ciência de Dados com Python",
                "Aprenda a analisar dados, criar gráficos e identificar padrões utilizando ferramentas modernas e casos reais de empresas.",
                "FECAP - Sala Multimídia B",
                "2026-10-22T14:00:00.000Z",
                "2026-10-22T17:00:00.000Z",
                30, 20, 80, 1, "Tecnologia"
        ));

        listaCompleta.add(new Curso(
                "curso-softskills-3",
                "Habilidades Profissionais e Inteligência Emocional",
                "Desenvolva comunicação interpessoal, oratória, liderança e resolução colaborativa de problemas para se destacar no mercado.",
                "FECAP - Auditório Principal",
                "2026-10-29T09:00:00.000Z",
                "2026-10-29T12:00:00.000Z",
                40, 25, 60, 1, "Soft Skills"
        ));

        listaCompleta.add(new Curso(
                "curso-empreendedorismo-4",
                "Inovação, Criatividade e Empreendedorismo",
                "Aprenda como transformar ideias em modelos de negócios viáveis, aplicando design thinking e metodologias ágeis.",
                "UNICAMP - Hub de Inovação",
                "2026-11-05T18:00:00.000Z",
                "2026-11-05T21:00:00.000Z",
                25, 15, 90, 1, "Negócios"
        ));

        adapter.atualizarCursos(listaCompleta);
    }
}
