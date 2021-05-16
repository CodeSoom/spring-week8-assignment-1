package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 사용자 인증 관련 요청을 처리합니다.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
@RequiredArgsConstructor
public class SessionController {
    private AuthenticationService authenticationService;

    /**
     * 주어진 시용자 로그인 정보로 로그인하고, 인증 토큰을 발급합니다.
     *
     * @param sessionRequestData 사용자 로그인 정보
     * @return 인증 토큰
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(
            @Valid @RequestBody SessionRequestData sessionRequestData
    ) {
        String accessToken = authenticationService.login(sessionRequestData);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
