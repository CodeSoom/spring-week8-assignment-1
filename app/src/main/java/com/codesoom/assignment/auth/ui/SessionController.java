package com.codesoom.assignment.auth.ui;

import com.codesoom.assignment.auth.application.AuthenticationService;
import com.codesoom.assignment.auth.dto.SessionRequestData;
import com.codesoom.assignment.auth.dto.TokenResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 인증 요청을 처리합니다.
 */
@RestController
@RequestMapping("/session")
public class SessionController {
    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 로그인 처리를 합니다.
     *
     * @param sessionRequestData 로그인 요청 정보
     * @return 토큰 응답
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseData login(
            @RequestBody SessionRequestData sessionRequestData
    ) {
        String email = sessionRequestData.getEmail();
        String password = sessionRequestData.getPassword();

        String accessToken = authenticationService.login(email, password);

        return TokenResponseData.builder()
                .accessToken(accessToken)
                .build();
    }
}
