package com.codesoom.assignment.user.adapter.in.web.dto.request;

import com.codesoom.assignment.user.application.port.command.UserMapper;
import com.codesoom.assignment.user.application.port.command.UserUpdateRequest;
import com.codesoom.assignment.user.domain.User;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
public class UserUpdateRequestDto implements UserUpdateRequest {
    @NotBlank(message = "이름을 입력하세요")
    private final String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4, max = 1024, message = "비밀번호는 4자 이상 1024자 이하로 입력해야 합니다")
    private final String password;

    @Builder
    public UserUpdateRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public User toEntity() {
        return UserMapper.INSTANCE.toEntity(this);
    }
}
