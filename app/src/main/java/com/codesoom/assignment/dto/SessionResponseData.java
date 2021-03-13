package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 후 전달 받는 데이터
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    /**
     * 로그인 토큰
     */
    private String accessToken;
}
