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
 * 유저 인증 filter.
 */
public class AuthenticationErrorFilter extends HttpFilter {

    /**
     *  http 요청, 응답에 대해 filter chain을 통해 filtering 한다.
     *
     * @param request http 요청
     * @param response http 응답
     * @param chain
     * @throws IOException 입출력에 오류가 있는 경우
     * @throws ServletException 서블릿에 오류가 있는 경우
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
