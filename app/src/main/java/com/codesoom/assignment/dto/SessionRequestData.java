package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 세션 요청서.
 */
@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
