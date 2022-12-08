package com.codesoom.assignment.common.authorization;

import com.codesoom.assignment.common.authentication.security.UserAuthentication;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class UserAuthorizationAop {
    private static final String MESSAGE = "회원의 아이디가 요청 아이디와 다릅니다.";

    /**
     * `@IdVerification`이 선언된 메서드가 호출될 때 AOP가 적용됩니다.
     */
    @Pointcut("@annotation(com.codesoom.assignment.common.authorization.IdVerification)")
    private void idVerificationMethod() {
    }

    /**
     * Access Token을 통해 전달되는 id와 @PathVariable을 통해 전달되는 id를 서로 비교합니다.
     *
     * @param joinPoint 메서드 관련 정보
     * @throws AccessDeniedException id가 존재하지 않거나 서로 다를 때 던집니다.
     */
    @Before("idVerificationMethod()")
    private void validateId(final JoinPoint joinPoint) throws AccessDeniedException {
        Long requestId = getIdByPathVariable(joinPoint);
        Long tokenId = getIdByUserAuthentication(joinPoint);

        if (!requestId.equals(tokenId)) {
            throw new AccessDeniedException(MESSAGE);
        }
    }


    private static Long getIdByPathVariable(final JoinPoint joinPoint) throws AccessDeniedException {
        return Arrays.stream(joinPoint.getArgs())
                .filter(Long.class::isInstance)
                .map(Long.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("회원 아이디가 존재하지 않습니다"));
    }

    private static Long getIdByUserAuthentication(final JoinPoint joinPoint) throws AccessDeniedException {
        UserAuthentication userAuthentication = Arrays.stream(joinPoint.getArgs())
                .filter(UserAuthentication.class::isInstance)
                .map(UserAuthentication.class::cast)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("회원 인증 정보가 존재하지 않습니다"));

        return userAuthentication.getUserId();
    }
}
