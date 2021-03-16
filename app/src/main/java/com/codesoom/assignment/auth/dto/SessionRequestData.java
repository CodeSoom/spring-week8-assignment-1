package com.codesoom.assignment.auth.dto;

import lombok.Getter;

/**
 * 사용자 인증 요청.
 */
@Getter
public class SessionRequestData {
    /** 이메일. */
    private String email;
    /** 비밀번호. */
    private String password;
}
