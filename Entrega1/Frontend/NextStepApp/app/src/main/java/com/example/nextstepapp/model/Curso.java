package com.example.nextstepapp.model;

public class Curso {

    private String id;
    private String title;
    private String description;
    private String location;
    private String course_date;
    private String course_time_end;
    private int total_spots;
    private int available_spots;
    private int points_awarded;
    private int has_certificate;
    private String category;
    private String enrollment_id;
    private String enrollment_status;

    public Curso() {
    }

    public Curso(String id, String title, String description, String location, String course_date, String course_time_end, int total_spots, int available_spots, int points_awarded, int has_certificate, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.course_date = course_date;
        this.course_time_end = course_time_end;
        this.total_spots = total_spots;
        this.available_spots = available_spots;
        this.points_awarded = points_awarded;
        this.has_certificate = has_certificate;
        this.category = category;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public String getCourseDate() {
        return course_date;
    }
    public String getCourseTimeEnd() {
        return course_time_end;
    }
    public int getTotalSpots() {
        return total_spots;
    }
    public int getAvailableSpots() {
        return available_spots;
    }
    public int getPointsAwarded() {
        return points_awarded;
    }
    public int getHasCertificate() {
        return has_certificate;
    }
    public String getCategory() {
        return category;
    }
    public String getEnrollmentId() {
        return enrollment_id;
    }
    public String getEnrollmentStatus() {
        return enrollment_status;
    }
    public String getInfoAgenda() {
        String localSeguro = location == null ? "Não informado" : location;
        String statusSeguro = enrollment_status == null ? "Não informado" : enrollment_status;

        return "Curso: " + title
                + "\n\nData e início: " + formatarDataHora(course_date)
                + "\nTérmino: " + formatarDataHora(course_time_end)
                + "\nLocal: " + localSeguro
                + "\nPontos: " + points_awarded
                + "\nSituação: " + statusSeguro;
    }

    private String formatarDataHora(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "Não informado";
        }

        return valor
                .replace("T", " ")
                .replace(".000Z", "");
    }
}
