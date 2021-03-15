package com.codesoom.assignment.auth.dto;

import lombok.Getter;

@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
