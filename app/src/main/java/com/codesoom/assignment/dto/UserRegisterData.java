package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.User;
import lombok.Getter;

@Getter
public class UserRegisterData {
    private final String email;
    private final String password;
    private final String name;

    public UserRegisterData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User toUser() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
