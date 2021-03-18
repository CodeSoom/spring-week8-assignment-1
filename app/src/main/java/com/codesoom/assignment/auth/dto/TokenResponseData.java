package com.codesoom.assignment.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인증 토큰 응답.
 */
@Getter
@NoArgsConstructor
public class TokenResponseData {
    /**
     * 사용자 인증 토큰.
     */
    private String accessToken;

    @Builder
    public TokenResponseData(String accessToken) {
        this.accessToken = accessToken;
    }
}
