package com.codesoom.assignment.controller;

import com.codesoom.assignment.application.authentication.AuthenticationService;
import com.codesoom.assignment.dto.session.SessionRequestDto;
import com.codesoom.assignment.dto.session.SessionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {
    private AuthenticationService authenticationService;

    SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseDto login(
            @RequestBody @Valid SessionRequestDto sessionRequestDto
    ) {
        final String email = sessionRequestDto.getEmail();
        final String password = sessionRequestDto.getPassword();

        final String accessToken = authenticationService.login(email, password);

        return SessionResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
