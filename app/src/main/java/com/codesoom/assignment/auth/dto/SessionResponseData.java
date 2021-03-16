package com.codesoom.assignment.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 인증 응답.
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    /**
     * 사용자 인증 토큰.
     */
    private String accessToken;
}
