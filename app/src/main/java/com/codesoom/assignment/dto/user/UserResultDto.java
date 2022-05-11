package com.codesoom.assignment.dto.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResultDto {
    private Long id;

    private String email;

    private String username;

    @Builder
    public UserResultDto(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }
}
