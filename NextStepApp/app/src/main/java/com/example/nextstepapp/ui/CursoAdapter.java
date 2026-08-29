package com.example.nextstepapp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nextstepapp.R;
import com.example.nextstepapp.model.Curso;

import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {

    private List<Curso> cursos;

    public CursoAdapter(List<Curso> cursos) {
        this.cursos = cursos;
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_curso, parent, false);
        return new CursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        Curso curso = cursos.get(position);
        holder.tvNomeCurso.setText(curso.getNome());
        holder.tvCargaHoraria.setText(curso.getCargaHoraria() + " horas");
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeCurso;
        TextView tvCargaHoraria;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeCurso = itemView.findViewById(R.id.tvNomeCurso);
            tvCargaHoraria = itemView.findViewById(R.id.tvCargaHoraria);
        }
    }
}
