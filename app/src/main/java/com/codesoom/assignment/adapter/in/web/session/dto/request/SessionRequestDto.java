package com.codesoom.assignment.adapter.in.web.session.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class SessionRequestDto {
    @NotBlank(message = "이메일을 입력하세요")
    private final String email;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4, max = 1024, message = "비밀번호는 4자 이상 1024자 이하로 입력해야 합니다")
    private final String password;

    @Builder
    public SessionRequestDto(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
