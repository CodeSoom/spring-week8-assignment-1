package com.codesoom.assignment.user.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 사용자 수정 명세서.
 */
@Getter
@NoArgsConstructor
public class UserModificationData {
    /**
     * 사용자 이름.
     */
    @NotBlank
    @Mapping("name")
    private String name;

    /**
     * 사용자 비밀번호.
     */
    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;

    @Builder
    public UserModificationData(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
