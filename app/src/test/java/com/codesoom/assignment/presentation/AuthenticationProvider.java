package com.codesoom.assignment.presentation;

import com.codesoom.assignment.role.domain.Role;
import com.codesoom.assignment.session.application.AuthenticationService;
import com.codesoom.assignment.session.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static com.codesoom.assignment.support.AuthHeaderFixture.관리자_1004번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_값_비정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_1번_정상_토큰;
import static com.codesoom.assignment.support.AuthHeaderFixture.유저_2번_정상_토큰;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public class AuthenticationProvider {
    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUpAuthToken() {
        given(authenticationService.parseToken(eq(유저_1번_값_비정상_토큰.토큰_값())))
                .willThrow(new InvalidTokenException(유저_1번_값_비정상_토큰.토큰_값()));

        given(authenticationService.parseToken(eq(유저_1번_정상_토큰.토큰_값())))
                .willReturn(유저_1번_정상_토큰.아이디());
        given(authenticationService.roles(eq(유저_1번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(new Role(유저_1번_정상_토큰.아이디(), "USER")));

        given(authenticationService.parseToken(eq(유저_2번_정상_토큰.토큰_값())))
                .willReturn(유저_2번_정상_토큰.아이디());
        given(authenticationService.roles(eq(유저_2번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(new Role(유저_2번_정상_토큰.아이디(), "USER")));

        given(authenticationService.parseToken(eq(관리자_1004번_정상_토큰.토큰_값())))
                .willReturn(관리자_1004번_정상_토큰.아이디());
        given(authenticationService.roles(eq(관리자_1004번_정상_토큰.아이디())))
                .willReturn(Arrays.asList(new Role(관리자_1004번_정상_토큰.아이디(), "ADMIN")));
    }
}
