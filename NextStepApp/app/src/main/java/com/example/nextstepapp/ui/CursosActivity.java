package com.example.nextstepapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.Curso;

import java.util.ArrayList;
import java.util.List;

public class CursosActivity extends AppCompatActivity {

    private RecyclerView rvCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        String matricula = getIntent().getStringExtra("MATRICULA");

        rvCursos = findViewById(R.id.rvCursos);
        rvCursos.setLayoutManager(new LinearLayoutManager(this));

        List<Curso> listaCursos = carregarCursosFicticios();
        CursoAdapter adapter = new CursoAdapter(listaCursos);
        rvCursos.setAdapter(adapter);
    }

    // Dados fictícios por enquanto — na próxima etapa isso vem da API/banco
    private List<Curso> carregarCursosFicticios() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso(1, "Introdução à Programação", 40));
        cursos.add(new Curso(2, "Lógica de Programação", 30));
        cursos.add(new Curso(3, "Banco de Dados I", 50));
        cursos.add(new Curso(4, "Orientação Profissional", 20));
        return cursos;
    }
}