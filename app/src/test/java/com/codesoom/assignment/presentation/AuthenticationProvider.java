package com.codesoom.assignment.presentation;

import com.codesoom.assignment.auth.application.exception.InvalidTokenException;
import com.codesoom.assignment.auth.application.port.in.AuthenticationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static com.codesoom.assignment.support.AuthHeaderFixture.관리자_1004번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_값_비정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_2번_정상_토큰;
import static com.codesoom.assignment.support.RoleFixture.관리자_1004번_권한;
import static com.codesoom.assignment.support.RoleFixture.유저_1번_권한;
import static com.codesoom.assignment.support.RoleFixture.유저_2번_권한;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public class AuthenticationProvider {
    @MockBean
    private AuthenticationUseCase authenticationUseCase;

    @BeforeEach
    void setUpAuthToken() {
        given(authenticationUseCase.parseToken(eq(유저_1번_값_비정상_토큰.토큰_값())))
                .willThrow(new InvalidTokenException());

        given(authenticationUseCase.parseToken(eq(유저_1번_정상_토큰.토큰_값())))
                .willReturn(유저_1번_정상_토큰.아이디());
        given(authenticationUseCase.roles(eq(유저_1번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(유저_1번_권한.권한_데이터_생성()));

        given(authenticationUseCase.parseToken(eq(유저_2번_정상_토큰.토큰_값())))
                .willReturn(유저_2번_정상_토큰.아이디());
        given(authenticationUseCase.roles(eq(유저_2번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(유저_2번_권한.권한_데이터_생성()));

        given(authenticationUseCase.parseToken(eq(관리자_1004번_정상_토큰.토큰_값())))
                .willReturn(관리자_1004번_정상_토큰.아이디());
        given(authenticationUseCase.roles(eq(관리자_1004번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(관리자_1004번_권한.권한_데이터_생성()));
    }
}
