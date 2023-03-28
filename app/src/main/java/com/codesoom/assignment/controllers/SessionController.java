package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.dto.SessionRequestData;
import com.codesoom.assignment.dto.SessionResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Controller For Session
 *
 * @author sim
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;

    /**
     * SessionController 생성자 메서드
     *
     * @param authenticationService - 인증 서비스
     */
    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 로그인을 시도한다.
     * 
     * @param sessionRequestData - 이메일과 비밀번호를 갖는 요청 객체
     * @return JWT accessToken을 담은 객체
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
