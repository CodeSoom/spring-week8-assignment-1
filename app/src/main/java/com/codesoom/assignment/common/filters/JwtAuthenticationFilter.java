package com.codesoom.assignment.common.filters;

import com.codesoom.assignment.auth.application.port.in.AuthenticationUseCase;
import com.codesoom.assignment.common.security.UserAuthentication;
import com.codesoom.assignment.role.domain.Role;
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
import java.util.List;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationUseCase authenticationUseCase;

    public JwtAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final AuthenticationUseCase authenticationUseCase) {
        super(authenticationManager);
        this.authenticationUseCase = authenticationUseCase;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain)
            throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null) {
            String accessToken = authorization.substring("Bearer ".length());
            Long userId = authenticationUseCase.parseToken(accessToken);
            List<Role> roles = authenticationUseCase.roles(userId);
            Authentication authentication =
                    new UserAuthentication(userId, roles);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
