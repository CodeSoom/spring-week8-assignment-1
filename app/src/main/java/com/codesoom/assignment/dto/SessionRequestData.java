package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 로그인 요청 데이터
 */
@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
