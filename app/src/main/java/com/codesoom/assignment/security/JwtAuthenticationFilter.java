package com.codesoom.assignment.security;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.domain.Role;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationService authenticationService) {
        super(authenticationManager);
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");

        String uri = request.getRequestURI();
        if (uri.startsWith("/session") || (uri.startsWith("/users") && request.getMethod().equals("POST"))) {
            chain.doFilter(request, response);
            return;
        }

        if (authorization == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String accessToken = authorization.substring("Bearer ".length());

        Claims claims = authenticationService.parseToken(accessToken);

        Role role = Role.valueOf(claims.get("role", String.class));
        Long userId = claims.get("userId", Long.class);

        Authentication authentication = new UserAuthentication(userId, role);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
