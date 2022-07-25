package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SessionRequestData {
    private final String email;

    private final String password;

    @JsonCreator
    @Builder
    public SessionRequestData(@JsonProperty("email") String email,
                              @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }
}
