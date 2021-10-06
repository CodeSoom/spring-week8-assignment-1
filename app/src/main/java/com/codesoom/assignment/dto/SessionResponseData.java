package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 응답에 쓰이는 클래스
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    private String accessToken;
}
