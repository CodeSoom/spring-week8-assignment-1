package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 세션 요청 DTO.
 */
@Getter
@NoArgsConstructor
public class SessionRequestData {
    @NotBlank
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;

    @Builder
    public SessionRequestData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean authenticate(String password) {
        return password.equals(this.password);
    }
}
