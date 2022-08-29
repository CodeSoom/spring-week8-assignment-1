package com.codesoom.assignment.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {
        throw new IllegalStateException("유틸성 클래스입니다.");
    }

    /**
     * 컨텍스트에 저장된 인증 정보를 가져옵니다.
     *
     * @return 인증
     */
    public static Authentication getInfo() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
