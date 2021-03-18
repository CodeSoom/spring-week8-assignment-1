package com.codesoom.assignment.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 사용자 수정 요청 정보.
 */
@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("password")
    private String password;

    @Builder
    public UserUpdateRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
