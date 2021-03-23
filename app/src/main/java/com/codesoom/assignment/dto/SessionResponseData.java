package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 세션 응답 DTO.
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    private String accessToken;
}
