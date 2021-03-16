package com.codesoom.assignment.auth.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * 인증 토큰 응답.
 */
@Getter
@Builder
public class TokenResponseData {
    /**
     * 사용자 인증 토큰.
     */
    private String accessToken;
}
