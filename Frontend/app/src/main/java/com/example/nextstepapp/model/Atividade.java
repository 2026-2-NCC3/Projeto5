package com.example.nextstepapp.model;

public class Atividade {
    private String titulo;
    private String data;
    private String horario;
    private String local;

    public Atividade(String titulo, String data, String horario, String local) {
        this.titulo = titulo;
        this.data = data;
        this.horario = horario;
        this.local = local;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getInfo() {
        return "Atividade: " + titulo + "\n\n"
                + "Data: " + data + "\n"
                + "Horário: " + horario + "\n"
                + "Local: " + local;
    }
}