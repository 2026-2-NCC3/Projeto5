package com.example.nextstepapp.model;

public class Curso {
    private int id;
    private String nome;
    private int cargaHoraria; // em horas

    public Curso(int id, String nome, int cargaHoraria) {
        this.id = id;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getCargaHoraria() { return cargaHoraria; }
}