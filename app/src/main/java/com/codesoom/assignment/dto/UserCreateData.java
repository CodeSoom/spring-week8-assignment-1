package com.codesoom.assignment.dto;

import com.codesoom.assignment.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** 사용자 생성에 사용한다. */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserCreateData {
    @NotBlank(message = "name 값은 필수입니다")
    private String name;

    @NotBlank(message = "email 값은 필수입니다")
    @Size(min=3)
    private String email;

    @NotBlank(message = "password 값은 필수입니다")
    @Size(min = 4, max = 1024)
    private String password;

    @Builder
    public UserCreateData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /** 사용자 데이터를 엔티티로 바꾼다. */
    public User toEntity() {
        return User.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
    }
}
