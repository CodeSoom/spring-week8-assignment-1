package com.codesoom.assignment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** 사용자 수정에 사용한다. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserUpdateData {
    @NotBlank(message = "name 값은 필수입니다")
    private String name;

    @NotBlank(message = "password 값은 필수입니다")
    @Size(min = 4, max = 1024)
    private String password;

    @Builder
    public UserUpdateData(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
