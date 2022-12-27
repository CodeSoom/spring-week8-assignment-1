package com.codesoom.assignment.auth.application.port.in;

import com.codesoom.assignment.role.domain.Role;

import java.util.List;

public interface AuthenticationUseCase {
    /**
     * 회원 로그인을 진행합니다.
     *
     * @param email    회원 이메일
     * @param password 회원 비밀번호
     * @return 인증 토큰 리턴
     */
    String login(final String email, final String password);

    /**
     * 인증 토큰의 유효성 검사를 진행합니다.
     *
     * @param accessToken 인증 토큰
     * @return 회원 고유 id
     */
    Long parseToken(final String accessToken);

    /**
     * 회원의 역할을 리턴합니다.
     *
     * @param userId 회원 고유 id
     * @return 회원 역할 리턴
     */
    List<Role> roles(final Long userId);
}
