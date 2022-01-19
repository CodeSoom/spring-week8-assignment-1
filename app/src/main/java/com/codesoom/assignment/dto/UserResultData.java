package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 요청 응답 데이터
 */
@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    private Long id;

    private String email;

    private String name;
}
