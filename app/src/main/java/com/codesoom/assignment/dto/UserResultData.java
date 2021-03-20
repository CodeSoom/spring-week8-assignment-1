package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 유저와 관련된 결과를 저장하는 형식
 */
@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    private Long id;

    private String email;

    private String name;
}
