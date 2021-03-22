package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 인증 요청 정보.
 */
@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
