package com.codesoom.assignment.user.adapter.in.web.dto.request;

import com.codesoom.assignment.user.application.port.command.UserCreateRequest;
import com.codesoom.assignment.user.application.port.command.UserMapper;
import com.codesoom.assignment.user.domain.User;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class UserCreateRequestDto implements UserCreateRequest {
    @NotBlank(message = "이메일을 입력하세요")
    @Size(min = 3)
    @Mapping("email")
    private String email;

    @NotBlank(message = "이름을 입력하세요")
    @Mapping("name")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4, max = 1024, message = "비밀번호는 4자 이상 1024자 이하로 입력해야 합니다")
    @Mapping("password")
    private String password;

    @Override
    public User toEntity() {
        return UserMapper.INSTANCE.toEntity(this);
    }
}
