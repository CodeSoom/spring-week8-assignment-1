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
 * 인증이 되었는지 확인을 한다.
 */
public class AuthenticationErrorFilter extends HttpFilter {

    /**
     * 인증이 되었는지 확인을 한다. 인증이 되지 않았으면 response를 보낸다.
     * @param request 요청된 request
     * @param response 반환할 response
     * @param chain 다음 filter과의 연결
     * @throws IOException
     * @throws ServletException
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
