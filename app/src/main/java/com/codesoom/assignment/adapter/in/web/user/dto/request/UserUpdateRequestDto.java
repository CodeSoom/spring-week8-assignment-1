package com.codesoom.assignment.adapter.in.web.user.dto.request;

import com.codesoom.assignment.user.application.port.command.UserUpdateRequest;
import com.codesoom.assignment.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateRequestDto implements UserUpdateRequest {
    @NotBlank(message = "이름을 입력하세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력하세요")
    @Size(min = 4, max = 1024, message = "비밀번호는 4자 이상 1024자 이하로 입력해야 합니다")
    private String password;

    @Builder
    private UserUpdateRequestDto(final String name, final String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public User toEntity(final PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(this.name)
                .password(passwordEncoder.encode(this.password))
                .build();
    }
}
