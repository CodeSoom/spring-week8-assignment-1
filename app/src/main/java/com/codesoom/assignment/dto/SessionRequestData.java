package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class SessionRequestData {
    private final String email;
    private final String password;

    public SessionRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
