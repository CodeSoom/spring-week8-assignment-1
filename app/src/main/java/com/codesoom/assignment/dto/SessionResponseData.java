package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 세션을 전달하는 형식.
 */
@Getter
@Builder
@AllArgsConstructor
public class SessionResponseData {
    private String accessToken;
}
