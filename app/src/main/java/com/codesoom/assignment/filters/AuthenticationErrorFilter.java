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
 * HTTP 요청 시 Request와 Response를 체크합니다.
 */
public class AuthenticationErrorFilter extends HttpFilter {
    /**
     * 에러를 확인하고 예외를 던지거나, 다음 Filter로 넘깁니다.
     *
     * @param request 클라이언트 요청 정보
     * @param response 서버 응답 정보
     * @param chain 연결된 Filter 정보
     * @throws IOException 요청-응답 간 예외가 발생 시 IOException을 던집니다.
     * @throws ServletException Servlet에서 예외가 발생 시 ServletException을 던집니다.
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
