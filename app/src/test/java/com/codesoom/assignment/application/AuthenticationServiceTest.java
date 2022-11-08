package com.codesoom.assignment.application;

import com.codesoom.assignment.domain.Role;
import com.codesoom.assignment.errors.InvalidTokenException;
import com.codesoom.assignment.security.UserAuthentication;
import com.codesoom.assignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaD0";

    private AuthenticationService authenticationService;


    @BeforeEach
    void setUp() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);
        RoleService roleService = mock(RoleService.class);

        authenticationService = new AuthenticationService(roleService, jwtUtil);

        given(roleService.getRoles(1L)).willReturn(List.of(new Role("USER")));
    }

    @Test
    void authenticateWithValidToken() {
        UserAuthentication authenticate = authenticationService.authenticate(VALID_TOKEN);

        assertThat(authenticate.getUserId()).isEqualTo(1L);
        assertThat(authenticate.getAuthorities())
                .hasSize(1)
                .first().extracting(GrantedAuthority::getAuthority).isEqualTo("USER");
    }

    @Test
    void authenticateWithInvalidToken() {
        assertThatThrownBy(
                () -> authenticationService.authenticate(INVALID_TOKEN)
        ).isInstanceOf(InvalidTokenException.class);
    }

    @Test
    void parseTokenWithValidToken() {
        Long userId = authenticationService.getUserId(VALID_TOKEN);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void parseTokenWithInvalidToken() {
        assertThatThrownBy(
                () -> authenticationService.getUserId(INVALID_TOKEN)
        ).isInstanceOf(InvalidTokenException.class);
    }
}
