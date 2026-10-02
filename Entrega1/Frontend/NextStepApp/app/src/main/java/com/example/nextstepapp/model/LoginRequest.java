package com.example.nextstepapp.model;

public class LoginRequest {

    private final String id;
    private final String password;

    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }
}