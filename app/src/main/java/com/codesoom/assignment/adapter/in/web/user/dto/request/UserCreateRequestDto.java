package com.codesoom.assignment.adapter.in.web.user.dto.request;

import com.codesoom.assignment.adapter.in.web.user.dto.UserMapper;
import com.codesoom.assignment.user.application.port.command.UserCreateRequest;
import com.codesoom.assignment.user.domain.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
public class UserCreateRequestDto implements UserCreateRequest {
    @NotBlank(message = "이메일을 입력하세요")
    @Size(min = 3)
    private final String email;

    @NotBlank(message = "이름을 입력하세요")
    private final String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4, max = 1024, message = "비밀번호는 4자 이상 1024자 이하로 입력해야 합니다")
    private final String password;

    @Builder
    private UserCreateRequestDto(final String email, final String name, final String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    @Override
    public User toEntity() {
        return UserMapper.INSTANCE.toEntity(this);
    }
}
