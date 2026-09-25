package com.example.nextstepapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.Curso;

import java.util.ArrayList;
import java.util.List;
public class CursoAdapter
        extends RecyclerView.Adapter<
        CursoAdapter.CursoViewHolder
        > {
    private final List<Curso> cursos = new ArrayList<>();
    public void atualizarCursos(
            List<Curso> novosCursos
    ) {
        cursos.clear();

        if (novosCursos != null) {
            cursos.addAll(novosCursos);
        }

        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_curso,
                        parent,
                        false
                );

        return new CursoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(
            @NonNull CursoViewHolder holder,
            int position
    ) {
        Curso curso = cursos.get(position);
        holder.tvNomeCurso.setText(
                curso.getTitle()
        );
        holder.tvDescricaoCurso.setText(
                valorOuPadrao(
                        curso.getDescription(),
                        "Sem descrição."
                )
        );
        holder.tvDataCurso.setText(
                "Início: "
                        + formatarDataHora(
                        curso.getCourseDate()
                )
        );
        holder.tvLocalCurso.setText(
                "Local: "
                        + valorOuPadrao(
                        curso.getLocation(),
                        "Não informado"
                )
        );
        holder.tvVagasCurso.setText(
                "Vagas disponíveis: "
                        + curso.getAvailableSpots()
        );

        holder.tvPontosCurso.setText(
                "Pontos: "
                        + curso.getPointsAwarded()
        );
    }
    @Override
    public int getItemCount() {
        return cursos.size();
    }
    private String valorOuPadrao(
            String valor,
            String padrao
    ) {
        if (valor == null || valor.trim().isEmpty()) {
            return padrao;
        }

        return valor;
    }
    private String formatarDataHora(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "Não informada";
        }
        return valor.replace("T", " ").replace(".000Z", "");
    }static class CursoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeCurso;
        private final TextView tvDescricaoCurso;
        private final TextView tvDataCurso;
        private final TextView tvLocalCurso;
        private final TextView tvVagasCurso;
        private final TextView tvPontosCurso;
        CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeCurso = itemView.findViewById(R.id.tvNomeCurso);
            tvDescricaoCurso = itemView.findViewById(R.id.tvDescricaoCurso);
            tvDataCurso = itemView.findViewById(R.id.tvDataCurso);
            tvLocalCurso = itemView.findViewById(R.id.tvLocalCurso);
            tvVagasCurso = itemView.findViewById(R.id.tvVagasCurso);
            tvPontosCurso = itemView.findViewById(R.id.tvPontosCurso);
        }
    }
}
