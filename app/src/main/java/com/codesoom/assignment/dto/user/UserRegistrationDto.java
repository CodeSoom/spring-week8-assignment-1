package com.codesoom.assignment.dto.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class UserRegistrationDto {
    @NotBlank
    @Size(min = 3)
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 4, max = 512)
    private String password;

    @Builder
    public UserRegistrationDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
