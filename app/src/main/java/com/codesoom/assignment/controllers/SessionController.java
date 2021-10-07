package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import com.codesoom.assignment.errors.LoginFailException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 로그인을 담당한다.
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 인증 토큰을 리턴한다.
     *
     * @param sessionRequestData 로그인에 필요한 데이터
     * @return 인증 토큰
     * @throws LoginFailException 로그인에 사용된 데이터가 잘못된 경우
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(
            @RequestBody SessionRequestData sessionRequestData
    ) {
        String email = sessionRequestData.getEmail();
        String password = sessionRequestData.getPassword();

        String accessToken = authenticationService.login(email, password);

        return SessionResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
