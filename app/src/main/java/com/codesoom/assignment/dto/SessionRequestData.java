package com.codesoom.assignment.dto;

import lombok.Getter;

/**
 * 세션을 전달하는 형식
 */
@Getter
public class SessionRequestData {
    private String email;
    private String password;
}
