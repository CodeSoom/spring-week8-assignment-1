package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 로그엔 요청에 쓰이는 클래스
 */
@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
