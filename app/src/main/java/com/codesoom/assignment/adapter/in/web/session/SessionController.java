package com.codesoom.assignment.adapter.in.web.session;

import com.codesoom.assignment.adapter.in.web.session.dto.request.SessionRequestDto;
import com.codesoom.assignment.adapter.in.web.session.dto.response.SessionResponseDto;
import com.codesoom.assignment.auth.application.port.in.AuthenticationUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final AuthenticationUseCase authenticationUseCase;

    public SessionController(final AuthenticationUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    /**
     * 로그인 성공 시 인증 토큰을 리턴합니다.
     *
     * @param sessionRequestDto 로그인 정보
     * @return 인증 토큰 리턴
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseDto login(@RequestBody @Valid final SessionRequestDto sessionRequestDto) {
        String email = sessionRequestDto.getEmail();
        String password = sessionRequestDto.getPassword();

        return new SessionResponseDto(
                authenticationUseCase.login(email, password)
        );
    }
}
