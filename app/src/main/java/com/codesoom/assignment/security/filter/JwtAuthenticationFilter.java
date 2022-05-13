package com.codesoom.assignment.security.filter;

import com.codesoom.assignment.application.auth.AuthorizationService;
import com.codesoom.assignment.domain.users.Role;
import com.codesoom.assignment.security.UserAuthentication;
import com.codesoom.assignment.exceptions.InvalidTokenException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

/**
 *  요청 헤더에 포함된 토큰 검증을 담당합니다.
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final AuthorizationService authorizationService;
    private static final String PREFIX = "Bearer ";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthorizationService authorizationService) {
        super(authenticationManager);
        this.authorizationService = authorizationService;
    }

    /**
     * HttpHeader에 인증 토큰이 있다면 토큰 검증 후 SecurityContextHolder에 인증 정보를 등록합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            String accessToken = getAccessToken(request);
            Long userId = authorizationService.parseToken(accessToken);
            List<Role> roles = authorizationService.roles(userId);

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UserAuthentication(userId, roles);
            context.setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    /**
     * 요청 헤더에 담긴 토큰을 반환합니다.
     *
     * @param request http 요청
     * @return AccessToken
     * @throws InvalidTokenException 토큰이 유효하지 않은 경우
     */
    private String getAccessToken(HttpServletRequest request) {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(Strings.isBlank(token)) {
            throw new InvalidTokenException("토큰을 입력하세요.");
        }
        if (!token.startsWith(PREFIX)) {
            throw new InvalidTokenException("지원하지 않는 토큰 타입입니다.");
        }

        String accessToken = token.substring(PREFIX.length());
        return accessToken;
    }

}
