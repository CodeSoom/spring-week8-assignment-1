package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 로그인 시 요청 데이터
 */
@Getter
public class SessionRequestData {
    /**
     * 요청받은 Email
     */
    private String email;

    /**
     * 요청받은 비밀번호
     */
    private String password;
}
