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
import com.example.nextstepapp.model.Curso;
import java.util.ArrayList;
import java.util.List;

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
            startActivity(new Intent(this, DetalhesEventoActivity.class));
        });

        // Configurar Filtros
        setupFilters();

        // Botão Voltar e Top Bar
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        View btnNotification = findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                startActivity(new Intent(this, NotificacoesActivity.class));
            });
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
            navAgenda.setOnClickListener(v -> {
                startActivity(new Intent(this, AgendaActivity.class));
            });
        }

        View navEvolucao = findViewById(R.id.nav_evolucao);
        if (navEvolucao != null) {
            navEvolucao.setOnClickListener(v -> {
                startActivity(new Intent(this, PresencaActivity.class));
            });
        }

        View navMensagens = findViewById(R.id.nav_mensagens);
        if (navMensagens != null) {
            navMensagens.setOnClickListener(v -> {
                Toast.makeText(this, "Nenhuma mensagem recente.", Toast.LENGTH_SHORT).show();
            });
        }

        // Carregar Dados Mockados
        carregarDadosExemplo();
    }

    private void setupFilters() {
        View.OnClickListener filterClick = v -> {
            resetFilterColors();
            TextView tv = (TextView) v;
            tv.setBackgroundResource(R.drawable.bg_filter_selected);
            tv.setTextColor(ContextCompat.getColor(this, R.color.white));

            String categoria = tv.getText().toString();
            filtrarLista(categoria);
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

    private void carregarDadosExemplo() {
        listaCompleta.clear();
        
        listaCompleta.add(new Curso("1", "Introdução à Programação Mobile",
            "Aprenda os conceitos básicos de desenvolvimento mobile e crie seu primeiro aplicativo.", 
            "USP - Butantã", "2026-04-25T10:00:00", "11:00", 30, 25, 50, 1, "Tecnologia"));

        listaCompleta.add(new Curso("2", "Análise de Dados",
            "Descubra como transformar dados em decisões com ferramentas práticas e projetos reais.", 
            "USP - Butantã", "2026-05-10T14:00:00", "16:00", 25, 18, 40, 1, "Tecnologia"));

        listaCompleta.add(new Curso("3", "Habilidades Profissionais",
            "Desenvolva sua comunicação, trabalho em equipe e inteligência emocional.", 
            "USP - Butantã", "2026-05-17T09:00:00", "12:00", 30, 20, 30, 1, "Soft Skills"));

        listaCompleta.add(new Curso("4", "Inovação e Empreendedorismo",
            "Aprenda a criar soluções inovadoras e a transformar ideias em projetos reais.", 
            "USP - Butantã", "2026-05-24T18:00:00", "20:00", 20, 5, 60, 1, "Negócios"));

        adapter.atualizarCursos(listaCompleta);
    }
}
