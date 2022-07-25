package com.codesoom.assignment.filters;

import com.codesoom.assignment.errors.InvalidTokenException;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인증, 인가 예외 필터를 처리하고 응답
 */
public class AuthenticationErrorFilter extends HttpFilter {
    /**
     * 토큰 검증이 실패하고 예외가 발생하는 경우, 401 상태코드 응답
     *
     * @param request  HTTP 요청 정보
     * @param response HTTP 응답 정보
     * @param chain    필터 정보
     *
     * @throws IOException 입출력에 문제가 생긴 경우
     * @throws ServletException 서블릿에 문제가 생긴 경우
     */
    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (InvalidTokenException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
