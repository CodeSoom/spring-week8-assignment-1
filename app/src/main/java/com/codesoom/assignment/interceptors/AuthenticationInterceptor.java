package com.codesoom.assignment.interceptors;

import com.codesoom.assignment.application.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
            ) throws Exception {
        return filterWithPathAndMethod(request) ||
                doAuthentication(request, response);
    }

    private boolean filterWithPathAndMethod(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (path.startsWith("/products") && (
                "GET".equals(method) || "OPTIONS".equals(method))
        ) {
            return true;
        }

        if ((path.startsWith("/session") || path.startsWith("/users"))
                && "POST".equals(method)) {
            return true;
        }
        return false;
    }

    /**
     * 유효한 토큰으로 요청했다면 true, 그렇지 않다면 false를 리턴합니다
     * @param request 요청
     * @param response 응답
     * @return {@code request} 에 있는 토큰이 유효한지 확인
     */
    private boolean doAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        String accessToken = authorization.substring("Bearer ".length());
        Long id = authenticationService.parseToken(accessToken);

        request.setAttribute("id", id);

        return true;
    }
}
