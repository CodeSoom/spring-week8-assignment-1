package com.codesoom.assignment.dto;

import lombok.Getter;

@Getter
public class UserRegisterData implements UserRegisterRequest {
    private final String email;
    private final String password;
    private final String name;

    public UserRegisterData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
