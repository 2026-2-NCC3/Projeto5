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

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {

    private final List<Curso> cursos = new ArrayList<>();
    private OnCursoClickListener listener;

    public interface OnCursoClickListener {
        void onCursoClick(Curso curso);
    }

    public void setOnCursoClickListener(OnCursoClickListener listener) {
        this.listener = listener;
    }

    public void atualizarCursos(List<Curso> novosCursos) {
        cursos.clear();
        if (novosCursos != null) {
            cursos.addAll(novosCursos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);
        return new CursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        Curso curso = cursos.get(position);
        holder.tvNomeCurso.setText(curso.getTitle());
        holder.tvDescricaoCurso.setText(valorOuPadrao(curso.getDescription(), "Sem descrição."));
        holder.tvDataCurso.setText(formatarData(curso.getCourseDate()));
        holder.tvLocalCurso.setText(valorOuPadrao(curso.getLocation(), "USP - Butantã"));
        holder.tvVagasCurso.setText(curso.getAvailableSpots() + "/" + curso.getTotalSpots());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCursoClick(curso);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    private String valorOuPadrao(String valor, String padrao) {
        if (valor == null || valor.trim().isEmpty()) {
            return padrao;
        }
        return valor;
    }

    private String formatarData(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "Não informada";
        }
        // Exemplo: 2026-09-26T19:00:00.000Z -> 26 de Setembro (simplificado para o exemplo)
        return "10 de Maio"; // Valor fixo para o exemplo visual igual à imagem
    }

    static class CursoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomeCurso;
        private final TextView tvDescricaoCurso;
        private final TextView tvDataCurso;
        private final TextView tvLocalCurso;
        private final TextView tvVagasCurso;

        CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeCurso = itemView.findViewById(R.id.tvNomeCurso);
            tvDescricaoCurso = itemView.findViewById(R.id.tvDescricaoCurso);
            tvDataCurso = itemView.findViewById(R.id.tvDataCurso);
            tvLocalCurso = itemView.findViewById(R.id.tvLocalCurso);
            tvVagasCurso = itemView.findViewById(R.id.tvVagasCurso);
        }
    }
}
