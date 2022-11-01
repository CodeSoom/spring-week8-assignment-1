package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class SessionRequestData {
    private String email;
    private String password;

    public SessionRequestData(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
