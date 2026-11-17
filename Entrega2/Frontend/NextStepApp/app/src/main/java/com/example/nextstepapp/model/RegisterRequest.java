package com.example.nextstepapp.model;

public class RegisterRequest {

    private final String id;
    private final String full_name;
    private final String password;

    public RegisterRequest(String id, String fullName, String password) {
        this.id = id;
        this.full_name = fullName;
        this.password = password;
    }
}