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

        // Configurar Clique no Curso (Abre Detalhes)
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

        // Configurar Filtros
        setupFilters();

        // Botão Voltar e Navegação
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.nav_inicio).setOnClickListener(v -> finish());

        // Carregar Cursos da API
        carregarCursosDaApi();
    }

    private void setupFilters() {
        View.OnClickListener filterClick = v -> {
            resetFilterColors();
            TextView tv = (TextView) v;
            tv.setBackgroundResource(R.drawable.bg_filter_selected);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));

            filtrarCursos(tv.getText().toString());
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

    private void filtrarCursos(String categoria) {
        if (categoria == null || categoria.equalsIgnoreCase("Todos")) {
            adapter.atualizarCursos(listaCompleta);
            return;
        }

        List<Curso> filtrados = new ArrayList<>();
        for (Curso curso : listaCompleta) {
            if (curso.getCategory() != null && curso.getCategory().equalsIgnoreCase(categoria)) {
                filtrados.add(curso);
            }
        }
        adapter.atualizarCursos(filtrados);
    }

    private void carregarCursosDaApi() {
        ApiClient.getApiService().getCursos().enqueue(new Callback<ApiResponse<List<Curso>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Curso>>> call, Response<ApiResponse<List<Curso>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    listaCompleta = response.body().getData();
                    adapter.atualizarCursos(listaCompleta);
                } else {
                    Toast.makeText(CursosActivity.this, "Erro ao carregar lista de cursos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Curso>>> call, Throwable t) {
                Toast.makeText(CursosActivity.this, "Erro de rede: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
