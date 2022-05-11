package com.codesoom.assignment.dto.session;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class SessionRequestDto {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @Builder
    public SessionRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
