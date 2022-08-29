package com.codesoom.assignment.dto;

/**
 * 유저 등록 요청에 필요한 행위를 정의하고 있습니다.
 */
public interface UserRegisterRequest {
    String getEmail();

    String getPassword();

    String getName();
}
