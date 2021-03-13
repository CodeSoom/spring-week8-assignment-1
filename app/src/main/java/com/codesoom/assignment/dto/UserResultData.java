package com.codesoom.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 생성, 변경된 회원정보 결과 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    /**
     * 생성, 변경된 회원정보 고유 ID값
     */
    private Long id;

    /**
     * 생성, 변경된 회원정보 Email
     */
    private String email;

    /**
     * 생성, 변경된 회원정보 이름
     */
    private String name;
}
