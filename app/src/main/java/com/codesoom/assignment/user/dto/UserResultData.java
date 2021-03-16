package com.codesoom.assignment.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 응답 정보.
 */
@Getter
public class UserResultData {
    /**
     * 사용자 식별자.
     */
    private Long id;

    /**
     * 사용자 이메일.
     */
    private String email;

    /**
     * 사용자 이름.
     */
    private String name;

    @Builder
    public UserResultData(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
