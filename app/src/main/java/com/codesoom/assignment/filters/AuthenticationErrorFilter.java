package com.codesoom.assignment.filters;

import com.codesoom.assignment.errors.InvalidTokenException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

/**
 * {@link JwtAuthenticationFilter} 내부 에러 처리를 담당합니다.
 * <p>
 * 필터는 인터셉터와 달리 서블릿 내에서 동작하기 때문에,
 * <p>
 * 인터셉터처럼 사용하기 위해 필터 내에 발생한 에러 핸들링을 위한 클래스입니다.
 */
public class AuthenticationErrorFilter extends HttpFilter {

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
